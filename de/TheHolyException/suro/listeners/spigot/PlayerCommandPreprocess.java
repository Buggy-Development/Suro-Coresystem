package de.TheHolyException.suro.listeners.spigot;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.help.HelpTopic;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import de.TheHolyException.suro.utils.Messages;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerCommandPreprocess implements Listener {
	

	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String cmd = event.getMessage();
		if (!event.isCancelled()) {
			HelpTopic topic = Bukkit.getServer().getHelpMap().getHelpTopic(cmd.split(" ")[0]);
			if (topic == null) {
				Messages.send(player, "§7Dieser Befehl wurde nicht Gefunden.");
				event.setCancelled(true);
			}
		}
		if (cmd.equalsIgnoreCase("/icanhasbukkit") || cmd.equalsIgnoreCase("/ver") || cmd.equalsIgnoreCase("/version") || cmd.equalsIgnoreCase("/about")) {
			 player.sendMessage("This server is running on MineBug version bug-MineBug-127 (MC:1.14.2) (Implementing API version 1.14.2-R0.1-SNAPSHOT)");
			 event.setCancelled(true);
		} else if (cmd.equalsIgnoreCase("/reload") || cmd.equalsIgnoreCase("/rl")) {
			if (player.hasPermission("system.reload")) {
				event.setCancelled(true);
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage("§c§lAchtung: §cAlle Serverdaten werden Neu geladen!");
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage("§6Bitte solange §lNICHT §6Bewegen oder schreiben.");
				Bukkit.broadcastMessage("§eDer Server könnte abstürzen!");
				Bukkit.broadcastMessage("");
				Bukkit.reload();
				Bukkit.broadcastMessage("§aDer Server wurde Erfolgreich Neu geladen!");
				Bukkit.broadcastMessage("§aDu kannst nun wieder normal Spielen.");
				Bukkit.broadcastMessage("");
			}
		} else if (cmd.equalsIgnoreCase("/plugins") || cmd.equalsIgnoreCase("/pl") || cmd.equalsIgnoreCase("/?") || cmd.equalsIgnoreCase("/help")) {
			event.setCancelled(true);
			if (player.hasPermission("system.plugins")) {
				Plugin[] pl = Bukkit.getServer().getPluginManager().getPlugins();
				ArrayList<String> names = new ArrayList<>();
				for (Plugin pls : pl) {
					names.add(pls.getName());
				}
				int plsize = pl.length;
				String plugins;
				char color = 'a';
				if (plsize >= 50) {
					color = '4';
				} else if (plsize >= 35) {
					color = 'c';
				} else if (plsize >= 25) {
					color = '6';
				} else
					color = 'a';
				plugins = "§" + color + plsize;

				player.sendMessage("§8[]§7======§8[§cPlugins§8]§7======§8[]");

				player.sendMessage(" ");
				player.sendMessage("§" + color + "Plugins §7[" + plugins + "§7] §8:");
				player.sendMessage(" ");

				boolean a = false, b = false;

				
				Collections.sort(names, String.CASE_INSENSITIVE_ORDER);

				TextComponent pluginlist = new TextComponent("");
				int i = 0;
				for (String plugi : names) {
					String pluginpref = "§7";
					Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(plugi);
					PluginDescriptionFile pldes = plugin.getDescription();
					if (getByName(plugi).isEnabled()) {
						if (pldes.getAuthors().contains("TheHolyException")) {
							pluginpref = "§6";
							a = true;
						}
					} else {
						pluginpref = "§4";
						b = true;
					}
					
					TextComponent pldata = Messages.createCommandHoverText(pluginpref+plugi, 
					"§6Name §7-> §e"+pldes.getName()
					+"\n§6FullName §7-> §e"+pldes.getFullName()
					+"\n§6Description §7-> §e"+pldes.getDescription()
					+"\n§6Authors §7-> §e"+pldes.getAuthors()
					+"\n§6Version §7-> §e"+pldes.getVersion()
					+"\n§6Load §7-> §e"+pldes.getLoad()
					+"\n§6LoadBefore §7-> §e"+pldes.getLoadBefore()
					+"\n§6Depends §7-> §e"+pldes.getDepend()
					+"\n§6SoftDepends §7-> §e"+pldes.getSoftDepend()
					+"\n§6Website §7-> §e"+pldes.getWebsite()
					+"\n\n§cAnklicken für mehr informationen"
					, "/plugininfo " + plugi);

					if (i == 1) {	
						pluginlist.addExtra(new TextComponent("§e, "));
					} else {
						i = 1;
					}
					if (pluginlist.toPlainText().length() > 150) {
						pluginlist.addExtra(pldata);
						player.spigot().sendMessage(pluginlist);
						pluginlist = new TextComponent("");
						i = 0;
					} else {
						pluginlist.addExtra(pldata);
					}

				}
				
				player.spigot().sendMessage(pluginlist);

				player.sendMessage(" ");
				player.sendMessage("§7Normale Plugins.");
				if (a == true) {
					player.sendMessage("§6Special Plugins. xD");
				}
				if (b == true) {
					player.sendMessage("§cPlugins die zu blöd zum laden sind.");
				}
				player.sendMessage("§eKomma Zeichen :P");
				player.sendMessage(" ");
				player.sendMessage("§8[]§7======§8[§cPlugins§8]§7======§8[]");
			} else player.sendMessage("Plugins (Error: 404): §aAnti-"+player.getName()+"-Plugin");
		}
	}
	
	
	private static Plugin getByName(String name) {
		for (Plugin pl : Bukkit.getServer().getPluginManager().getPlugins()) {
			if (pl.getName().equalsIgnoreCase(name)) {
				return pl;
			}
		}
		return null;
	}
	
	
	
}