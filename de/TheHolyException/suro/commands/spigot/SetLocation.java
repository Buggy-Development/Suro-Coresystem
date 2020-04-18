package de.TheHolyException.suro.commands.spigot;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.TheHolyException.suro.utils.JSONReader;
import de.TheHolyException.suro.utils.Messages;

public class SetLocation implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
		
		Player player = (Player) sender;
		
		if (!player.hasPermission("system.*")) return true;
		if (args.length == 0) {
			
		} else if (args.length == 1) {
			JSONReader reader = JSONReader.getJSONReader("locations");
			Location loc = player.getLocation();
			reader.set(args[0], loc.getWorld().getName() + "#" + loc.getX() + "#" + loc.getY() + "#" + loc.getZ() + "#" + loc.getYaw() + "#" + loc.getPitch());
			Messages.send(player, "Location §a" + args[0] + "§b wurde gesetzt");
		}
		
		return true;
	}
	
	@Deprecated
	public static Location getLocation(String path) {
		JSONReader reader = JSONReader.getJSONReader("locations");
		String[] data = reader.getString(path, null).split("#");
		return new Location(Bukkit.getWorld(data[0]), Double.valueOf(data[1]), Double.valueOf(data[2]), Double.valueOf(data[3]), Float.valueOf(data[4]), Float.valueOf(data[5]));
	}

}
