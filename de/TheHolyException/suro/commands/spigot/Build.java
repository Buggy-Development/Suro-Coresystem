package de.TheHolyException.suro.commands.spigot;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.TheHolyException.suro.CoreSpigot;
import de.TheHolyException.suro.managers.BuildManager;
import de.TheHolyException.suro.utils.Messages;

public class Build implements CommandExecutor {

	private BuildManager bm = CoreSpigot.getInstance().getBuildmanager();
	
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
		
		Player player = (Player) sender;
		if (!player.hasPermission("system.build")) return true;
		
		if (bm == null) bm = CoreSpigot.getInstance().getBuildmanager();
		
		if (args.length == 0) {
			
			if (bm.containsPlayer(player)) {
				bm.removePlayer(player);
				Messages.send(player, "Du bist nicht länger im Builder modus");
				player.setGameMode(GameMode.ADVENTURE);
			} else {
				bm.addPlayer(player);
				Messages.send(player, "Du bist nun im Builder modus");
				player.setGameMode(GameMode.CREATIVE);
			}
			
		} else if (args.length == 1 && player.hasPermission("system.build.others")) {
			Player target = Bukkit.getPlayer(args[0]);
			if (!target.isOnline()) {
				Messages.send(player, "Dieser Spieler ist nicht Online");
				return true;
			}
			
			if (bm.containsPlayer(target)) {
				bm.removePlayer(target);
				Messages.send(player, "Du hast §a" + target.getName() + " §baus dem Builder modus entfernt");
				Messages.send(target, "Du bist nicht länger im Builder modus");
				target.setGameMode(GameMode.ADVENTURE);
			} else {
				bm.addPlayer(target);
				Messages.send(player, "Du hast §a" + target.getName() + " §bin den Builder modus gesetzt");
				Messages.send(target, "Du bist nun im Builder modus");
				target.setGameMode(GameMode.CREATIVE);
			}
		}
		
		return true;
	}

}
