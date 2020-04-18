package de.TheHolyException.suro.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.mojang.datafixers.util.Pair;

@SuppressWarnings("unchecked")
public class ZombieDropManager {
	
	public void saveInventory(Player player) {
		Pair<File,FileConfiguration> data = getFileConfiguration(player.getUniqueId());
		FileConfiguration cfg = data.getSecond();
		List<ItemStack> armor = new ArrayList<ItemStack>();
		List<ItemStack> contents = new ArrayList<ItemStack>();
		for (ItemStack item : player.getInventory().getArmorContents()) if (item != null) armor.add(item);
		for (ItemStack item : player.getInventory().getContents()) if (item != null) contents.add(item);
		cfg.set("content", contents);
		try {cfg.save(data.getFirst());} catch (IOException e) {e.printStackTrace();}
	}
	
	private Pair<File, FileConfiguration> getFileConfiguration(UUID uuid) {
		File folder = new File("plugins//CoreSystemSuro//inventorys//");
		if (!folder.exists()) folder.mkdirs();
		File file = new File("plugins//CoreSystemSuro//inventorys//" + uuid.toString()+".yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);		
		return Pair.of(file, cfg);
	}
	public List<ItemStack> getContents(UUID uuid) {
		Pair<File,FileConfiguration> data = getFileConfiguration(uuid);
		FileConfiguration cfg = data.getSecond();
		if (!cfg.contains("content")) return null;
		List<ItemStack> items = (List<ItemStack>) cfg.getList("content");
		return items;
	}
	
	public void dropInventory(OfflinePlayer player, Location location) {
		List<ItemStack> content = getContents(player.getUniqueId());
		if (content != null) {
			for (ItemStack item : content) {
				location.getWorld().dropItem(location, item);
			}
		} else System.out.println("Contents is null");
	}

}
