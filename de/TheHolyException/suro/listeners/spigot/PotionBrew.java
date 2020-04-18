package de.TheHolyException.suro.listeners.spigot;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;

public class PotionBrew implements Listener {
	
	@EventHandler
	public void onPotionBrew(PrepareItemCraftEvent event) {
		System.out.println(event.getRecipe().getResult().getType().toString());
	}

}
