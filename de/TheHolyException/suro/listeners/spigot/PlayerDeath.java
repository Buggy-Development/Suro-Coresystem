package de.TheHolyException.suro.listeners.spigot;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import de.TheHolyException.suro.CoreSpigot;
import de.TheHolyException.suro.utils.ItemManager;
import de.TheHolyException.suro.utils.JSONDataReader;

public class PlayerDeath implements Listener {

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		
		Player player = event.getEntity();
		JSONDataReader reader = JSONDataReader.getDataReader(player.getUniqueId());
		reader.set("death", true);
		new BukkitRunnable() {
			
			@Override
			public void run() {
				player.kickPlayer("§cDu bist ausgeschieden\n\n§7Vielen dank für deine Teilnahme!\n\n§bhttps://twitter.com/MineBug_DE\n\n§7Hosted by §bMineBug.de");
			}
		}.runTaskLater(CoreSpigot.getInstance(), 20);
		if (player.getKiller() != null) {
			Player killer = player.getKiller();
			Bukkit.broadcastMessage("§4§l✟ §c§l" + player.getName() + " §7§l⚔ §e§l" + killer.getName());
		} else {
			Bukkit.broadcastMessage("§4§l✟ §e" + player.getName());
		}
		CoreSpigot.getInstance().getZombieManager().getZombiedropManager().dropInventory(player, player.getLocation());
		onDeath(player, player.getLocation());
		CoreSpigot.getInstance().getSuroEngine().checkWinner();
	}
	
	
	public static void onDeath(OfflinePlayer player, Location loc) {
		Bukkit.getOnlinePlayers().forEach(all -> {
			all.playSound(all.getEyeLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 10, 0)	;
		});
		loc.getWorld().dropItem(loc, new ItemManager(Material.DIAMOND).setDisplayName("§5§lDiamant").setGlow().build());
	}	
}