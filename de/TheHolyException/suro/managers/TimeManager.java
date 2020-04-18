package de.TheHolyException.suro.managers;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import de.TheHolyException.suro.CoreSpigot;
import de.TheHolyException.suro.utils.JSONReader;
import de.TheHolyException.suro.utils.PlatformType;

public class TimeManager {
	
	private Date date;
	private Date start;
	private Date stop;
	
	public TimeManager() {
		updateTimes();
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if (PlatformType.currentType.equals(PlatformType.SPIGOT_1_14)) {
					if (Bukkit.getOnlinePlayers().size() != 0) {
						if (!PluginMessagingManagerSpigot.dataToSync.isEmpty()) {
							System.out.println("Snycing data now...");
							PluginMessagingManagerSpigot.dataToSync.forEach(data -> {
								PlatformType.syncServerData(data);
							});
							System.out.println("Snycing data done!");
						}
					}
				}
				date = new Date();
				if (!canJoinSuro() && !Bukkit.getOnlinePlayers().isEmpty() && !CoreSpigot.lobby) {
//					Bukkit.getOnlinePlayers().forEach(player -> {
//						if (!player.hasPermission("system.gamemaster")) {
//							player.kickPlayer(Messages.prefix + "§cDie Zeit ist zu ende.");
//						}
//					});
				}
			}
		}.runTaskTimer(CoreSpigot.getInstance(), 20, 20);
	}

	
	/*
	 * 
	 * curr 15:36 -> zahl
	 * 
	 * start 15:38 -> zahl2
	 * 
	 * zahl >= zahl2
	 */
	
	public void updateTimes() {
		JSONReader reader = JSONReader.getJSONReader("settings");
		String[] a = reader.getString("starttime", "18:00").split(":");
		String[] b = reader.getString("stoptime", "19:00").split(":");
		Calendar c_start = new GregorianCalendar();
		c_start.setTime(new Date());
		c_start.set(Calendar.HOUR_OF_DAY, Integer.valueOf(a[0]));
		c_start.set(Calendar.MINUTE, Integer.valueOf(a[1]));
		if (c_start.getTimeInMillis() < System.currentTimeMillis()) {
			System.out.println("Starttime skiped to next day");
			c_start.add(Calendar.DATE, 1);
		}
		start = c_start.getTime();
		Calendar c_stop = new GregorianCalendar();
		c_stop.setTime(new Date());
		c_stop.set(Calendar.HOUR_OF_DAY, Integer.valueOf(b[0]));
		c_stop.set(Calendar.MINUTE, Integer.valueOf(b[1]));
		stop = c_stop.getTime();
 	}
	
	public String getRemainingTime() {
		long diff = start.getTime()-date.getTime();
		if(diff < 0) {
			updateTimes();
		}
		
		if (date.getTime() > start.getTime()) {
			diff = stop.getTime()-date.getTime();
		} else {
			if (date.getTime() > stop.getTime()) {
				Calendar c = GregorianCalendar.getInstance();
				c.setTime(start);
				c.add(Calendar.DATE, 1);
				start = c.getTime();
			}
		}
		long diffSeconds = diff /1000%60;
		long diffMinutes = diff /(60*1000)%60;
		long diffHours = diff /(60*60*1000)%24;
//		if(diffSeconds < 0) diffSeconds += 60;
//		if(diffMinutes < 0) diffMinutes += 60;
//		if(diffHours < 0) diffHours += 24;
		return fastFormat(diffHours) + ":" + fastFormat(diffMinutes) + ":" + fastFormat(diffSeconds);
	}
	
	public String fastFormat(long i) {
		return (char)('0' + ((i / 10) % 10)) + "" + (char)('0' + (i % 10));
	}
	
	public String getStartTimeString() {
		Calendar c = new GregorianCalendar();
		c.setTime(start);
		return String.format("%02d",c.get(Calendar.HOUR_OF_DAY))+":"+String.format("%02d", c.get(Calendar.MINUTE));
	}
	
	public String getStopTimeString() {
		Calendar c = new GregorianCalendar();
		c.setTime(stop);
		return String.format("%02d",c.get(Calendar.HOUR_OF_DAY))+":"+String.format("%02d", c.get(Calendar.MINUTE));
	}
	
	public boolean canJoinSuro() {
		return (date.getTime() > start.getTime() && date.getTime() < stop.getTime());
	}
}
