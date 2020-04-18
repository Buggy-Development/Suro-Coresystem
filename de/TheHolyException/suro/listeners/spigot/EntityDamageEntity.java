package de.TheHolyException.suro.listeners.spigot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import de.TheHolyException.suro.CoreSpigot;

public class EntityDamageEntity implements Listener {

	private static Set<Zombie> needToBeNoAI = new HashSet<Zombie>();
	private static Map<Player ,Integer> droppedPlayers = new ConcurrentHashMap<>();
	static BukkitScheduler scheduler = Bukkit.getScheduler();
	
	public EntityDamageEntity() {
		new BukkitRunnable() {
			int tack = 0;
			@Override
			public void run() {
				
				List<Zombie> remove = new ArrayList<Zombie>();
				for (Zombie z : needToBeNoAI) {
					if (z.isOnGround()) {
						remove.add(z);
						z.setAI(false);
					}
				}
				for (Zombie z : remove) needToBeNoAI.remove(z);
				remove.clear();
				
				if (tack == 4) {
					tack = 0;
					for (Player player : droppedPlayers.keySet()) {
						int value = droppedPlayers.get(player);
						if (value <= 0) {
							droppedPlayers.remove(player);
							continue;
						}
						droppedPlayers.put(player, droppedPlayers.get(player)-1);				
					}
				}
				tack ++;
			}
		}.runTaskTimer(CoreSpigot.getInstance(), 5, 5);
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			if (event.getCause().equals(DamageCause.FALL)) {
				Player player = (Player) event.getEntity();
				if (droppedPlayers.containsKey(player)) {
					droppedPlayers.remove(player);
					event.setCancelled(true);
				}
			}
		}
		if (event.getEntity() instanceof Zombie) {
			if (((Zombie)event.getEntity()).getCustomName() != null) {
				if (!event.getCause().equals(DamageCause.ENTITY_ATTACK)) {
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Zombie) {
			if (((Zombie)event.getEntity()).getCustomName() != null) {
				if ((event.getDamager() instanceof Player)) {
					event.setCancelled(false);
					Zombie zombie = (Zombie) event.getEntity();
					zombie.setAI(true);
//					zombie.setVelocity(player.getLocation().getDirection().divide(new Vector(0.8, 0, 0.8)).normalize().setY(0.35));
					needToBeNoAI.add(zombie);
					DustOptions dust = new DustOptions(Color.RED, 1);
					Bukkit.getOnlinePlayers().forEach(all -> {
						if (all.getLocation().distance(zombie.getLocation()) < 20) {
							all.playSound(zombie.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1, 1);
							all.spawnParticle(Particle.REDSTONE, zombie.getLocation().clone().add(0, 1, 0), (int)Math.round(3*event.getDamage()), 0.2d, 0, 0.2d, 0.6, dust);
						}
					});
					return;
				}
				event.setCancelled(true);
			}
		}
	}
	
	public static void saveThisDude(Player player) {
		if (droppedPlayers.containsKey(player)) droppedPlayers.remove(player);
		droppedPlayers.put(player, 20);
	}
}
