package de.TheHolyException.suro.commands.bungee;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.json.simple.JSONObject;

import com.google.common.collect.ImmutableSet;

import de.TheHolyException.suro.CoreBungee;
import de.TheHolyException.suro.managers.MySQLManager;
import de.TheHolyException.suro.managers.PluginMessagingManagerBungeeCord;
import de.TheHolyException.suro.managers.TokenManager;
import de.TheHolyException.suro.utils.JSONDataReader;
import de.TheHolyException.suro.utils.JSONReader;
import de.TheHolyException.suro.utils.LuckPermsUtils;
import de.TheHolyException.suro.utils.Messages;
import de.TheHolyException.suro.utils.PlatformType;
import de.TheHolyException.suro.utils.RandomString;
import de.TheHolyException.suro.utils.SUROPlayer;
import de.TheHolyException.suro.utils.TextManager;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class SuroAdmin extends Command implements TabExecutor {

	private SimpleDateFormat format = new SimpleDateFormat("dd-MM HH_mm_ss");
	private MySQLManager manager = CoreBungee.getInstance().getMySQLManager();
	private String[] tabcomplete = new String[] {"checkplayer","checktoken","checksettings","deleteplayerdata","disabletoken","genstreamertokens"
			,"gentokens","getgameinformation","revive","togglegamemaster","setstarttime","setstoptime","toggleportal","tooglereadingtokens","toggleresourcepackrequest"};
	
	public SuroAdmin() {
		super("suroadmin", "system.*");
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		ProxiedPlayer player = (ProxiedPlayer) sender;
		if (!player.hasPermission("system.*")) return;
		
		if (args.length == 0) {
			
		} else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("checksettings")) {
				JSONReader reader = JSONReader.getJSONReader("settings");
				for (Object key : reader.getJSONData().keySet()) {
					String value = reader.getString(key.toString(), null);
					if (isBoolean(value)) {
						TextComponent text = new TextComponent("§3"+key.toString()+" §7-> §b");
						text.addExtra(new TextManager(value).addClickCommand("/suroadmin changesetting " + key.toString() + " " + (!Boolean.valueOf(value))).build());
						player.sendMessage(text);
					} else {
						player.sendMessage("§3"+key.toString()+" §7-> §b"+value);
					}
				}
				
				String launched = CoreBungee.getInstance().getMySQLManager().getDataManager().getServerDataValue("launched");
				TextComponent textl = new TextComponent("§3launched §7-> §b");
				textl.addExtra(new TextManager(launched).addClickCommand("/suroadmin changesetting launched " + (!Boolean.valueOf(launched))).build());
				player.sendMessage(textl);
				

				String started = CoreBungee.getInstance().getMySQLManager().getDataManager().getServerDataValue("started");
				TextComponent texts = new TextComponent("§3started §7-> §b");
				texts.addExtra(new TextManager(started).addClickCommand("/suroadmin changesetting started " + (!Boolean.valueOf(started))).build());
				player.sendMessage(texts);
			}
			if (args[0].equalsIgnoreCase("getgameinformation")) {
				
				List<SUROPlayer> totalplayers = CoreBungee.getInstance().getSuroEngine().getAllPlayers();
				List<SUROPlayer> aliveplayers = CoreBungee.getInstance().getSuroEngine().getAllAlivePlayers();
				
				player.sendMessage("§3Alive Players §7-> §b" + aliveplayers.size());
				player.sendMessage("§3Death Players §7-> §b" + (totalplayers.size()-aliveplayers.size()));
			}
			if (args[0].equalsIgnoreCase("toggleportal")) {
				JSONReader reader = JSONReader.getJSONReader("settings");
				if (reader.getBoolean("portal", false)) {
					reader.set("portal", false);
					Messages.send(player, "Das Portal wurde §cDeaktiviert§b!");
				} else {
					reader.set("portal", true);
					Messages.send(player, "Das Portal wurde §aAktiviert§b!");
				}
			}
			if (args[0].equalsIgnoreCase("tooglereadingtokens")) {
				JSONReader reader = JSONReader.getJSONReader("settings");
				if (reader.getBoolean("tokenreading", false)) {
					reader.set("tokenreading", false);
					Messages.send(player, "Tokens können nun nicht mehr Eineglößt werden§b!");
				} else {
					reader.set("tokenreading", true);
					Messages.send(player, "Tokens können wieder Eineglößt werden§b!");
				}
			}
			if (args[0].equalsIgnoreCase("toggleresourcepackrequest")) {
				JSONReader reader = JSONReader.getJSONReader("settings");
				if (reader.getBoolean("resourcepackrequest", false)) {
					reader.set("resourcepackrequest", false);
					Messages.send(player, "Das ResourcePack wird nicht länger Abgefragt!");
				} else {
					reader.set("resourcepackrequest", true);
					Messages.send(player, "Das ResourcePack nun Abgefragt!");
				}
			}
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("checktoken")) {
				List<String> tokens = TokenManager.getAllTokens();
				if (tokens.contains(args[1])) {
					JSONReader reader = new JSONReader("tokens");
					JSONObject obj = reader.getJSONObject(args[1], null);
					player.sendMessage(new TextComponent("Token: " + args[1] + " data ->"));
					String s = "";
					for (Object a : obj.keySet()) {
						s = s+" "+a.toString()+":"+obj.get(a);
					}
				}
			}
			
			if (args[0].equalsIgnoreCase("checkplayer")) {
				UUID uuid = manager.getPlayerDataManager().getPlayerbyName(args[1]);
				if (uuid == null) {
					player.sendMessage("Dieser Spieler ist nicht registriert.");
					return;
				}
				JSONDataReader reader = JSONDataReader.getDataReader(uuid);
				player.sendMessage(new TextComponent("Player: " + args[1] + " data ->"));
				JSONObject obj = (JSONObject) reader.getJSONData();
				for (Object key : obj.keySet()) {
					player.sendMessage("§3"+key+" §7-> §b"+obj.get(key));
				}
				if (reader.containsKey("token")) {
					player.sendMessage(new TextManager("§7[§cDisable Token§7]").addHoverEvent("Disable the token of this player").addClickCommandSuggest("/suroadmin disabletoken " + reader.getString("token", null)).build());
				} else player.sendMessage(new TextComponent("§7§m[§c§mDisable Token§7§m]"));
				if (reader.containsKey("death") && reader.getBoolean("death", false)) {
					player.sendMessage(new TextManager("§7[§aRevive Player§7]").addHoverEvent("Belebe diesen Spieler wieder").addClickCommandSuggest("/suroadmin revive " + args[1]).build());
				} else player.sendMessage(new TextComponent("§7§m[§a§mRevive Player§7§m]"));
			}
			
			if (args[0].equalsIgnoreCase("disabletoken")) {
				JSONReader reader = new JSONReader("tokens");
				JSONObject token = reader.getJSONObject(args[1], null);
				token.put("banned", true);
				reader.set(args[1], token);
				ProxiedPlayer tar = ProxyServer.getInstance().getPlayer(UUID.fromString(token.get("userid").toString()));
				if (tar != null && tar.isConnected()) {
					if (tar.getServer().getInfo().getName().equalsIgnoreCase("suro")) {
						ServerInfo tarserver = ProxyServer.getInstance().getServerInfo("Lobby");
						tar.connect(tarserver);
					}
					tar.sendMessage("§cDein Token wurde Gesperrt!");
				}
				Messages.send(player, "Der Token §e" + args[1] + " §bwurde Gesperrt");
			}
			
			if (args[0].equalsIgnoreCase("revive")) {
				UUID uuid = manager.getPlayerDataManager().getPlayerbyName(args[1]);
				if (uuid == null) {
					player.sendMessage("Dieser Spieler ist nicht registriert.");
					return;
				}
				JSONDataReader reader = JSONDataReader.getDataReader(uuid);
				reader.set("death", false);
				Messages.send(player, "§e" + args[1] + "§bwurde Wiederbelebt");
				PluginMessagingManagerBungeeCord.sendMessageFromBungeeToServers("suro:sync", "#DATASTREAM#REVIVE#"+uuid.toString());
			}
			
			if (args[0].equalsIgnoreCase("deleteplayerdata")) {
				UUID uuid = manager.getPlayerDataManager().getPlayerbyName(args[1]);
				if (uuid == null) {
					player.sendMessage("Dieser Spieler ist nicht registriert.");
					return;
				}
				manager.getPlayerDataManager().delete(uuid.toString());
				JSONDataReader.getDataReader(uuid).sync();
				PlatformType.syncPlayer(uuid);
			}
			
			if (args[0].equalsIgnoreCase("togglegamemaster")) {
				UUID uuid = manager.getPlayerDataManager().getPlayerbyName(args[1]);
				if (uuid == null) {
					player.sendMessage("Dieser Spieler ist nicht registriert.");
					return;
				}
				JSONDataReader reader = JSONDataReader.getDataReader(uuid);
				if (reader.getBoolean("gamemaster", false)) {
					reader.set("gamemaster", false);
					if (reader.containsKey("token")) {
						JSONReader tokens = JSONReader.getJSONReader("tokens");
						JSONObject token = tokens.getJSONObject(reader.getString("token", null), null);
						if (token != null) {
							if (token.containsKey("streamer") && Boolean.valueOf(token.get("streamer").toString())) {
								LuckPermsUtils.setGroup(uuid, "streamer");
							} else {
								LuckPermsUtils.setGroup(uuid, "spieler");
							}
							return;
						}
					}
					LuckPermsUtils.setGroup(uuid, "default");
					Messages.send(player, "Der Spieler §e" + args[1] + " ist nicht länger GameMaster");
					
				} else {
					reader.set("gamemaster", true);
					LuckPermsUtils.setGroup(uuid, "GameMaster");
					Messages.send(player, "Der Spieler §e" + args[1] + " ist nun GameMaster");
				}
			}
			
			if (args[0].equalsIgnoreCase("setstarttime")) {
				if (checkIfTimeStamp(args[1])) {
					JSONReader reader = JSONReader.getJSONReader("settings");
					reader.set("starttime", args[1]);
					PlatformType.syncServerData("settings");
					Messages.send(player, "Die Startzeit wurde zu §e" + args[1] + " §bgeändert");
				} else Messages.send(player, "Das format ist Falsch! nutze §712:34");
			}
			
			if (args[0].equalsIgnoreCase("setstoptime")) {
				if (checkIfTimeStamp(args[1])) {
					JSONReader reader = JSONReader.getJSONReader("settings");
					reader.set("stoptime", args[1]);
					PlatformType.syncServerData("settings");
					Messages.send(player, "Die Stopzeit wurde zu §e" + args[1] + " §bgeändert");
				} else Messages.send(player, "Das format ist Falsch! nutze §712:34");
			}
			
			
			if (args[0].equalsIgnoreCase("gentokens")) {
				int amount = Integer.valueOf(args[1]);
				RandomString randomstringgen = new RandomString(24);
				File folder = new File("plugins//CoreSystem//tokens//");
				if (folder.exists()) folder.mkdirs();
				File file = new File("plugins//CoreSystem//tokens//"+format.format(new Date(System.currentTimeMillis()))+".yml");
				try {
					file.createNewFile();
				} catch (IOException ex) {ex.printStackTrace();}
				
				List<String> tokens = new ArrayList<String>();
				try {
					FileOutputStream fos = new FileOutputStream(file);
					for (int i = 0; i < amount; i++) {
						String s = randomstringgen.nextString();
						fos.write((s+"\n").getBytes());
						tokens.add(s);
					}
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				JSONReader reader = new JSONReader("tokens");
				tokens.forEach(token -> {
					JSONObject obj = new JSONObject();
					obj.put("used", false);
					reader.set(token, obj);
				});
				player.sendMessage(new TextComponent("§aErfolgreich erstellt, tokens sind bei §eplugins/CoreSystem/tokens"));
			}
			
			
			if (args[0].equalsIgnoreCase("genstreamertokens")) {
				int amount = Integer.valueOf(args[1]);
				RandomString randomstringgen = new RandomString(24);
				File folder = new File("plugins//CoreSystem//tokens//");
				if (folder.exists()) folder.mkdirs();
				File file = new File("plugins//CoreSystem//tokens//"+format.format(new Date(System.currentTimeMillis()))+"_STREAMERTOKENS.yml");
				try {
					file.createNewFile();
				} catch (IOException ex) {ex.printStackTrace();}
				
				List<String> tokens = new ArrayList<String>();
				try {
					FileOutputStream fos = new FileOutputStream(file);
					for (int i = 0; i < amount; i++) {
						String s = randomstringgen.nextString();
						fos.write((s+"\n").getBytes());
						tokens.add(s);
					}
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				JSONReader reader = new JSONReader("tokens");
				tokens.forEach(token -> {
					JSONObject obj = new JSONObject();
					obj.put("used", false);
					obj.put("streamer", true);
					reader.set(token, obj);
				});
				player.sendMessage(new TextComponent("§aErfolgreich erstellt, tokens sind bei §eplugins/CoreSystem/tokens"));
			}
		} else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("changesetting")) {
				String setting = args[1];
				if (setting.equalsIgnoreCase("launched") || setting.equalsIgnoreCase("started")) {
					CoreBungee.getInstance().getMySQLManager().getDataManager().set(setting, args[2]);;
				} else {
					JSONReader reader = JSONReader.getJSONReader("settings");
					if (reader.containsKey(setting)) {
						reader.set(setting, args[2]);
					}
				}
				BungeeCord.getInstance().getPluginManager().dispatchCommand(player, "suroadmin checksettings");
			}
		}
	}
	
	private boolean isBoolean(String s) {
		try {
			Boolean b = Boolean.valueOf(s);
			if (b) {}
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	
	private boolean checkIfTimeStamp(String timestamp) {
		
		try {
			String[] a = timestamp.split(":");
			Integer.valueOf(a[0]);
			Integer.valueOf(a[1]);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	
	@Override
	public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
		if (!(args.length > 1 || args.length == 0)) {
            Set<String> matches = new HashSet<>();
            if (args.length == 1) {
                String search = args[0].toLowerCase();
                for (String gens : tabcomplete) {
                    if (gens.toLowerCase().startsWith( search)) {
                        matches.add(gens);
                    }
                }
            } else {
                String search = args[1].toLowerCase();
                for (String gens : tabcomplete) {
                    if (gens.toLowerCase().startsWith(search)) {
                        matches.add(gens);
                    }
                }
            }
            return matches;
        } else if (!(args.length > 2 || args.length == 1)) {
        	if (args[0].equalsIgnoreCase("checkplayer") || args[0].equalsIgnoreCase("deleteplayerdata")) {
                Set<String> matches = new HashSet<>();
                for (SUROPlayer player : CoreBungee.getInstance().getSuroEngine().getAllPlayers()) {
                	JSONDataReader reader = JSONDataReader.getDataReader(player.getUniqueId());
                	if (reader.containsKey("username")) {
                		matches.add(reader.getString("username", null));
                	}
                }
                return matches;
        	}
        }

        

        return ImmutableSet.of();
	}
}
