package de.TheHolyException.suro.managers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import de.TheHolyException.suro.CoreSpigot;
import de.TheHolyException.suro.utils.JSONReader;

public class LocationManager {
	
	private static Map<String, LocationManager> cache = new HashMap<String, LocationManager>();
	private static MySQLManager mysql;
	private JSONReader data;
	
	private LocationManager(String datapath) {
		if (mysql == null) mysql = CoreSpigot.getInstance().getMySQLManager();
		data = JSONReader.getJSONReader(datapath);
	}
	
	public static LocationManager getLocationManager(String datapath) {
		if (cache.containsKey(datapath)) {
			return cache.get(datapath);
		}
		LocationManager mngr = new LocationManager(datapath);
		cache.put(datapath, mngr);
		return mngr;
	}
	
	public Location getLocation(String name) {
		String[] raw = data.getString(name, null).split("#");
		return new Location(Bukkit.getWorld(raw[0]), Double.valueOf(raw[1]), Double.valueOf(raw[2]), Double.valueOf(raw[3]), Float.valueOf(raw[4]), Float.valueOf(raw[5]));
	}
	
	public void setLocation(String name, Location location) {
		data.set(name, location.getWorld().getName()+"#"+location.getX()+"#"+location.getY()+"#"+location.getZ()+"#"+location.getYaw()+"#"+location.getPitch());
	}
	
	public static String getLocationAsString(Location location) {
		return location.getWorld().getName()+"#"+location.getX()+"#"+location.getY()+"#"+location.getZ()+"#"+location.getYaw()+"#"+location.getPitch();
	}
	
	public static Location getStringAsLocation(String data) {
		String[] raw = data.split("#");
		return new Location(Bukkit.getWorld(raw[0]), Double.valueOf(raw[1]), Double.valueOf(raw[2]), Double.valueOf(raw[3]), Float.valueOf(raw[4]), Float.valueOf(raw[5]));
	}

}
