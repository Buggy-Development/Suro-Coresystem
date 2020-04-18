package de.TheHolyException.suro.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.scheduler.BukkitRunnable;

import de.TheHolyException.suro.CoreSpigot;

public class SyncTask {
	private static SyncTask synctask;
	private List<Consumer<Void>> tasks = new ArrayList<>();
	
	public SyncTask() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				for (Consumer<Void> task : tasks) {
					task.accept(null);
				}
				tasks.clear();
			}
		}.runTaskTimer(CoreSpigot.getInstance(), 20, 20);
	}
	
	public static SyncTask getSyncTask() {
		if (synctask == null) synctask = new SyncTask();
		return synctask;
	}
	
	public void executeSync(Consumer<Void> execution) {
		tasks.add(execution);
	}

}
