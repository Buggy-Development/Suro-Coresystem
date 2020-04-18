package de.TheHolyException.suro.utils;

import org.bukkit.scheduler.BukkitRunnable;

import de.TheHolyException.suro.CoreSpigot;

public class MSPT {
	
	private int UPDATE_RATE = 10;
	
	private long cm = 0;
	private double mspt;
	
	public MSPT() {
		cm = System.currentTimeMillis();
		new BukkitRunnable() {
			@Override
			public void run() {
				mspt = (double)(System.currentTimeMillis() - cm) / UPDATE_RATE;
				cm = System.currentTimeMillis();
			}
		}.runTaskTimer(CoreSpigot.getInstance(), UPDATE_RATE, UPDATE_RATE);
	}
	
	public double getMSPT() {
		return mspt;
	}

}
