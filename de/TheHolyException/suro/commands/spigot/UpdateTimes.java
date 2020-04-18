package de.TheHolyException.suro.commands.spigot;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.TheHolyException.suro.CoreSpigot;

public class UpdateTimes implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
		
		Player player = (Player) sender;
		if (!player.hasPermission("system.*")) return true;
		
		CoreSpigot.getInstance().getTimeManager().updateTimes();
		
		return true;
	}

}
