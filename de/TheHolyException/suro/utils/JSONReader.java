package de.TheHolyException.suro.utils;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import de.TheHolyException.suro.managers.MySQLManager;

@SuppressWarnings("unchecked")
public class JSONReader {

	private JSONObject JSONData;
	private String path;
	private MySQLManager manager = null;
	private static HashMap<String, JSONReader> cachedData = new HashMap<String, JSONReader>();
	
	
	public JSONReader(String path) {
		this.path = path;
		sync();
	}
	
	public static JSONReader getJSONReader(String path) {
		if (cachedData.containsKey(path)) {
			return cachedData.get(path);
		}
		return new JSONReader(path);
	}
	
	public JSONObject getJSONData() {
		return JSONData;
	}
	
	public List<Object> getList(String path) {
		List<Object> list = new ArrayList<>();
		for (Object obj : (JSONArray)get(path,new JSONArray())) list.add(obj.toString());
		return (list);
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
	
	public JSONObject getJSONObject(String path, JSONObject defaultvalue) {
		checkPath(path, defaultvalue);
		return (JSONObject) JSONData.get(path);
	}
	
	public boolean containsKey(Object obj) {
		return JSONData.containsKey(obj);
	}
	
	private void checkPath(String path, Object defaultvalue) {
		if (!JSONData.containsKey(path)) {
			if (defaultvalue != null) {
				JSONData.put(path, defaultvalue);
				save();
			}
		}
	}
	
	public void setList(String path, List<Object> list) {
		JSONArray array = new JSONArray();
		for (Object obj : list) {
			array.add(obj);
		}
		JSONData.put(path, array);
	}
	
	public void remove(String path) {
		JSONData.remove(path);
		save();
	}
	
	public void set(String path, Object value) {
		JSONData.put(path, value);
		save();
	}

	public Object get(String path, Object defaultvalue) {
		if (!JSONData.containsKey(path)) {
			JSONData.put(path, defaultvalue);
			save();
		}
		return JSONData.get(path);
	}
	
	public void sync() {
		if (manager == null) manager = PlatformType.getMySQLManager();
		try {
			if (!manager.getDataManager().contains(path)) {
				JSONData = new JSONObject();
			} else {
				JSONData = (JSONObject) new JSONParser().parse(decodeB64(manager.getDataManager().getServerDataValue(path)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void save() {
		manager.getDataManager().set(path, encodeB64(JSONData.toJSONString()));
		PlatformType.syncServerData(path);
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
	
}
