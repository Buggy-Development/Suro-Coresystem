package de.TheHolyException.suro.managers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;

import de.TheHolyException.suro.CoreSpigot;
import de.TheHolyException.suro.utils.ItemManager;
import de.TheHolyException.suro.utils.JSONDataReader;
import de.TheHolyException.suro.utils.SUROPlayer;

public class ZombieManager {

	public HashMap<String, Zombie> zombiePlayers = new HashMap<String, Zombie>();
	
	private ZombieDropManager zombieDropManager;
	
	public ZombieDropManager getZombiedropManager() {
		if (zombieDropManager == null) zombieDropManager = new ZombieDropManager();
		return zombieDropManager;
	}
	
	public HashMap<String,Zombie> getZombiePlayers() {
		return zombiePlayers;
	}
	
	public void createZombie(Player player) {
		System.out.println("[ZombieManager] spawning Zombie from " + player.getName() + " Methode#1");
		Zombie zombie = player.getWorld().spawn(player.getLocation(), Zombie.class);
		zombie.setAI(false);
		zombie.setSilent(true);
		zombie.setCanPickupItems(false);
		zombie.getEquipment().setHelmet(new ItemManager(Material.LEATHER_HELMET).setColor(7).setDisplayName("Leder Helm kappa")
				.addLoreLines("Falls du dieses Item haben solltest", "dann lösche es bitte ^^", "","§4~Eure GameMaster").setUnbreakable(true).build());
		zombie.getEquipment().setHelmetDropChance(0);
		zombie.setBaby(false);
		zombie.setCustomName(player.getName());
		zombie.setCustomNameVisible(true);
		zombiePlayers.put(player.getName(), zombie);
	}
	
	public void createZombie(String name, Location location) {
		System.out.println("[ZombieManager] spawning Zombie from " + name + " Methode#2");
		Zombie zombie = location.getWorld().spawn(location, Zombie.class);
		zombie.setAI(false);
		zombie.setSilent(true);
		zombie.setCanPickupItems(false);
		zombie.getEquipment().setHelmet(new ItemManager(Material.LEATHER_HELMET).setColor(7).setDisplayName("Leder Helm kappa")
				.addLoreLines("Falls du dieses Item haben solltest", "dann lösche es bitte ^^", "","§4~Eure GameMaster").setUnbreakable(true).build());
		zombie.getEquipment().setHelmetDropChance(0);
		zombie.setBaby(false);
		zombie.setCustomName(name);
		zombie.setCustomNameVisible(true);
		zombiePlayers.put(name, zombie);
	}

	public void removeZombie(Player player) {
		for (World world : Bukkit.getWorlds()) {
			for (Entity en : world.getEntities()) {
				if (en.getCustomName() != null) {
					System.out.println("[ZombieManager] delete Zombie from " + en.getCustomName());
					if (en.getCustomName().equalsIgnoreCase(player.getName())) {
						en.remove();
						return;
					}
				}
			}
		}
	}
	
	public void removeAllZombies() {
		for (Iterator<String> iterator = zombiePlayers.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			zombiePlayers.get(key).remove();
			zombiePlayers.remove(key);
		}
		for (World world : Bukkit.getWorlds()) {
			for (Entity en : world.getEntities()) {
				if (en.getCustomName() != null) en.remove();
			}
		}
	}
	
	public void spawnAllZombies() {
		Map<UUID, String> users = CoreSpigot.getInstance().getMySQLManager().getPlayerDataManager().getAllUsersUUIDKey();
		for (SUROPlayer player : CoreSpigot.getInstance().getSuroEngine().getAllAlivePlayers()) {
			Player bukkitplayer = Bukkit.getPlayer(player.getUniqueId());
			if (bukkitplayer != null && !bukkitplayer.isOnline()) {
				if (!TokenManager.isGamemaster(bukkitplayer.getUniqueId()) && !TokenManager.isDeath(bukkitplayer.getUniqueId())) {
					JSONDataReader reader = JSONDataReader.getDataReader(player.getUniqueId());
					if (reader.containsKey("logoutloc") && LocationManager.getStringAsLocation(reader.getString("logoutloc", null)) != null) {
						createZombie(users.get(player.getUniqueId()), LocationManager.getStringAsLocation(reader.getString("logoutloc", null)));
					}
				}
			}
		}
	}
}
