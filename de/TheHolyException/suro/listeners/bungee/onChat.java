package de.TheHolyException.suro.listeners.bungee;

import de.TheHolyException.suro.utils.Messages;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

@SuppressWarnings("deprecation")
public class onChat implements Listener {
	
	public static String[] words = new String[] {"sieg heil", "judenvergasung", "ficken","hitler", "adolf hitler", "hurensohn", "fotze", "ficker", "missit", "vergaser", "schwuchtel", "cock", "penis"
			, "pimmel", "hure", "hurensoh", "huren", "pisser", "l2p", "l²p", "e²", "geschlechtsverkehr", "wichskrüppel", "arschlochkind", "penisschlumpf", "sperma", "wichs"
			, "sex", "muschi", "vagina", "erektion", "hoden", "möse", "cracknutte", "tripper", "sack", "pisse", "kotze", "scheiss", "vorhaut", "prostata"
			, "schlampe", "luder", "pissnelke", "motherfucker", "fick", "wichser", "asshole", "dumbass", "bitch", "huso", "titten", "wixer", "wixxer", "wikser", "noob", "noop"
			, "nigger", "e2", "spast", "bastard", "Fvck", "hvere", "wxer", "Gasdusche", "vergasen", "n4b", "nob", "gaskammer", "ez", "arschloch", "arsch", "bist du dum","fuck","fck"}; 
	
	public static String[] werbung = new String[] {".de", ".d e", "[punkt]", "(punkt)", ".dee", "xxx", ".ip", ".biz", "join now", ".pl", ".net", "mein netzwerk", "noip", "c o m", "x x x", "c om", ".com", "www"
			, ".tk", ",de", ",com", ",tk", ",net", "(,)", "(.)", "sytrex", "s y t r e x", "s ytrex", "sy trex", "syt rex", "sytr ex", "sytre x", "sy tre x", "sytr ex", "s ytrex"
			, "syntaxgaming", "syntax gaming", "blauban", "blau ban", "b lauban", "s yntaxgaming", "[.]", ".tv"};
	
	@EventHandler
	public void onChatEvent(ChatEvent event) {
		ProxiedPlayer sender = (ProxiedPlayer) event.getSender();
		String message = event.getMessage();
		
		if (!sender.hasPermission("system.chatfilter.bypass") && !message.startsWith("/")) {
			System.out.println(event.getMessage());
			for (String word : words) {
				if (message.toLowerCase().contains(word)) {
					event.setCancelled(true);
					sender.sendMessage(Messages.prefix + " §cDeine Nachricht wurde nicht gesendet§8.");
					sender.sendMessage(Messages.prefix + " §cGrund: §bMögliche Beleidigungen§8.");
					ProxyServer.getInstance().getPlayers().forEach(player -> {
						if (player.hasPermission("system.chatfilter.notify")) {
							player.sendMessage("§8§l۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞");
							player.sendMessage("§7Der Spieler §e" + sender.getName() + " §7hat eine Beleidgung geschrieben§8.");
							player.sendMessage("§7Nachricht§8: §e" + event.getMessage());
							player.sendMessage("§8§l۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞");
						}
					});
					break;
				}
			}
			for (String word : werbung) {
				if (message.toLowerCase().contains(word)) {
					event.setCancelled(true);
					sender.sendMessage(Messages.prefix + " §cDeine Nachricht wurde nicht gesendet§8.");
					sender.sendMessage(Messages.prefix + " §cGrund: §bMögliche Werbung§8.");
					ProxyServer.getInstance().getPlayers().forEach(player -> {
						if (player.hasPermission("system.chatfilter.notify")) {
							player.sendMessage("§8§l۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞");
							player.sendMessage("§7Der Spieler §e" + sender.getName() + " §7hat versucht Werbung zu machen§8.");
							player.sendMessage("§7Nachricht§8: §e" + event.getMessage());
							player.sendMessage("§8§l۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞۞");
						}
					});
					break;
				}
			}			
		}
	}
}
