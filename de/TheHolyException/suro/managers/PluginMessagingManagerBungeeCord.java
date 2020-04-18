package de.TheHolyException.suro.managers;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.md_5.bungee.api.ProxyServer;

public class PluginMessagingManagerBungeeCord {
	
	public static void sendMessageFromBungeeToServers(String channel, String... data) {
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
		
		ProxyServer.getInstance().getServers().values().forEach(server -> {
			System.out.println("[SuroDataSync] <BungeeCord> -> <Spigot> " + stream.toString());
			server.sendData("BungeeCord", stream.toByteArray());
		});
	}
	
	public static void sendMessageFromBungeeToServer(String server, String channel, String... data) {
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

		System.out.println("[SuroDataSync] <BungeeCord> -> <"+server+"> " + stream.toString());
		ProxyServer.getInstance().getServerInfo(server).sendData("BungeeCord", stream.toByteArray());
	}

}
