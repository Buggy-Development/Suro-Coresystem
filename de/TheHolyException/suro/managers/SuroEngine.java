package de.TheHolyException.suro.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import codecrafter47.bungeetablistplus.api.bungee.BungeeTabListPlusAPI;
import codecrafter47.bungeetablistplus.api.bungee.tablist.FakePlayer;
import de.TheHolyException.suro.CoreSpigot;
import de.TheHolyException.suro.utils.JSONDataReader;
import de.TheHolyException.suro.utils.JSONReader;
import de.TheHolyException.suro.utils.PlatformType;
import de.TheHolyException.suro.utils.SUROPlayer;
import net.md_5.bungee.api.ProxyServer;

public class SuroEngine {

	private MySQLManager mysql = PlatformType.getMySQLManager();
	private boolean launched = false;
	private boolean introrunning = false;
	private HashMap<String,FakePlayer> fakeplayers = new HashMap<String,FakePlayer>();
	private Set<Player> players = new HashSet<Player>();
	private static List<Location> hiveblocks = new ArrayList<>();

	
	
	private final String offlinetag = "§7[§cOffline§7] §7";
	
	public void syncData() {
		if (!mysql.getDataManager().contains("launched")) mysql.getDataManager().set("launched", "false");
		launched = Boolean.valueOf(mysql.getDataManager().getServerDataValue("launched"));
	}
	
	public SuroEngine() {
		System.out.println(" ");
		System.out.println(" ");
		System.out.println("Started Suro Engine");
		System.out.println(" ");
		System.out.println(" ");
		syncData();
		
		if (PlatformType.currentType.equals(PlatformType.SPIGOT_1_14)) {
			Bukkit.getWorlds().forEach(world -> {
				world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
				world.setGameRule(GameRule.DISABLE_ELYTRA_MOVEMENT_CHECK, true);
				world.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, true);
				world.setGameRule(GameRule.NATURAL_REGENERATION, false);
				world.setDifficulty(Difficulty.HARD);
				world.setStorm(false);
				world.setThundering(false);
			});
			players.clear();
			Bukkit.getOnlinePlayers().forEach(player -> {
				System.out.println(player.getName());
				if (TokenManager.hasValidToken(player.getUniqueId())) {
					if (!TokenManager.isGamemaster(player.getUniqueId())) {
						players.add(player);
					}
				}
			});
		}

		Timer t = new Timer();
		t.schedule(new TimerTask() {
			int secs = 0;
			@Override
			public void run() {
				if (secs == 30) {
					//SAVE BACKUP LOCATION
					if (PlatformType.currentType.equals(PlatformType.SPIGOT_1_14) && !CoreSpigot.lobby && isLaunched()) {
						Bukkit.getOnlinePlayers().forEach(player -> {
							JSONDataReader reader = JSONDataReader.getDataReader(player.getUniqueId() );
							reader.set("logoutloc", LocationManager.getLocationAsString(player.getLocation()));
						});
					}	
					secs = 0;
				}
				secs ++;
				
				//Tablist OFFLINE TAG
				if (PlatformType.currentType.equals(PlatformType.BUNGEECORD)) {
					List<SUROPlayer> splayers = getAllPlayers();
					splayers.forEach(player -> {
						JSONDataReader reader = JSONDataReader.getDataReader(player.getUniqueId());
						if (reader.containsKey("token") && reader.getString("token", null).length() > 2) {
							String username = reader.getString("username", null);
							if (!player.isBanned() && !player.isDeath()) {
								if (!PlatformType.isPlayerOnline(player.getUniqueId())) {
									if (!fakeplayers.containsKey(username)) {
										FakePlayer fp = BungeeTabListPlusAPI.getFakePlayerManager().createFakePlayer(offlinetag + username, ProxyServer.getInstance().getServerInfo("suro"));
										fakeplayers.put(username,fp);
									}
								} else {
									if (fakeplayers.containsKey(username)) {
										BungeeTabListPlusAPI.getFakePlayerManager().removeFakePlayer(fakeplayers.get(username));
										fakeplayers.remove(username);
									}
								}
							} else {
								if (fakeplayers.containsKey(username)) {
									BungeeTabListPlusAPI.getFakePlayerManager().removeFakePlayer(fakeplayers.get(username));
									fakeplayers.remove(username);
								}
							}
						}
					});
				}				
			}
		}, 1000, 1000);
		
	}
	
	public List<SUROPlayer> getAllPlayers() {
		List<SUROPlayer> list = new ArrayList<SUROPlayer>();
		JSONReader tokens = JSONReader.getJSONReader("tokens");
		for (UUID uuid : mysql.getPlayerDataManager().getAllUsers().values()) {
			JSONDataReader reader = JSONDataReader.getDataReader(uuid);
			if (!TokenManager.isGamemaster(uuid)) {
				if (reader.containsKey("token") && reader.getString("token", null).length()>=24) {
					String tokenstr = reader.getString("token", null);
					JSONObject token = tokens.getJSONObject(tokenstr, null);
					if (token != null) {
						list.add(SUROPlayer.getSUROPlayer(uuid)
								.setDeath(reader.getBoolean("death", false))
								.setStreamer(reader.getBoolean("streamer", false))
								.setToken(reader.getString("token", null))
								.setBanned((token.containsKey("banned") ? Boolean.valueOf(token.get("banned").toString()) : false)));
					}
				}
			}
		}
		return list;
	}

	public List<SUROPlayer> getAllAlivePlayers() {
		List<SUROPlayer> list = new ArrayList<SUROPlayer>();
		for (SUROPlayer player : getAllPlayers()) {
			if (!player.isDeath() && !player.isBanned()) list.add(player);
		}
		return list;
	}
	
	public void buildHive(Location l) {
		List<Location> a = new ArrayList<Location>();
		a.add(l.clone().add(0, -1, 0));
		
		a.add(l.clone().add(1, 0, 0));
		a.add(l.clone().add(-1, 0, 0));
		a.add(l.clone().add(0, 0, 1));
		a.add(l.clone().add(0, 0, -1));
		
		a.add(l.clone().add(1, 1, 0));
		a.add(l.clone().add(-1, 1, 0));
		a.add(l.clone().add(0, 1, 1));
		a.add(l.clone().add(0, 1, -1));
		
		a.add(l.clone().add(0, 2, 0));
		a.forEach(loc -> loc.getBlock().setType(Material.WHITE_STAINED_GLASS));
		hiveblocks.addAll(a);
	}
	
	public void deleteAllHives(boolean effects) {
		DustOptions dust = new DustOptions(Color.SILVER, 2);
		hiveblocks.forEach(loc -> {
			loc.getBlock().setType(Material.AIR);
			if (effects) {
				Bukkit.getOnlinePlayers().forEach(player -> {
					if (player.getLocation().distance(loc) < 30) {
						player.spawnParticle(Particle.REDSTONE, loc, 3, 0.2d, 0, 0.2d, 0.6, dust);
					}
				});
			}
		});
	}
	
	public boolean isLaunched() {
		return launched;
	}
	
	public boolean isIntroRunning() {
		return introrunning;
	}
	
	public void setIntrorunning(boolean introrunning) {
		this.introrunning = introrunning;
	}
	
	public void setLaunched(boolean launched) {
		this.launched = launched;
		mysql.getDataManager().set("launched", launched+"");
	}
	
	public void addPlayer(Player player) {
		players.add(player);
	}
	
	public void removePlayer(Player player) {
		if (players.contains(player)) players.remove(player);
	}
	
	public boolean containsPlayer(Player player) {
		return players.contains(player);
	}

}
