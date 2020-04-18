package de.TheHolyException.suro.managers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.TheHolyException.suro.utils.JSONDataReader;

/*
 * @author MineBug.de Development
 *  
 * Created by TheHolyException at 22.01.2019 - 18:28:44
 */

public class MySQLPlayerDataManager {

	private MySQLManager manager;
	
	public MySQLPlayerDataManager(MySQLManager manager) {
		this.manager = manager;
	}
	
	public String getPlayerJSONData(String uuid) {
		try {
			ResultSet rs = manager.query("SELECT * FROM `PlayerData` WHERE `UUID`='"+uuid+"'");
			if (rs.first()) {
				return rs.getString("JSONDATA");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public boolean contains(String uuid) {
		try {
			ResultSet rs = manager.query("SELECT * FROM `PlayerData` WHERE `UUID`='"+uuid+"'");
			return rs.first();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	public void set(String uuid, String jsondata) {
		String qry;
		if (contains(uuid)) {
			qry = "UPDATE `PlayerData` SET `JSONDATA`='"+jsondata+"' WHERE `UUID`='"+uuid+"'";
		}  else {
			qry = "INSERT INTO `PlayerData`(`UUID`, `JSONDATA`) VALUES ('"+uuid+"','"+jsondata+"')";
		}
		manager.update(qry);
	}
	
	public void delete(String uuid) {
		if (contains(uuid)) {
			manager.update("DELETE FROM `PlayerData` WHERE `UUID`='"+uuid+"'");
		}
	}
	
	public Map<String, UUID> getAllUsers() {
		Map<String, UUID> map = new HashMap<>();
		try {

			ResultSet rs = manager.query("SELECT * FROM `PlayerData`");
			while (rs.next()) {
				UUID uuid = UUID.fromString(rs.getString("UUID"));
				String username = JSONDataReader.getDataReader(uuid).getString("username", "#NOTSET#");
				map.put(username, uuid);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return map;
	}
	
	public UUID getPlayerbyName(String name) {
		Map<String,UUID> users = getAllUsers();
		for (String username : users.keySet()) {
			if (name.equalsIgnoreCase(username)) return users.get(username);
		}
		return null;
	}
	
	public Map<UUID, String> getAllUsersUUIDKey() {
		Map<UUID, String> map = new HashMap<>();
		try {

			ResultSet rs = manager.query("SELECT * FROM `PlayerData`");
			while (rs.next()) {
				UUID uuid = UUID.fromString(rs.getString("UUID"));
				String username = JSONDataReader.getDataReader(uuid).getString("username", "#NOTSET#");
				map.put(uuid, username);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return map;
	}
	
	//@return the manager
	public MySQLManager getMySQLManager() {
		return manager;
	}
}
