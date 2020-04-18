package de.TheHolyException.suro.managers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/*
 * @author MineBug.de Development
 *  
 * Created by TheHolyException at 05.01.2019 - 00:46:35
 */

public class MySQLDataManager {
	
	MySQLManager manager;
	
	protected MySQLDataManager(MySQLManager manager) {
		this.manager = manager;
	}
	
	public String getServerDataValue(String key) {
		try {
			ResultSet rs = manager.query("SELECT * FROM `ServerData` WHERE `KEY`='"+key+"'");
			if (rs.first()) {
				return rs.getString("VALUE");
			} else {
				System.out.println("Key not Found: " + key);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return "";
	}
	
	public boolean contains(String key) {
		try {
			ResultSet rs = manager.query("SELECT * FROM `ServerData` WHERE `KEY`='"+key+"'");
			return rs.first();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	public void set(String key, String value) {
		String qry;
		if (contains(key)) {
			qry = "UPDATE `ServerData` SET `VALUE`='"+value+"' WHERE `KEY`='"+key+"'";
		}  else {
			qry = "INSERT INTO `ServerData`(`KEY`, `VALUE`) VALUES ('"+key+"','"+value+"')";
		}
		manager.update(qry);
	}
	
	public Map<String, String> getData() {
		Map<String,String> map = new HashMap<>();
		
		try {
			ResultSet rs = manager.query("SELECT * FROM `ServerData`");
			while(rs.next()) {
				String key = rs.getString("KEY");
				String value = rs.getString("VALUE");
				map.put(key, value);
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
