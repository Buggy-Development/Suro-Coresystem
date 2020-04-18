package de.TheHolyException.suro.managers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.json.simple.JSONObject;

import de.TheHolyException.suro.CoreSpigot;
import de.TheHolyException.suro.listeners.spigot.EntityDamageEntity;
import de.TheHolyException.suro.utils.JSONDataReader;
import de.TheHolyException.suro.utils.JSONReader;
import de.TheHolyException.suro.utils.PlatformType;
import de.TheHolyException.suro.utils.SUROPlayer;

public class SuroEngineSpigot extends SuroEngine {

	private BukkitTask task;
	
	public SuroEngineSpigot() {
		
		task = new BukkitRunnable() {
			
			@Override
			public void run() {
				if (!CoreSpigot.lobby) {
					List<SUROPlayer> alivePlayers = getAllAlivePlayers();
					if (alivePlayers.size() == 1) {
						checkWinner();
						this.cancel();
					}
				}
			}
		}.runTaskTimer(CoreSpigot.getInstance(), 20, 20);
		
	}
	
	
	
	public void checkWinner() {
		SuroEngineSpigot engine = CoreSpigot.getInstance().getSuroEngine();
		List<SUROPlayer> players = engine.getAllAlivePlayers();
		if (engine.getAllAlivePlayers().size() == 1) {
			SUROPlayer winner = players.get(0);
			Bukkit.broadcastMessage("§c§lSURO §7>> §cDer Spieler §e" + CoreSpigot.getInstance().getMySQLManager().getPlayerDataManager().getAllUsersUUIDKey().get(winner.getUniqueId()) + " §chat §lSuRo §cgewonnen!");
			task.cancel();
		}
	}
	
	public void startPrepairTime() {
		Bukkit.getWorlds().forEach(world -> {
			world.setTime(2000);
			world.setDifficulty(Difficulty.PEACEFUL);
		});
		Set<Player> teleporting = new HashSet<Player>();
		if (PlatformType.currentType.equals(PlatformType.SPIGOT_1_14)) {
			String[] messages = new String[] {"§c10","§c9","§c8","§c7","§c6","§c5","§c4","§63","§e2","§a1","§2Los Geht's"};
			new BukkitRunnable() {
				int tick = -5;
				@Override
				public void run() {
					Bukkit.getOnlinePlayers().forEach(player -> {
						if (!TokenManager.isGamemaster(player.getUniqueId())) {
							if (player.getLocation().getY() < 239 && ! teleporting.contains(player)) {
								teleporting.add(player);
								player.teleport(LocationManager.getLocationManager("locations").getLocation("suro_firsttp"));
//								new TeleportGoneWild().WildTeleport(player, 2500, -2500, 2500, -2500, true);
								CoreSpigot.getInstance().getTeleportManager().TeleportRandom(player, player.getWorld());
							}
						}
					});
					if (tick >= 0) {
						Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle(messages[tick], "", 4, 9, 4));
					}
					if (tick == 0) {
						Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, 0));
					}
					if (tick > 6 && tick != 10) {
						Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, 0));
					}
					if (tick == 10) {
						Bukkit.getOnlinePlayers().forEach(player -> {
							player.playSound(player.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1);
							player.playSound(player.getEyeLocation(), Sound.ENTITY_ENDER_DRAGON_HURT, 1, 0);
							EntityDamageEntity.saveThisDude(player);
						});
						deleteAllHives(true);
						setLaunched(true);
						Bukkit.getWorlds().forEach(world -> world.setDifficulty(Difficulty.HARD));
						this.cancel();
					}

					tick ++;
				}
			}.runTaskTimer(CoreSpigot.getInstance(), 20, 20);
		}
	}
	
	
	@SuppressWarnings("deprecation")
	public void initIntro(Player player) {
		player.setGameMode(GameMode.SPECTATOR);
		Bukkit.getOnlinePlayers().forEach(all -> {
			if (all != player) {
				player.hidePlayer(all);
			}
		});
		player.teleport(LocationManager.getLocationManager("locations").getLocation("intro_teleport"));
		player.playSound(player.getEyeLocation(), Sound.MUSIC_DISC_BLOCKS, 0.8f, 0.9f);
	}
	
	private long timestarted;
	
	public void startIntro() {
		if (PlatformType.currentType.equals(PlatformType.SPIGOT_1_14)) {
			setIntrorunning(true);
			Bukkit.getOnlinePlayers().forEach(player -> {
				initIntro(player);
			});

			String[] messages = new String[] {"#E30","#B§c§lMineBug#n§cpresents#E60", "#B§2§lSuRo#n§7(§aMit großem 'R'§7)#E310","#RUN_ST1#"
					
					,"#E20","§c§lMuhahaha#E50","§cIhr fragt Euch sicher,#n was Ihr hier tut?","§cIhr seid Teil#n eines neuen Versuchs;","§ceines Versuchs#n uns zu unterhalten!"
					,"Wir haben#n Euch ausgewählt;","Wir haben#n Euch verschleppt;","Wir haben#n Euch die Freiheit geraubt;","Wir haben#n Euch die Regeln geraubt!","###"
					
					,"Diese Insel#n ist Euer neues Heim.","Ein Heim#n der verzweiflung!#E10","Oder?#E30","Es liegt an Euch,","Handelt;#E20","Lebt;#E20","Tötet;#E20","Tut was Euer Herz begehrt,","doch bedenkt stets: #nEuer Tot ist endgültig.","###"
					,"Und nur der letzte#n noch Lebende," ,"darf diese Welt#n wieder verlassen!", "###"
					
					,"§4§l~Eure Game Master", "§4§lTheHolyException §cund §4§lJulian4060206","#STOP#"};
			Map<Long,String> messageontick = new HashMap<>();

			long lasttick  = 10;
			for (String msg : messages) {
				messageontick.put(lasttick, (msg.contains("#E") ? msg.split("#E")[0] : msg));
				if (msg.contains("#E")) {
					lasttick = lasttick + Long.valueOf(msg.split("#E")[1]);
				}
				lasttick = msg.equalsIgnoreCase("###") ? (lasttick + 100) : (lasttick + (msg.length()*3));
			}
			
			List<Location> camlocs = new ArrayList<Location>();
			Map<Integer,Location> unsorted = new HashMap<>();
			JSONReader reader = JSONReader.getJSONReader("locations");
			for (Object key : reader.getJSONData().keySet()) {
				if (String.valueOf(key).startsWith("intro_cam_")) {
					unsorted.put(Integer.valueOf(String.valueOf(key).replace("intro_cam_", "")), LocationManager.getLocationManager("locations").getLocation(key.toString()));
				}
			}
			Map<Integer,Location> sorted = new TreeMap<Integer, Location>(unsorted);
			for (int key : sorted.keySet()) {
				camlocs.add(sorted.get(key));
			}
			
			timestarted = System.currentTimeMillis();
			Bukkit.getOnlinePlayers().forEach(player -> {
				try {
					CameraStudio.travel(player, camlocs, CameraStudio.parseTimeString("2m29s"), null, null);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			});
			
			new BukkitRunnable() {
				int intro_tick  = 0;
				long lastmessagetick = 0;
				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					for (Long t : messageontick.keySet()) {
						if (intro_tick == t) {
							String message = messageontick.get(t);
							if (message.equals("###")) break;
							if (message.equals("#RUN_ST1#")) {
								Bukkit.getOnlinePlayers().forEach(player -> {
									player.getWorld().spawn(LocationManager.getLocationManager("locations").getLocation("mid"), LightningStrike.class);
//									player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20000, 0, false, false));
//									player.teleport(LocationManager.getLocationManager("locations").getLocation("intro_teleport"));
								});
								break;
							}
							if (message.equals("#STOP#")) {
								setIntrorunning(false);
								JSONReader serverreader = JSONReader.getJSONReader("tokens");
								PluginMessagingManagerSpigot.sendMessageToBungee(null, "suro:sync", "#DATASTREAM#SUROENGINE#SYNC");
								PluginMessagingManagerSpigot.sendMessageToBungee(null, "suro:sync", "#DATASTREAM#SUROENGINE#STARTPREPAIRTIME");
								org.bukkit.Bukkit.getOnlinePlayers().forEach(player -> {
									player.removePotionEffect(PotionEffectType.INVISIBILITY);
									player.removePotionEffect(PotionEffectType.BLINDNESS);
									player.stopSound(Sound.MUSIC_DISC_BLOCKS);
									JSONDataReader reader = JSONDataReader.getDataReader(player.getUniqueId());
									if (reader.containsKey("token")) {
										JSONObject token = serverreader.getJSONObject(reader.getString("token", null), null);
										if (!(token.containsKey("banned") && Boolean.valueOf(token.get("banned").toString()))) {
											CoreSpigot.getInstance().getPluginMessagingManager().sendMessageToBungee(player, "Connect", "suro");
										} else {

											player.setGameMode(GameMode.ADVENTURE);
											player.teleport(player.getWorld().getSpawnLocation());
											Bukkit.getOnlinePlayers().forEach(all -> {
												if (all != player) player.showPlayer(all);
											});
										}
									} else {
										player.setGameMode(GameMode.ADVENTURE);
										player.teleport(player.getWorld().getSpawnLocation());
										Bukkit.getOnlinePlayers().forEach(all -> {
											if (all != player) player.showPlayer(all);
										});
									}
								});
								System.out.println(System.currentTimeMillis()-timestarted);
								JSONReader reader = JSONReader.getJSONReader("settings");
								reader.set("tokenreading", false);
								reader.set("portal", true);
								reader.set("resourcepackrequest",false);
								PlatformType.syncServerData("settings");
								this.cancel();
								break;
							}
							String top;
							String bottom;
							if (message.startsWith("#B")) {
								message = message.replace("#B","");
								if (message.contains("#n")) {
									String[] a = message.replace("#b", "").split("#n");
									top = a[0];
									bottom= a[1];
								} else {
									top = message.replace("#b", "");
									bottom = "";
								}
							} else {
								bottom = message.replace("#b", "").replace("#n", "");
								top = "";
							}
							Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle("§c"+top, "§c"+bottom, (int)(t-lastmessagetick)/2, (int)(t/2-lastmessagetick), (int)(t-lastmessagetick)/2));
							lastmessagetick = t;
							break;
						}
					}
					intro_tick ++;
				}
			}.runTaskTimer(CoreSpigot.getInstance(), 50, 0);
		}
	}
	

}
