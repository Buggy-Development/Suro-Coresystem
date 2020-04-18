package de.TheHolyException.suro.listeners.spigot;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EntitySpawn implements Listener {
	
	@EventHandler
	public void onEntitySpawn(EntitySpawnEvent event) {
		if (event.getEntityType().equals(EntityType.PHANTOM)) event.setCancelled(true);
	}
	
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (event.getEntityType().equals(EntityType.PHANTOM)) event.setCancelled(true);
	}
}
