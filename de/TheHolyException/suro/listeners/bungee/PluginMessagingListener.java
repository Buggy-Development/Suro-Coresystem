package de.TheHolyException.suro.listeners.bungee;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

import de.TheHolyException.suro.CoreBungee;
import de.TheHolyException.suro.managers.PluginMessagingManagerBungeeCord;
import de.TheHolyException.suro.utils.JSONDataReader;
import de.TheHolyException.suro.utils.JSONReader;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PluginMessagingListener implements Listener {
	

	@EventHandler
	public void onMessageReceive(PluginMessageEvent event) {
		if (event.getTag().equalsIgnoreCase("BungeeCord")) {
			DataInputStream stream = new DataInputStream(new ByteArrayInputStream(event.getData()));
			try {
				String channel = stream.readUTF();
				if (channel.equalsIgnoreCase("suro:sync")) {
					String data = stream.readUTF();
					if (data.equalsIgnoreCase("serverdata")) {
						JSONReader.getJSONReader(stream.readUTF()).sync();
					} else {
						if (data.startsWith("#DATASTREAM")) {
							System.out.println("[SuroDataSync] <Spigot> " + data);
							String[] raw = data.replace("#DATASTREAM#", "").split("#");
							if (raw[0].equalsIgnoreCase("SUROENGINE")) {
								if (raw[1].equalsIgnoreCase("SYNC")) {
									CoreBungee.getInstance().getSuroEngine().syncData();
									PluginMessagingManagerBungeeCord.sendMessageFromBungeeToServers("suro:sync", "#DATASTREAM#SUROENGINE#SYNC");
								}
								if (raw[1].equalsIgnoreCase("STARTPREPAIRTIME")) {
									PluginMessagingManagerBungeeCord.sendMessageFromBungeeToServer("suro", "suro:sync", "#DATASTREAM#SUROENGINE#STARTPREPAIRTIME");
								}
							}
						} else {
							syncdata(UUID.fromString(data));
						}
					}
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static void syncdata(UUID uuid) {
		JSONDataReader.getDataReader(uuid).sync();
	}
}
