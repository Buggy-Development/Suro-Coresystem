package de.TheHolyException.suro.commands.spigot;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.TheHolyException.suro.CoreSpigot;
import de.TheHolyException.suro.managers.PluginMessagingManagerSpigot;

public class StartIntro implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
		
		Player player = (Player) sender;
		
		if (!player.hasPermission("system.startintro")) return true;
		
		if (args.length == 0) {
			CoreSpigot.getInstance().getSuroEngine().startIntro();
		} else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("fake")) {
				PluginMessagingManagerSpigot.sendMessageToBungee(null, "suro:sync", "#DATASTREAM#SUROENGINE#SYNC");
				PluginMessagingManagerSpigot.sendMessageToBungee(null, "suro:sync", "#DATASTREAM#SUROENGINE#STARTPREPAIRTIME");
				Bukkit.getScheduler().scheduleSyncDelayedTask(CoreSpigot.getInstance(), () -> Bukkit.getOnlinePlayers().forEach(all -> CoreSpigot.getInstance().getPluginMessagingManager().sendMessageToBungee(all, "Connect", "suro")),30);
			}
		}
		
		return true;
	}

}
