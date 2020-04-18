package de.TheHolyException.suro.listeners.spigot;

import org.bukkit.EntityEffect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import de.TheHolyException.suro.CoreSpigot;

public class PlayerMove implements Listener {
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (CoreSpigot.lobby) {
			if (!CoreSpigot.getInstance().getBuildmanager().containsPlayer(player)) {
				if (player.getLocation().getY() < 150) {
					player.teleport(player.getWorld().getSpawnLocation());
					player.playEffect(EntityEffect.TOTEM_RESURRECT);
					player.playSound(player.getEyeLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.2f, 1f);
				}
			}
		}
	}

}
