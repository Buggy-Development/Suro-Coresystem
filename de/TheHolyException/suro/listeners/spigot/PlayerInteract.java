package de.TheHolyException.suro.listeners.spigot;

import java.util.ConcurrentModificationException;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

import de.TheHolyException.suro.CoreSpigot;
import de.TheHolyException.suro.managers.TokenManager;
import de.TheHolyException.suro.utils.Inventorys;
import de.TheHolyException.suro.utils.JSONDataReader;
import de.TheHolyException.suro.utils.JSONReader;
import de.TheHolyException.suro.utils.Messages;

public class PlayerInteract implements Listener {
	
	public static HashMap<Player, Integer> cooldown = new HashMap<Player, Integer>();
	
	public PlayerInteract() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if (!cooldown.isEmpty()) {
					try {
						cooldown.keySet().forEach(player -> {
							int i = cooldown.get(player);
							if (i <= 0) {
								cooldown.remove(player);
							} else {
								cooldown.put(player, (i -1));
							}
						});
					} catch (ConcurrentModificationException ex) {}
				}
			}
		}.runTaskTimerAsynchronously(CoreSpigot.getInstance(), 10, 10);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (CoreSpigot.lobby) {
			if (!CoreSpigot.getInstance().getBuildmanager().containsPlayer(event.getPlayer())) {
				event.setCancelled(true);
			}
			if (!cooldown.containsKey(event.getPlayer())) {
				cooldown.put(event.getPlayer(), 1);
				if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
					if (event.getClickedBlock().getType().equals(Material.END_GATEWAY)) {
						Player player = event.getPlayer();
						JSONDataReader reader = JSONDataReader.getDataReader(player.getUniqueId());
						if (JSONReader.getJSONReader("settings").getBoolean("portal", false)) {
							if (!reader.getBoolean("gamemaster", false)) {
								if (!CoreSpigot.getInstance().getTimeManager().canJoinSuro()) {
									if (reader.getBoolean("death", false)) {
										Messages.send(player, "Du bist Gestorben, und kannst daher nicht weiterspielen :/");
										return;
									}
									String token = TokenManager.getPlayersToken(player.getUniqueId());
									if (token == null) {
										Messages.send(player, "Du hast keinen Gültigen Token registriert, falls du einen besitzt löse ihn ein mit §e/reedtoken <Token>");
										return;
									}
									JSONReader tokenreader = new JSONReader("tokens");
									JSONObject jsontoken = (JSONObject) tokenreader.getJSONData().get(token);
									if (jsontoken.containsKey("banned") && Boolean.valueOf(jsontoken.get("banned").toString())) {
										Messages.send(player, "Dein Token ist ungültig / gesperrt, bitte wende dich an einen Gamemaster");
										return;
									}
								} else {
									Messages.send(event.getPlayer(), "Du kannst SuRo noch nicht Betreten");
									return;
								}
							}
							Messages.send(event.getPlayer(), "Verbindung zum Suro server wird hergestellt...");
							CoreSpigot.getInstance().getPluginMessagingManager().sendMessageToBungee(event.getPlayer(), "Connect", "suro");
							return;
						} else Messages.send(event.getPlayer(), "Das Portal ist Deaktiviert!");
					}
				}
			}
		} else {
			if (event.getAction() != Action.PHYSICAL) {
				Player player = event.getPlayer();
				ItemStack item = player.getItemInHand();
				if (item == null) return;
				if (item.getType().equals(Material.COMPASS) && item.hasItemMeta() && item.getItemMeta().getDisplayName().equalsIgnoreCase("§6Zuschauen")) {
					Inventorys.spectator.open(player);
				}
			}
		}
	}

}
