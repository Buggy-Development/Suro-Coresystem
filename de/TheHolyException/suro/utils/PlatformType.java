package de.TheHolyException.suro.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;

import de.TheHolyException.suro.CoreBungee;
import de.TheHolyException.suro.CoreSpigot;
import de.TheHolyException.suro.managers.MySQLManager;
import de.TheHolyException.suro.managers.PluginMessagingManagerBungeeCord;
import de.TheHolyException.suro.managers.PluginMessagingManagerSpigot;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;

public enum PlatformType {
	
	SPIGOT_1_14,
    BUNGEECORD;
	
	public static PlatformType currentType;
	public static File getDataPath() {
		if (currentType.equals(BUNGEECORD)) {
			return CoreBungee.getInstance().getDataFolder();
		} else {
			return CoreSpigot.getInstance().getDataFolder();
		}
	}
	
	public static boolean isPlayerOnline(UUID uuid) {
		try {
			if (currentType.equals(BUNGEECORD)) {
				return ProxyServer.getInstance().getPlayer(uuid).isConnected();
			} else {
				return Bukkit.getPlayer(uuid).isOnline();
			}
		} catch (NullPointerException ex) {
			return false;
		}
	}
	
	public static MySQLManager getMySQLManager() {
		if (currentType.equals(BUNGEECORD)) {
			return CoreBungee.getInstance().getMySQLManager();
		} else {
			return CoreSpigot.getInstance().getMySQLManager();
		}
	}
	
	public static List<UUID> getOnlinePlayers() {
		List<UUID> list = new ArrayList<UUID>();
		if (currentType.equals(BUNGEECORD)) {
			BungeeCord.getInstance().getPlayers().forEach(player -> {
				list.add(player.getUniqueId());
			});
		} else {
			Bukkit.getOnlinePlayers().forEach(player -> {
				list.add(player.getUniqueId());
			});
		}
		return list;
	}
	
	public static void syncPlayer(UUID uuid) {
		if (currentType.equals(BUNGEECORD)) {
			PluginMessagingManagerBungeeCord.sendMessageFromBungeeToServers("suro:sync", uuid.toString());
		} else {
			PluginMessagingManagerSpigot.sendMessageToBungee(Bukkit.getPlayer(uuid), "suro:sync", uuid.toString());
		}
	}
	
	public static void syncServerData(String key) {
		if (currentType.equals(BUNGEECORD)) {
			PluginMessagingManagerBungeeCord.sendMessageFromBungeeToServers("suro:sync", "serverdata", key);
		} else  {
			PluginMessagingManagerSpigot.sendMessageToBungee(null, "suro:sync", "serverdata", key);
		}
	}
	
	public static String getUsername(UUID uuid) {
		if (currentType.equals(BUNGEECORD)) {
			return ProxyServer.getInstance().getPlayer(uuid).getName();
		} else {
			return Bukkit.getPlayer(uuid).getName();
		}
	}
	
}
