package de.TheHolyException.suro.managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;

/*
 * @author MineBug.de Development
 *  
 * Created by TheHolyException at 05.01.2019 - 00:20:01
 */

public class MySQLManager {
	
	private String host,port,database,username,password = null;
	private Connection connection;
	
	public MySQLManager(String host, String port, String database, String username, String password) {
		this.host = host;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = password;
		
		connect();
		connectionChecker(1800000);
		createTables();
	}
	
	public void connect() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database+"?autoReconnect=true", username, password);
			System.out.println("[CoreSystem] Succsesfully conntected to MySQL");
		} catch (SQLException e) {
			System.err.println("[CoreSystem] Connection to MySQL Failed: §c" + e.getMessage() + "§e.");
		}
	}
	
	public void close() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
		}
	}
	
	public boolean isConnected() {
		return connection != null;
	}
	
	public void update(String qry) {
		try {
			Statement st = connection.createStatement();
			st.executeUpdate(qry);
			st.close();
		} catch (SQLException e) {
			connect();
			System.err.println(e);
		}
	}	
	
	public ResultSet query(String qry) {
		ResultSet rs = null;

		try {
			Statement st = connection.createStatement();
			rs = st.executeQuery(qry);
		} catch (SQLException e) {
			connect();
			System.err.println(e);
		}
		return rs;
	}
	
	public void createTables() {
		if (isConnected()) {
			try {
				connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS ServerData (`KEY` VARCHAR(255) PRIMARY KEY, `VALUE` VARCHAR(255))");
				connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS PlayerData (`UUID` VARCHAR(255) PRIMARY KEY, `JSONDATA` VARCHAR(64532))");
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		} else {
		}
	}
	
	private void connectionChecker(long time) {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				if (!isConnected()) {
					connect();
				}
			}
		}, time); 
	}
	
	private MySQLDataManager manager = null;
	public MySQLDataManager getDataManager() {
		if (manager == null) manager = new MySQLDataManager(this);
		return manager;
	}
	private MySQLPlayerDataManager playermanager = null;
	public MySQLPlayerDataManager getPlayerDataManager() {
		if (playermanager == null) playermanager = new MySQLPlayerDataManager(this);
		return playermanager;
	}
}
