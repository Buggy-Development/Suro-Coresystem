package de.TheHolyException.suro.listeners.spigot;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.codec.binary.Base64;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.TheHolyException.suro.CoreSpigot;
import de.TheHolyException.suro.managers.ZombieManager;
import de.TheHolyException.suro.utils.AsyncTask;
import de.TheHolyException.suro.utils.ItemManager;
import de.TheHolyException.suro.utils.JSONDataReader;
import de.TheHolyException.suro.utils.JSONReader;

public class PlayerJoin implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage("");
		Player player = event.getPlayer();
		JSONDataReader reader = JSONDataReader.getDataReader(player.getUniqueId());
		reader.set("username", player.getName());
		
		
		if (!CoreSpigot.lobby) {
			ZombieManager npc = CoreSpigot.getInstance().getZombieManager();
			npc.removeZombie(player);
			if (reader.getBoolean("gamemaster", false)) {
				player.setGameMode(GameMode.SPECTATOR);
				player.getInventory().clear();
				player.getInventory().setItem(4, new ItemManager(Material.COMPASS).setDisplayName("§6Zuschauen").build());
			} else if (reader.getBoolean("death", false)) {
				if (reader.containsKey("streamer") && reader.getBoolean("streamer", false)) {
					
				} else {
					player.kickPlayer("§cDu bist ausgeschieden\n\n§bVielen dank für deine Teilnahme!\n\n§7Hosted by §bMineBug.de");
				}
			}
			if (!reader.getBoolean("gamemaster", false)) {
				CoreSpigot.getInstance().getSuroEngine().addPlayer(player);
				if (!CoreSpigot.getInstance().getSuroEngine().isLaunched()) {
					CoreSpigot.getInstance().getTeleportManager().TeleportRandom(player, player.getWorld());
				}
			}
		} else {
			JSONReader serverreader = JSONReader.getJSONReader("settings");
			if (serverreader.getBoolean("resourcepackrequest", true)) Bukkit.getScheduler().runTaskLater(CoreSpigot.getInstance(), () -> player.setResourcePack("http://download.minebug.de/files/Suro_Resource_Pack.zip"), 20);
			player.setGameMode(GameMode.ADVENTURE);
			if (CoreSpigot.getInstance().getSuroEngine().isIntroRunning()) {
				CoreSpigot.getInstance().getSuroEngine().initIntro(player);
			} else {
				player.teleport(Bukkit.getWorld("Lobby").getSpawnLocation());
			}
		}
		
		AsyncTask.getAsyncTask().executeAsync(a -> {
	        String url = "http://textures.minecraft.net/texture/" + player.getUniqueId().toString();
	        if (!url.isEmpty()) {
	        	JSONReader headcache = JSONReader.getJSONReader("skull_cache");
	        	if (headcache.containsKey(player.getUniqueId().toString())) {
	        		String cachedata = headcache.getString(player.getUniqueId().toString(), null);
	    	        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
	    	        if (!new String(encodedData).equals(cachedata)) {
	    	        	headcache.set(player.getUniqueId().toString(), new String(encodedData));
	    	        }
	        	}
	        }
		});
		
	}
}
