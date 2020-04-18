package de.TheHolyException.suro.listeners.spigot;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import de.TheHolyException.suro.CoreSpigot;
import de.TheHolyException.suro.managers.LocationManager;
import de.TheHolyException.suro.managers.TokenManager;
import de.TheHolyException.suro.utils.JSONDataReader;
import de.TheHolyException.suro.utils.JSONReader;

public class PluginMessagingListener implements PluginMessageListener {

//CoreBungee.getInstance().getPluginMessagingManager().sendMessageFromBungeeToServers("suro:sync", "#DATASTREAM#REVIVE#"+player.getUniqueId());
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
		DataInputStream stream = new DataInputStream(new ByteArrayInputStream(bytes));
		if (channel.equalsIgnoreCase("BungeeCord")) {
			try {
				String pipeline = stream.readUTF();
				if (pipeline.equalsIgnoreCase("suro:sync")) {
					String data = stream.readUTF();
					System.out.println("[SuroDataSync] <BungeeCord> " + data);
					if (data.equalsIgnoreCase("serverdata")) {
						JSONReader.getJSONReader(stream.readUTF()).sync();
						CoreSpigot.getInstance().getTimeManager().updateTimes();
					} else {
						if (data.startsWith("#DATASTREAM")) {
							String[] raw = data.replace("#DATASTREAM#", "").split("#");
							if (raw[0].equalsIgnoreCase("REVIVE")) {
								UUID uuid = UUID.fromString(raw[1]);
								Player target = Bukkit.getPlayer(uuid);
								if (!CoreSpigot.getInstance().getSuroEngine().containsPlayer(target)) {
									CoreSpigot.getInstance().getSuroEngine().addPlayer(target);
								}
								if (!CoreSpigot.lobby) {
									String playername = CoreSpigot.getInstance().getMySQLManager().getPlayerDataManager().getAllUsersUUIDKey().get(uuid);
									if (!CoreSpigot.getInstance().getZombieManager().getZombiePlayers().containsKey(playername)) {
										UUID id = CoreSpigot.getInstance().getMySQLManager().getPlayerDataManager().getPlayerbyName(playername);
										if (!TokenManager.isGamemaster(id) && !TokenManager.isDeath(id)) {
											CoreSpigot.getInstance().getZombieManager().createZombie(playername, LocationManager.getStringAsLocation(JSONDataReader.getDataReader(uuid).getString("logoutloc", null)));
										}
									}
								}
							} else if (raw[0].equalsIgnoreCase("SUROENGINE")) {
								if (raw[1].equalsIgnoreCase("SYNC")) {
									CoreSpigot.getInstance().getSuroEngine().syncData();
								} else if (raw[1].equalsIgnoreCase("STARTPREPAIRTIME")) {
									if (!CoreSpigot.lobby) CoreSpigot.getInstance().getSuroEngine().startPrepairTime();
								}
							}
						} else {
							UUID uuid = UUID.fromString(data);
							JSONDataReader.getDataReader(uuid).sync();
						}
					}
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
