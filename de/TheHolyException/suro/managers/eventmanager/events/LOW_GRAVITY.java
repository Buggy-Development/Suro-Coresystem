package de.TheHolyException.suro.managers.eventmanager.events;

import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.TheHolyException.suro.managers.eventmanager.SuroEvent;

public class LOW_GRAVITY implements SuroEvent {

	private int start_time;
	private int hold_time;
	private int strenght;
	
	public LOW_GRAVITY(int start_time, int hold_time, int strength) {
		this.start_time = start_time;
		this.hold_time = hold_time;
		this.strenght = strength;
	}
	
	@Override
	public String getName() {
		return "Geringe Schwerkraft";
	}
	
	@Override
	public String getID() {
		return "lowGravity";
	}
	
	@Override
	public void start() {
		Bukkit.getOnlinePlayers().forEach(player -> {
			player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, Integer.MAX_VALUE, (251+strenght)));
		});
	}

	@Override
	public void stop() {
		Bukkit.getOnlinePlayers().forEach(player -> {
			player.removePotionEffect(PotionEffectType.LEVITATION);
		});
	}

	@Override
	public int getStartTime() {
		return start_time;
	}

	@Override
	public int getHoldTime() {
		return hold_time;
	}
	
	public static boolean checkStrenght(int strenght) {
		return (strenght >= 0 && strenght < 4);
	}

	@Override
	public int getStrenght() {
		return strenght;
	}
	

}
