package de.TheHolyException.suro.managers.eventmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import de.TheHolyException.suro.CoreSpigot;
import de.TheHolyException.suro.managers.eventmanager.events.WATER_IS_TOXIC;

public class EventManager {

	private Map<SuroEvent, Integer> eventtasks = new HashMap<>();
	private Map<SuroEvent, Integer> starttasks = new HashMap<>();
	
	public EventManager() {
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				
				for (Iterator<SuroEvent> iterator = starttasks.keySet().iterator(); iterator.hasNext();) {
					SuroEvent key = iterator.next();
					int value = starttasks.get(key);
					if (value <= 1) {
						starttasks.remove(key);
						key.start();
						eventtasks.put(key, key.getHoldTime());
						Bukkit.getOnlinePlayers().forEach(all -> {
							all.sendTitle("§3Das §b"+key.getName()+" §3Event", "§3ist nun gestartet!", 10, 30, 20);
							all.playSound(all.getEyeLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 10, 1.2f);
						});
						continue;
					}
					starttasks.put(key, (value-1));
				}
				
				for (Iterator<SuroEvent> iterator = eventtasks.keySet().iterator(); iterator.hasNext();) {
					SuroEvent key = iterator.next();
					int value = eventtasks.get(key);
					if (value <= 1) {
						Bukkit.getOnlinePlayers().forEach(all -> {
							all.sendTitle("§3Das §b"+key.getName()+" §3Event", "§3ist nun beendet!", 10, 30, 20);
							all.playSound(all.getEyeLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 10, 1.2f);
						});
						eventtasks.remove(key);
						key.stop();
						continue;
					}
					eventtasks.put(key, (value-1));
				}
				if (runningEvents().contains("toxicWater")) {
					Bukkit.getOnlinePlayers().forEach(player -> {
						if (player.getLocation().getBlock().getType().equals(Material.WATER) || player.getLocation().clone().add(0,1,0).getBlock().getType().equals(Material.WATER)) {
							WATER_IS_TOXIC sev = (WATER_IS_TOXIC) CoreSpigot.getInstance().getEventManager().getEvent("toxicWater");
							System.out.println((sev == null));
							player.damage((sev.getDamagepersecond() <= 0) ? 0.5 : sev.getDamagepersecond()/2);
							player.sendTitle("§4§l/!\\ §cAchtung Wasser ist Giftig §4§l/!\\", "§cVerlasse schnellstmöglich das Wasser", 6, 12, 6);
						}
					});
				}				
			}
		}.runTaskTimer(CoreSpigot.getInstance(), 20, 20);
	}
	
	public void startNewEvent(SuroEvent event) {
		starttasks.put(event, event.getStartTime());
	}
	
	public Map<SuroEvent, Integer> getEventTasks() {
		return eventtasks;
	}
	
	public Map<SuroEvent, Integer> getPrepairEventtasks() {
		return starttasks;
	}
	
	public List<String> runningEvents() {
		List<String> events = new ArrayList<String>();
		eventtasks.keySet().forEach(key -> {
			events.add(key.getID());
		});
		return events;
	}
	
	public SuroEvent getEvent(String eventid) {
		if (runningEvents().contains(eventid)) {
			for (SuroEvent ev : eventtasks.keySet()) {
				if (ev.getID().equalsIgnoreCase(eventid)) return ev;
			}
			for (SuroEvent ev : starttasks.keySet()) {
				if (ev.getID().equalsIgnoreCase(eventid)) return ev;
			}
		}
		return null;
	}

}
