package de.TheHolyException.suro.utils;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import de.TheHolyException.suro.managers.MySQLPlayerDataManager;

@SuppressWarnings({"unchecked"})
public class JSONDataReader {
	
	private UUID uuid;
	private JSONObject JSONData;
	public static Set<JSONDataReader> datareader = new HashSet<JSONDataReader>();
	private static MySQLPlayerDataManager mysqlmanager = PlatformType.getMySQLManager().getPlayerDataManager();
	
	public JSONDataReader(UUID uuid) {
		this.uuid = uuid;
		sync();
	}
	
	public JSONObject getJSONData() {
		return JSONData;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public boolean getBoolean(String path, Boolean defaultvalue) {
		checkPath(path, defaultvalue);
		return Boolean.valueOf(JSONData.get(path).toString());
	}
	
	public int getInt(String path, int defaultvalue) {
		checkPath(path, defaultvalue);
		return Integer.valueOf(JSONData.get(path).toString());
	}
	
	public String getString(String path, String defaultvalue) {
		checkPath(path, defaultvalue);
		return String.valueOf(JSONData.get(path).toString());
	}

	public Double getDouble(String path, double defaultvalue) {
		checkPath(path, defaultvalue);
		return Double.valueOf(JSONData.get(path).toString());
	}
	
	public boolean containsKey(Object obj) {
		return JSONData.containsKey(obj);
	}
	
	private void checkPath(String path, Object defaultvalue) {
		if (defaultvalue == null) return;
		if (!JSONData.containsKey(path)) {
			JSONData.put(path, defaultvalue);
			save();
		}
	}
	
	private Object get(String path, Object defaultvalue) {
		if (!JSONData.containsKey(path)) {
			JSONData.put(path, defaultvalue);
			save();
		}
		return JSONData.get(path);
	}
	
	public List<Object> getList(String path) {
		List<Object> list = new ArrayList<>();
		for (Object obj : (JSONArray)get(path,new JSONArray())) list.add(obj.toString());
		return (list);
	}
	
	public void setList(String path, List<Object> list) {
		JSONArray array = new JSONArray();
		for (Object obj : list) {
			array.add(obj);
		}
		JSONData.put(path, array);
	}
	
	public void set(String path, Object value) {
		JSONData.put(path, value);
		save();
	}
	
	public void sync() {
		String rawdata = mysqlmanager.getPlayerJSONData(uuid.toString());
		if (rawdata == null) {
			JSONData = generateDefaultJSONData();
			save();
		} else {
			JSONParser parser = new JSONParser();
			try {
				JSONData = (JSONObject) parser.parse(decodeB64(rawdata));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void save() {
		mysqlmanager.set(uuid.toString(), encodeB64(JSONData.toJSONString()));
		PlatformType.syncPlayer(uuid);
	}
	
	@SuppressWarnings("unused")
	private JSONArray getArray(String value) {
		return (JSONArray) JSONData.get(value);
	}
	
	private JSONObject generateDefaultJSONData() {
		
		JSONObject data = new JSONObject();
		data.put("gamemaster", false);
		data.put("death", false);
		if (PlatformType.isPlayerOnline(uuid)) data.put("username", PlatformType.getUsername(uuid));
		return data;
	}
	
	
	private String encodeB64(String value) {
		try {
			return Base64.getEncoder().encodeToString(value.getBytes());
		} catch (Exception e) {
			return null;
		}
	}
	
	private String decodeB64(String value) {
		try {
			byte[] decodedValue = Base64.getDecoder().decode(value.getBytes());
			return new String(decodedValue);
		} catch (Exception e) {
			return null;
		}
	}

	public static JSONDataReader getDataReader(UUID uuid) {
		for (JSONDataReader reader : datareader) {
			if (reader.getUUID().equals(uuid)) return reader;
		}
		JSONDataReader reader = new JSONDataReader(uuid);
		datareader.add(reader);
		return reader;
	}
	
//	public static JSONDataReader getDataReader(String name) {
//		UUID userid = mysqlmanager.getAllUsers().get(name);
//		return getDataReader(userid);
//	}
	
	public static boolean containsPlayerReader(UUID uuid) {
		for (JSONDataReader readers : datareader) {
			if (readers.getUUID().equals(uuid)) return true;
		}
		return false;
	}
	
	public static void removePlayerReader(UUID uuid) {
		if (containsPlayerReader(uuid)) {
			JSONDataReader toremove = null;
			for (JSONDataReader readers : datareader) {
				if (readers.getUUID().equals(uuid)) {
					toremove = readers;
				}
			}
			datareader.remove(toremove);
		}
	}
	
	public static void dataReaderCleanUP() {
		Timer t = new Timer();
		t.schedule(new TimerTask() {
			
			@Override
			public void run() {
				HashSet<JSONDataReader> remove = new HashSet<JSONDataReader>();
				datareader.forEach(reader -> {if (!PlatformType.isPlayerOnline(reader.getUUID())) remove.add(reader);});
				remove.forEach(reader -> {datareader.remove(reader);});
			}
		}, 150000, 150000);
	}

}
