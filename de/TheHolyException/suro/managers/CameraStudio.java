package de.TheHolyException.suro.managers;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import de.TheHolyException.suro.CoreSpigot;

public class CameraStudio implements Listener {
	static HashSet<UUID> travelling = new HashSet<UUID>();
	static HashSet<UUID> stopping = new HashSet<UUID>();

	public CameraStudio() {
		
	}

	public static double round(double unrounded, int precision) {
		BigDecimal bd = new BigDecimal(unrounded);
		BigDecimal rounded = bd.setScale(precision, 4);
		return rounded.doubleValue();
	}
	
	public static void travel(final Player player, List<Location> locations, int time, String FailMessage,
			final String CompletedMessage) {
		List<Double> diffs = new ArrayList<Double>();
		List<Integer> travelTimes = new ArrayList<Integer>();

		double totalDiff = 0.0D;

		for (int i = 0; i < locations.size() - 1; i++) {
			Location s = (Location) locations.get(i);
			Location n = (Location) locations.get(i + 1);
			double diff = CameraStudio.positionDifference(s, n);
			totalDiff += diff;
			diffs.add(Double.valueOf(diff));
		}

		for (Iterator<Double> n = diffs.iterator(); n.hasNext();) {
			double d = ((Double) n.next()).doubleValue();
			travelTimes.add(Integer.valueOf((int) (d / totalDiff * time)));
		}

		final List<Location> tps = new ArrayList<Location>();

		World w = player.getWorld();

		for (int i = 0; i < locations.size() - 1; i++) {
			Location s = (Location) locations.get(i);
			Location n = (Location) locations.get(i + 1);
			int t = ((Integer) travelTimes.get(i)).intValue();

			double moveX = n.getX() - s.getX();
			double moveY = n.getY() - s.getY();
			double moveZ = n.getZ() - s.getZ();
			double movePitch = n.getPitch() - s.getPitch();

			double yawDiff = Math.abs(n.getYaw() - s.getYaw());
			double c = 0.0D;

			if (yawDiff <= 180.0D) {
				if (s.getYaw() < n.getYaw()) {
					c = yawDiff;
				} else {
					c = -yawDiff;
				}
			} else if (s.getYaw() < n.getYaw()) {
				c = -(360.0D - yawDiff);
			} else {
				c = 360.0D - yawDiff;
			}

			double d = c / t;

			for (int x = 0; x < t; x++) {
				Location l = new Location(w, s.getX() + moveX / t * x, s.getY() + moveY / t * x,
						s.getZ() + moveZ / t * x, (float) (s.getYaw() + d * x),
						(float) (s.getPitch() + movePitch / t * x));
				tps.add(l);
			}

		}

		try {
			player.setAllowFlight(true);
			player.teleport((Location) tps.get(0));
			player.setFlying(true);
			travelling.add(player.getUniqueId());
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(CoreSpigot.getInstance(), new Runnable() {
				private int ticks = 0;

				public void run() {
					if (this.ticks < tps.size()) {

						player.teleport((Location) tps.get(this.ticks));

						if (!stopping.contains(player.getUniqueId())) {
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(CoreSpigot.getInstance(), this, 1L);
						} else {
							stopping.remove(player.getUniqueId());
							travelling.remove(player.getUniqueId());
						}

						this.ticks += 1;
					} else {
						travelling.remove(player.getUniqueId());
						if (CompletedMessage != null)
							player.sendMessage(CompletedMessage);
					}
				}
			}, 3L);
		} catch (Exception e) {
			if (FailMessage != null)
				player.sendMessage(FailMessage);
		}
	}

	public static int parseTimeString(String timeString) throws java.text.ParseException {
		Date length = null;
		/*
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("mm'm'ss's'");
			length = formatter.parse(timeString);
		} catch (Exception e) {
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("m'm'ss's'");
				length = formatter.parse(timeString);
			} catch (Exception e1) {
				try {
					SimpleDateFormat formatter = new SimpleDateFormat("m'm's's'");
					length = formatter.parse(timeString);
				} catch (Exception e2) {
					try {
						SimpleDateFormat formatter = new SimpleDateFormat("mm'm's's'");
						length = formatter.parse(timeString);
					} catch (Exception e3) {
						try {
							SimpleDateFormat formatter = new SimpleDateFormat("mm'm'");
							length = formatter.parse(timeString);
						} catch (Exception e4) {
							try {
								SimpleDateFormat formatter = new SimpleDateFormat("m'm'");
								length = formatter.parse(timeString);
							} catch (Exception e5) {
								try {
									SimpleDateFormat formatter = new SimpleDateFormat("s's'");
									length = formatter.parse(timeString);
								} catch (Exception e6) {
									SimpleDateFormat formatter = new SimpleDateFormat("ss's'");
									length = formatter.parse(timeString);
								}
							}
						}
					}
				}
			}
		}
		*/
		String[] formats = new String[] {"mm'm'ss's'", "m'm'ss's'", "m'm's's'", "mm'm's's'", "mm'm'", "m'm'", "s's'", "ss's'"};
		//BIN GLEICH DA

		for(String x : formats) if( (length = tryCastTime(x, timeString)) != null) break;
		
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(length);

		int time = (cal.get(12) * 60 + cal.get(13)) * 20;

		return time;
	}
	
	private static Date tryCastTime(String a, String b) {
		try {
			return new SimpleDateFormat(a).parse(b);
		} catch (Exception e) {
			return null;
		}
	}

	public static double positionDifference(Location cLoc, Location eLoc) {
		double cX = cLoc.getX();
		double cY = cLoc.getY();
		double cZ = cLoc.getZ();

		double eX = eLoc.getX();
		double eY = eLoc.getY();
		double eZ = eLoc.getZ();

		double dX = eX - cX;
		if (dX < 0.0D) {
			dX = -dX;
		}
		double dZ = eZ - cZ;
		if (dZ < 0.0D) {
			dZ = -dZ;
		}
		double dXZ = Math.hypot(dX, dZ);

		double dY = eY - cY;
		if (dY < 0.0D) {
			dY = -dY;
		}
		double dXYZ = Math.hypot(dXZ, dY);

		return dXYZ;
	}

	public static boolean isTravelling(UUID PlayerUUID) {
		if (travelling.contains(PlayerUUID))
			return true;
		return false;
	}

	public static void stop(final UUID playerUUID) {
		stopping.add(playerUUID);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(CoreSpigot.getInstance(), new Runnable() {
			public void run() {
				stopping.remove(playerUUID);
			}
		}, 2L);
	}

}