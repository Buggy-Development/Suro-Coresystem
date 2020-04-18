package de.TheHolyException.suro.listeners.spigot;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import de.TheHolyException.suro.CoreSpigot;

public class BlockBreak implements Listener {
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (!CoreSpigot.getInstance().getSuroEngine().isLaunched() && !event.getPlayer().hasPermission("system.*")) {
			event.setCancelled(true);
		}
	}
}
