package de.TheHolyException.suro.listeners.spigot;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status;

import de.TheHolyException.suro.utils.JSONReader;

public class PlayerResourcePackStatus implements Listener {
	
	@EventHandler
	public void onResourcePackchangeStatus(PlayerResourcePackStatusEvent event) {
		JSONReader serverreader = JSONReader.getJSONReader("settings");
		if (serverreader.getBoolean("resourcepackrequest", true)) 
		if (event.getStatus().equals(Status.DECLINED)) {
			event.getPlayer().kickPlayer("§cBitte akzeptiere das Server ResourcePack\n\n§cÄndere es Notfalls in den einstellungen des Gespeicherten Servers.");
		}
	}

}
