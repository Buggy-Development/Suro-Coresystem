package de.TheHolyException.suro.managers.eventmanager;

public interface SuroEvent {
	public String getName();
	public String getID();
	public void start();
	public void stop();
	
	public int getStrenght();
	public int getStartTime();
	public int getHoldTime();
}
