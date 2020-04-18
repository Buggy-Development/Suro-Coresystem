package de.TheHolyException.suro.managers;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.UUID;

import org.json.simple.JSONObject;

import de.TheHolyException.suro.utils.JSONDataReader;
import de.TheHolyException.suro.utils.JSONReader;

public class TokenManager {
	
	public static List<String> getAllUnusedTokens() {
		List<String> list = new ArrayList<String>();
		
		JSONReader reader = new JSONReader("tokens");
		try {
			for (Object token : reader.getJSONData().keySet()) {
				JSONObject obj = (JSONObject) reader.getJSONObject(token.toString(), null);
				if (!Boolean.valueOf(obj.get("used").toString())) {
					list.add((String)token);
				}
			}
		} catch (ConcurrentModificationException ex) {}
		return list;
	}
	
	public static List<String> getAllTokens() {
		List<String> list = new ArrayList<String>();
		
		JSONReader reader = new JSONReader("tokens");
		for (Object token : reader.getJSONData().keySet()) {
			list.add((String)token);
		}
		return list;
	}
	
	public static String getPlayersToken(UUID uuid) {	
		JSONDataReader reader = JSONDataReader.getDataReader(uuid);
		if (reader.containsKey("token")) {
			return reader.getString("token", null);
		}
		return null;
	}
	
	public static List<String> getAllTokensonPlayerRegistert(UUID uuid) {
		List<String> list = new ArrayList<String>();

		JSONReader reader = new JSONReader("tokens");
		getAllTokens().forEach(token -> {
			JSONObject obj = reader.getJSONObject(token, null);
			if (obj.get("userid").toString().equalsIgnoreCase(uuid.toString())) {
				list.add(token);
			}
		});
		
		return list;
	}
	
	public static boolean hasValidToken(UUID uuid) {
		JSONDataReader reader = JSONDataReader.getDataReader(uuid);
		JSONReader tokens = JSONReader.getJSONReader("tokens");
		
		if (!reader.containsKey("token")) return false;
		String tokenstring = reader.getString("token", null);
		if (tokenstring == null) return false;
		JSONObject token = tokens.getJSONObject(tokenstring, null);
		if (token == null) return false;
		if (token.containsKey("banned") && Boolean.valueOf(token.get("banned").toString())) return false; 
		
		return true;
	}
	
	public static boolean isDeath(UUID uuid) {
		JSONDataReader reader = JSONDataReader.getDataReader(uuid);
		return reader.getBoolean("death", false);
	}
	
	public static boolean canSpectate(UUID uuid) {
		JSONDataReader reader = JSONDataReader.getDataReader(uuid);
		JSONReader tokens = JSONReader.getJSONReader("tokens");

		if (!reader.containsKey("token")) return false;
		String tokenstring = reader.getString("token", null);
		if (tokenstring == null) return false;
		JSONObject token = tokens.getJSONObject(tokenstring, null);
		if (token == null) return false;
		if (!(token.containsKey("streamer") && Boolean.valueOf(token.get("streamer").toString()))) return false;
		
		return true;
	}
	
	public static boolean isGamemaster(UUID uuid) {
		JSONDataReader reader = JSONDataReader.getDataReader(uuid);
		
		if (!(reader.containsKey("gamemaster") && reader.getBoolean("gamemaster", null))) return false;
		
		return true;
	}
	
	public static void setGamemaster(UUID uuid, boolean value) {
		JSONDataReader reader = JSONDataReader.getDataReader(uuid);
		reader.set("gamemaster", value);
	}
	
}
