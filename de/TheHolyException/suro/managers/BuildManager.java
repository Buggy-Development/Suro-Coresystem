package de.TheHolyException.suro.managers;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import de.TheHolyException.suro.CoreSpigot;

@SuppressWarnings("deprecation")
public class BuildManager implements Listener {
	
	public BuildManager(Boolean registerlisteners) {
		if (registerlisteners) CoreSpigot.getInstance().getServer().getPluginManager().registerEvents(this, CoreSpigot.getInstance());
	}

	private Set<Player> canBuild = new HashSet<Player>();
	
	public boolean addPlayer(Player player) {
		if (!canBuild.contains(player)) {
			canBuild.add(player);
			return true;
		}
		return false;
	}
	
	public boolean removePlayer(Player player) {
		if (canBuild.contains(player)) {
			canBuild.remove(player);
			return true;
		}
		return false;
	}
	
	public boolean containsPlayer(Player player) {
		return canBuild.contains(player);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (!containsPlayer(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (!containsPlayer(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			if (!containsPlayer((Player)event.getDamager())) {
				event.setCancelled(true);
			}
		}
	}	
	
	@EventHandler
	public void onVehicleDestroy(VehicleDestroyEvent event) {
		if (event.getAttacker() instanceof Player) {
			if (!containsPlayer((Player)event.getAttacker())) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onHangingBreak(HangingBreakByEntityEvent event) {
		if (event.getRemover() instanceof Player) {
			if (!containsPlayer((Player)event.getRemover())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event) {
		if (event.toWeatherState()) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onThunderChange(ThunderChangeEvent event) {
		if (event.toThunderState()) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onItemDropp(PlayerDropItemEvent event) {
		if (!containsPlayer(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event) {
		if (!containsPlayer(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
		if (!containsPlayer(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		if (!containsPlayer(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerOpenInventory(InventoryOpenEvent event) {
		if (!containsPlayer((Player)event.getPlayer())) {
			event.setCancelled(true);
		}
	}
}
