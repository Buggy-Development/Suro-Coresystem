package de.TheHolyException.suro.listeners.spigot;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import de.TheHolyException.suro.CoreSpigot;
import de.TheHolyException.suro.managers.LocationManager;
import de.TheHolyException.suro.managers.TokenManager;
import de.TheHolyException.suro.managers.ZombieManager;
import de.TheHolyException.suro.utils.JSONDataReader;

public class PlayerQuit implements Listener {
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		event.setQuitMessage("");
		Player player = event.getPlayer();
		JSONDataReader.removePlayerReader(player.getUniqueId());
		if (!CoreSpigot.lobby) {
			if (CoreSpigot.getInstance().getSuroEngine().containsPlayer(player)) {
				if (!TokenManager.isGamemaster(player.getUniqueId())) {
					if (!TokenManager.isDeath(player.getUniqueId())) {
						ZombieManager npc = CoreSpigot.getInstance().getZombieManager();
						npc.createZombie(player);
						npc.getZombiedropManager().saveInventory(player);
						CoreSpigot.getInstance().getSuroEngine().removePlayer(player);
						JSONDataReader reader = JSONDataReader.getDataReader(player.getUniqueId());
						reader.set("logoutloc", LocationManager.getLocationAsString(player.getLocation()));
					}
				}
			}
		}
	}
}
