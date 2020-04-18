package de.TheHolyException.suro.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SUROPlayer {
	
	private UUID uuid;
	private boolean death;
	private boolean streamer;
	private boolean banned;
	private String token;
	private static Set<SUROPlayer> players = new HashSet<SUROPlayer>();
	
	
	public static SUROPlayer getSUROPlayer(UUID uuid) {
		for (SUROPlayer player : players) {
			if (player.getUniqueId().toString().equalsIgnoreCase(uuid.toString())) return player;
		}
		SUROPlayer player = new SUROPlayer(uuid);
		players.add(player);
		return player;
	}
	
	private SUROPlayer(UUID uuid) {
		this.uuid = uuid;
	}
	
	public SUROPlayer setDeath(boolean alive) {
		this.death = alive;
		return this;
	}
	
	public SUROPlayer setStreamer(boolean streamer) {
		this.streamer = streamer;
		return this;
	}

	public SUROPlayer setBanned(boolean banned) {
		this.banned = banned;
		return this;
	}
	
	public SUROPlayer setToken(String token) {
		this.token = token;
		return this;
	}
	
	public UUID getUniqueId() {
		return uuid;
	}
	
	public boolean isDeath() {
		return death;
	}
	
	public boolean isStreamer() {
		return streamer;
	}
	
	public boolean isBanned() {
		return banned;
	}
	
	public String getToken() {
		return token;
	}

}
