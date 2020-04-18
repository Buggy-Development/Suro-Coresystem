package de.TheHolyException.suro.listeners.spigot;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import de.TheHolyException.suro.utils.LuckPermsUtils;

public class PlayerChat implements Listener {
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		String raw = LuckPermsUtils.getMetaData(event.getPlayer().getUniqueId()).getPrefix();
		if (raw.length() > 4) {
			String[] rawprefix = new String[] {raw.substring(raw.length()-2, raw.length()),raw.substring(0,raw.length()-6)};
			String playercolor = rawprefix[0].replace("&", "§");
			String prefix = rawprefix[1].replace("&", "§");
			event.setFormat(prefix + " " + playercolor + event.getPlayer().getName() + " §8» §7" + event.getMessage());
		} else {
			event.setFormat("§7" + event.getPlayer().getName() + " §8» §7" + event.getMessage());
		}
	}

}
