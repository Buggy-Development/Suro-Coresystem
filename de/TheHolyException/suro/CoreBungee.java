package de.TheHolyException.suro;

import java.io.File;
import java.io.IOException;

import de.TheHolyException.suro.commands.bungee.ReadToken;
import de.TheHolyException.suro.commands.bungee.SuroAdmin;
import de.TheHolyException.suro.commands.bungee.SystembCommand;
import de.TheHolyException.suro.listeners.bungee.PluginMessagingListener;
import de.TheHolyException.suro.listeners.bungee.onChat;
import de.TheHolyException.suro.listeners.bungee.onPlayerJoin;
import de.TheHolyException.suro.listeners.bungee.onProxyPing;
import de.TheHolyException.suro.managers.MySQLManager;
import de.TheHolyException.suro.managers.PluginMessagingManagerBungeeCord;
import de.TheHolyException.suro.managers.SuroEngine;
import de.TheHolyException.suro.utils.PlatformType;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class CoreBungee extends Plugin {
	
	private static CoreBungee instance;
	private MySQLManager mysqlmanager;
	private File config;
	private SuroEngine engine;
	private PluginMessagingManagerBungeeCord pluginMessagingManager;
	
	
	public void onEnable() {
		PlatformType.currentType = PlatformType.BUNGEECORD;
		CoreBungee.instance = this;
		config = new File(getDataFolder() + "//config.yml");
		File f = new File(getDataFolder() + "/");
		if(!f.exists()) f.mkdir();
		Configuration cfg = null;
		if (!config.exists()) {
			try {
				config.createNewFile();
				cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(config);
				cfg.set("mysql_host", "localhost");
				cfg.set("mysql_password", "P@ssword");
				ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, config);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(config);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		mysqlmanager = new MySQLManager(cfg.getString("mysql_host"), "3306", "mc_suro", "root", cfg.getString("mysql_password"));
		BungeeCord.getInstance().registerChannel("suro:sync");
		BungeeCord.getInstance().registerChannel("suro:messaging");
		engine = new SuroEngine();
		pluginMessagingManager = new PluginMessagingManagerBungeeCord();

		getProxy().getPluginManager().registerListener(this, new PluginMessagingListener());
		getProxy().getPluginManager().registerListener(this, new onProxyPing());
		getProxy().getPluginManager().registerListener(this, new onPlayerJoin());
		getProxy().getPluginManager().registerListener(this, new onChat());
		
		getProxy().getPluginManager().registerCommand(this, new SuroAdmin());
		getProxy().getPluginManager().registerCommand(this, new ReadToken());
		getProxy().getPluginManager().registerCommand(this, new SystembCommand());
	}
	
	public static CoreBungee getInstance() {
		return instance;
	};
	
	public MySQLManager getMySQLManager() {
		return mysqlmanager;
	}

	public SuroEngine getSuroEngine() {
		return engine;
	}
	
	public PluginMessagingManagerBungeeCord getPluginMessagingManager() {
		return pluginMessagingManager;
	}
}
