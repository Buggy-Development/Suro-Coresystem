package de.TheHolyException.suro.listeners.spigot;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import de.TheHolyException.suro.CoreSpigot;
import de.TheHolyException.suro.utils.JSONDataReader;

public class EntityDeath implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof Zombie) {
			Zombie zombie = (Zombie) event.getEntity();
			if (zombie.getCustomName() != null) {
				Player killer = event.getEntity().getKiller();
				event.setDroppedExp(0);
				event.getDrops().clear();
				String killedplayername = event.getEntity().getCustomName();
				JSONDataReader reader = JSONDataReader.getDataReader(CoreSpigot.getInstance().getMySQLManager().getPlayerDataManager().getPlayerbyName(killedplayername));
				reader.set("death", true);
				OfflinePlayer player = Bukkit.getOfflinePlayer(killedplayername);
				CoreSpigot.getInstance().getZombieManager().getZombiedropManager().dropInventory(player, zombie.getLocation());
				PlayerDeath.onDeath(player, zombie.getLocation());
				Bukkit.broadcastMessage("§4§l✟ §c§l" + player.getName() + "§7(§4§l☠§7) §7§l⚔ §c§l" + killer.getName());
				CoreSpigot.getInstance().getSuroEngine().checkWinner();
			}
		}
	}
}