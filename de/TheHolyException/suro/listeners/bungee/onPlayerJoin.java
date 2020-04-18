package de.TheHolyException.suro.listeners.bungee;

import de.TheHolyException.suro.CoreBungee;
import de.TheHolyException.suro.utils.JSONDataReader;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class onPlayerJoin implements Listener {
	
	@EventHandler
	public void onJoin(LoginEvent event) {
		if (CoreBungee.getInstance().getMySQLManager().getPlayerDataManager().contains(event.getConnection().getUniqueId().toString())) {
			JSONDataReader reader = JSONDataReader.getDataReader(event.getConnection().getUniqueId());
			if (!reader.getBoolean("death", false)) return;
			if (reader.containsKey("gamemaster") && reader.getBoolean("gamemaster", false)) return;
			if (reader.containsKey("streamer") && reader.getBoolean("streamer", false)) return;

			event.getConnection().disconnect(new TextComponent("§cDu bist ausgeschieden\n\n§7Vielen dank für deine Teilnahme!\n\n§bhttps://twitter.com/MineBug_DE\n\n§7Hosted by §bMineBug.de"));
//			if (!reader.getBoolean("death", false) 
//					|| (reader.containsKey("gamemaster") && reader.getBoolean("gamemaster", false)) 
//					|| (reader.containsKey("streamer") && reader.getBoolean("streamer", false))) {
//				event.getConnection().set
//			} else {
//				event.getConnection().disconnect(new TextComponent("§cDu bist ausgeschieden\n\n§7Vielen dank für deine Teilnahme!\n\n§bhttps://twitter.com/MineBug_DE\n\n§7Hosted by §bMineBug.de"));
//			}
		}
	}
}
