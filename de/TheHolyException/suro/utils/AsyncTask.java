package de.TheHolyException.suro.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class AsyncTask {
	
	private static AsyncTask asynctask;
	private List<Consumer<Void>> tasks = new ArrayList<>();
	
	public AsyncTask() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				for (Consumer<Void> task : tasks) {
					task.accept(null);
				}
				tasks.clear();
			}
		}, 1000,1000);
	}
	
	public static AsyncTask getAsyncTask() {
		if (asynctask == null) asynctask = new AsyncTask();
		return asynctask;
	}
	
	public void executeAsync(Consumer<Void> execution) {
		tasks.add(execution);
	}

}
