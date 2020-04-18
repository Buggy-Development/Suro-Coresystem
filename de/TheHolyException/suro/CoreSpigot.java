package de.TheHolyException.suro;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import de.TheHolyException.suro.commands.spigot.Build;
import de.TheHolyException.suro.commands.spigot.Events;
import de.TheHolyException.suro.commands.spigot.GenerateChunks;
import de.TheHolyException.suro.commands.spigot.SetLocation;
import de.TheHolyException.suro.commands.spigot.StartIntro;
import de.TheHolyException.suro.commands.spigot.SystemCommand;
import de.TheHolyException.suro.commands.spigot.UpdateTimes;
import de.TheHolyException.suro.listeners.spigot.BlockBreak;
import de.TheHolyException.suro.listeners.spigot.EntityDamageEntity;
import de.TheHolyException.suro.listeners.spigot.EntityDeath;
import de.TheHolyException.suro.listeners.spigot.EntitySpawn;
import de.TheHolyException.suro.listeners.spigot.PlayerChat;
import de.TheHolyException.suro.listeners.spigot.PlayerCommandPreprocess;
import de.TheHolyException.suro.listeners.spigot.PlayerDeath;
import de.TheHolyException.suro.listeners.spigot.PlayerInteract;
import de.TheHolyException.suro.listeners.spigot.PlayerJoin;
import de.TheHolyException.suro.listeners.spigot.PlayerMove;
import de.TheHolyException.suro.listeners.spigot.PlayerQuit;
import de.TheHolyException.suro.listeners.spigot.PlayerResourcePackStatus;
import de.TheHolyException.suro.listeners.spigot.PluginMessagingListener;
import de.TheHolyException.suro.listeners.spigot.PotionBrew;
import de.TheHolyException.suro.managers.BuildManager;
import de.TheHolyException.suro.managers.CameraStudio;
import de.TheHolyException.suro.managers.CustomScoreboardManager;
import de.TheHolyException.suro.managers.MySQLManager;
import de.TheHolyException.suro.managers.SpigotPluginMessagingMananger;
import de.TheHolyException.suro.managers.SuroEngineSpigot;
import de.TheHolyException.suro.managers.TeleportManager;
import de.TheHolyException.suro.managers.TimeManager;
import de.TheHolyException.suro.managers.ZombieManager;
import de.TheHolyException.suro.managers.eventmanager.EventManager;
import de.TheHolyException.suro.utils.CustomPlaceholders;
import de.TheHolyException.suro.utils.IngameScoreboard;
import de.TheHolyException.suro.utils.JSONDataReader;
import de.TheHolyException.suro.utils.LobbyScoreboard;
import de.TheHolyException.suro.utils.MSPT;
import de.TheHolyException.suro.utils.PlatformType;
import de.TheHolyException.suro.utils.TPS;
import de.TheHolyException.suro.utils.WorldGenerator;
import fr.minuskube.inv.SmartInvsPlugin;

public class CoreSpigot extends JavaPlugin {
	
	private static CoreSpigot instance;
	private static File config;
	private MySQLManager mysqlmanager;
	private SpigotPluginMessagingMananger messagingmanager;
	private TimeManager timemamager;
	private BuildManager buildmanager;
	private MSPT mspt;
	private SuroEngineSpigot engine;
	private CameraStudio camera;
	private ZombieManager zombieManager;
	private EventManager eventManager;
	private TeleportManager teleportManager;
	public static boolean lobby;
	
	public void onEnable() {
		PlatformType.currentType = PlatformType.SPIGOT_1_14;
		FileConfiguration cfg;
		CoreSpigot.instance = this;
		File f = new File(getDataFolder() + "/");
		if(!f.exists()) f.mkdir();
		config = new File(getDataFolder() + "//config.yml");
		if (!config.exists()) {
			try {
				config.createNewFile();
				cfg = YamlConfiguration.loadConfiguration(config);
				cfg.set("lobby", false);
				cfg.set("mysql_host", "localhost");
				cfg.set("mysql_password", "P@ssword");
				cfg.save(config);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		cfg = YamlConfiguration.loadConfiguration(config);
		mysqlmanager = new MySQLManager(cfg.getString("mysql_host"), "3306", "mc_suro", "root", cfg.getString("mysql_password"));
		JSONDataReader.dataReaderCleanUP();

		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) new CustomPlaceholders().register();
		
		cfg = YamlConfiguration.loadConfiguration(config);
		lobby = cfg.getBoolean("lobby");

		System.out.println(" ");
		System.out.println(" ");
		System.out.println("Lobby mode -> " + lobby);
		System.out.println(" ");
		System.out.println(" ");
		
		new SmartInvsPlugin().onEnable(this);
		
		getCommand("setlocation").setExecutor(new SetLocation());
		getCommand("updatetimes").setExecutor(new UpdateTimes());
		getCommand("build").setExecutor(new Build());
		getCommand("startintro").setExecutor(new StartIntro());
		getCommand("system").setExecutor(new SystemCommand());
		getCommand("events").setExecutor(new Events());
		getCommand("generatechunks").setExecutor(new GenerateChunks());
		
		registerListener(new PlayerJoin());
		registerListener(new PlayerMove());
		registerListener(new PlayerQuit());
		registerListener(new PlayerChat());
		registerListener(new PlayerInteract());
		registerListener(new PlayerCommandPreprocess());
		registerListener(new PlayerResourcePackStatus());
		
		PluginMessagingListener pml = new PluginMessagingListener();
//		Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new SpigotPluginMessagingListener());
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "suro:sync");
		Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", pml);
		Bukkit.getMessenger().registerIncomingPluginChannel(this, "suro:sync", pml);
		
		messagingmanager = new SpigotPluginMessagingMananger();
		timemamager = new TimeManager();
		mspt = new MSPT();
		engine = new SuroEngineSpigot();
		camera = new CameraStudio();
		zombieManager = new ZombieManager();
		teleportManager = new TeleportManager(300, 300);
		CustomScoreboardManager sbmanager = new CustomScoreboardManager();
		
		if (lobby) {
			sbmanager.setModule(new LobbyScoreboard());
			buildmanager = new BuildManager(true);
		} else {
			sbmanager.setModule(new IngameScoreboard());
			buildmanager = new BuildManager(false);
			eventManager = new EventManager();
			zombieManager.removeAllZombies();
			zombieManager.spawnAllZombies();
			registerListener(new EntityDamageEntity());
			registerListener(new EntityDeath());
			registerListener(new EntitySpawn());
			registerListener(new BlockBreak());
			registerListener(new PlayerDeath());
			registerListener(new PotionBrew());
		}
		
		new TPS().run();
	}
	
	public void onDisable() {
		getSuroEngine().deleteAllHives(false);
	}
	
	private void registerListener(Listener listener) {
		getInstance().getServer().getPluginManager().registerEvents(listener, instance);
	}
	
	public static CoreSpigot getInstance() {
		return instance;
	}
	
	public MySQLManager getMySQLManager() {
		return mysqlmanager;
	}
	
	public SpigotPluginMessagingMananger getPluginMessagingManager() {
		return messagingmanager;
	}
	
	public TimeManager getTimeManager() {
		return timemamager;
	}
	
	public MSPT getMSPT() {
		return mspt;
	}
	
	public BuildManager getBuildmanager() {
		return buildmanager;
	}
	
	public SuroEngineSpigot getSuroEngine() {
		return engine;
	}
	
	public CameraStudio getCameraStudio() {
		return camera;
	}
	
	public ZombieManager getZombieManager() {
		return zombieManager;
	}
	
	public EventManager getEventManager() {
		return eventManager;
	}
	
	public TeleportManager getTeleportManager() {
		return teleportManager;
	}
	
	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		return new WorldGenerator(id);
	}
	
}
