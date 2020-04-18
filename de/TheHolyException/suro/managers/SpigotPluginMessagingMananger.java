package de.TheHolyException.suro.managers;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.bukkit.entity.Player;

import de.TheHolyException.suro.CoreSpigot;

public class SpigotPluginMessagingMananger {
	
	public SpigotPluginMessagingMananger() {
	}
	
	public void sendMessageToBungee(Player player, String channel, String... datas) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(stream);
		
		try {
			output.writeUTF(channel);
			for (String data : datas) {
				output.writeUTF(data);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		player.sendPluginMessage(CoreSpigot.getInstance(), "BungeeCord", stream.toByteArray());
	}

}
