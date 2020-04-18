package de.TheHolyException.suro.managers;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.TheHolyException.suro.CoreSpigot;

public class PluginMessagingManagerSpigot {
	
	public static HashSet<String> dataToSync = new HashSet<String>();
	
	public static void sendMessageToBungee(Player player, String channel, String... data) {
		if (player == null) {
			try {
				player = (Player) Bukkit.getOnlinePlayers().toArray()[0];
			} catch (ArrayIndexOutOfBoundsException ex) {
				System.out.println("Failed to sync ServerDate, no Players are Online");
				dataToSync.add(data[1]);
				return;
			}
		}
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(stream);
		
		try {
			output.writeUTF(channel);
			for (String msg : data) {
				output.writeUTF(msg);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("[SuroDataSync] <Spigot> -> <BungeeCord> " + stream.toString());
		player.sendPluginMessage(CoreSpigot.getInstance(), "BungeeCord", stream.toByteArray());
	}
}
