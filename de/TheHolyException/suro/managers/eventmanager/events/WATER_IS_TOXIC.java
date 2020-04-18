package de.TheHolyException.suro.managers.eventmanager.events;

import de.TheHolyException.suro.managers.eventmanager.SuroEvent;

public class WATER_IS_TOXIC implements SuroEvent {

	private double damagepersecond;
	private int start_time;
	private int hold_time;
	
	public WATER_IS_TOXIC(int start_time, int hold_time, double damagepersecond) {
		this.start_time = start_time;
		this.hold_time = hold_time;
		this.damagepersecond = damagepersecond;
	}
	
	@Override
	public String getName() {
		return "Wasser ist Gift";
	}

	@Override
	public String getID() {
		return "toxicWater";
	}

	@Override
	public void start() {
		
	}

	@Override
	public void stop() {
		
	}

	@Override
	public int getStartTime() {
		return start_time;
	}

	@Override
	public int getHoldTime() {
		return hold_time;
	}
	
	public double getDamagepersecond() {
		return damagepersecond;
	}

	public static boolean checkStrenght(Integer damage) {
		return (damage >= 0 && damage <= 20);
	}

	@Override
	public int getStrenght() {
		return (int) Math.round(damagepersecond);
	} 

}
