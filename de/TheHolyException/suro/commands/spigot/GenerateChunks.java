package de.TheHolyException.suro.commands.spigot;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.mojang.datafixers.util.Pair;

import de.TheHolyException.suro.CoreSpigot;

public class GenerateChunks implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
		
		Player player = (Player) sender;
		if (!player.hasPermission("system.*")) return true;
		
		if (args.length == 4) {
			int x1 = Integer.valueOf(args[0])/16;
			int z1 = Integer.valueOf(args[1])/16;
			
			int x2 = Integer.valueOf(args[2])/16;
			int z2 = Integer.valueOf(args[3])/16;
			
			int lowestX = Math.min(x1, x2);
			int lowestZ = Math.min(z1, z2);
			
			int highestX = lowestX == x1 ? x2 : x1;
			int highestZ = lowestZ == z1 ? z2 : z1;
			
			
			ArrayList<Pair<Integer,Integer>> chunks = new ArrayList<>();
			
			for (int x = lowestX; x <= highestX; x ++) {
				for (int z = lowestZ; z <= highestZ; z ++) {
					chunks.add(Pair.of(x*16, z*16));
				}
			}
			
			new BukkitRunnable() {

				int generated = 0;
				@Override
				public void run() {
					if (generated >= chunks.size()) {
						Bukkit.getConsoleSender().sendMessage("Generated Chunks: " + generated);
						this.cancel();
					} else {
						Pair<Integer,Integer> pair = chunks.get(generated);
						int a = pair.getFirst(); int b = pair.getSecond();
						player.sendMessage("Generating Chunk X:" + a + " Y:" + b);
						System.out.println("Generating Chunk X:" + a + " Y:" + b);
						Chunk chunk = new Location(player.getWorld(),a,0,b).getChunk();
						chunk.load();
						chunk.unload();
						generated ++;
					}
				}
			}.runTaskTimer(CoreSpigot.getInstance(), 1, 1);
			
			
		} else player.sendMessage("Use /generatechunks <Pos1X> <Pos1Z> <Pos2X> <Pos2Z>");
		
		return true;
	}

	
}
