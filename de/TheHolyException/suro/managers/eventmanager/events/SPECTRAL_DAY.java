package de.TheHolyException.suro.managers.eventmanager.events;

import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.TheHolyException.suro.managers.eventmanager.SuroEvent;

public class SPECTRAL_DAY implements SuroEvent {
	
	private int start_time;
	private int hold_time;
	
	public SPECTRAL_DAY(int start_time, int hold_time) {
		this.start_time = start_time;
		this.hold_time = hold_time;
	}
	
	@Override
	public String getName() {
		return "Spectral Day";
	}
	
	@Override
	public String getID() {
		return "spectralDay";
	}
	
	@Override
	public void start() {
		Bukkit.getOnlinePlayers().forEach(player -> player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 0)));
	}

	@Override
	public void stop() {
		Bukkit.getOnlinePlayers().forEach(player -> player.removePotionEffect(PotionEffectType.GLOWING));
	}

	@Override
	public int getStartTime() {
		return start_time;
	}

	@Override
	public int getHoldTime() {
		return hold_time;
	}

	@Override
	public int getStrenght() {
		return -1;
	}

}
