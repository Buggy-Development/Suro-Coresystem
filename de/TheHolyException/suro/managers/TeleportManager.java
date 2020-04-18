package de.TheHolyException.suro.managers;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import de.TheHolyException.suro.CoreSpigot;
import de.TheHolyException.suro.listeners.spigot.EntityDamageEntity;

public class TeleportManager {
	
	private int rndX;
	private int rndZ;
	private Random rnd;
	
	public TeleportManager(int randomeX, int randomeZ) {
		rndX = randomeX;
		rndZ = randomeZ;
		rnd = new Random();
	}
	
	public void TeleportRandom(Player player, World world) {
		for (int i = 0; i < 8; i ++) {
			Location loc = new Location(world, (rnd.nextInt(rndX)-rndX/2),240,(rnd.nextInt(rndZ)-rndZ/2),0,0);
			if (loc.getBlock().getBiome().toString().contains("OCEAN")) continue;
			Block hblock = world.getHighestBlockAt(loc);
			if (hblock.getType().equals(Material.LAVA)) continue;
			if (!loc.getChunk().isLoaded()) loc.getChunk().load();
			player.teleport(loc.add(0.5,0,0.5));
			EntityDamageEntity.saveThisDude(player);
			CoreSpigot.getInstance().getSuroEngine().buildHive(loc);
			break;
		}
	}

}
