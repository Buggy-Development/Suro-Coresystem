package de.TheHolyException.suro.commands.spigot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.TheHolyException.suro.CoreSpigot;
import de.TheHolyException.suro.managers.eventmanager.events.LOW_GRAVITY;
import de.TheHolyException.suro.managers.eventmanager.events.SPECTRAL_DAY;
import de.TheHolyException.suro.managers.eventmanager.events.WATER_IS_TOXIC;
import de.TheHolyException.suro.utils.TextManager;

@SuppressWarnings({"unchecked","rawtypes"})
public class Events implements CommandExecutor, TabCompleter{

	private String[] tabcomlete = new String[] {"lowgravity", "toxicwater", "spectral"};
	private Map<Player, Consumer<Void>> confirmation = new HashMap();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
		Player player = (Player) sender;
		if (!player.hasPermission("system.events")) return true;
		
		if (!CoreSpigot.lobby) {
			if (args.length == 0) {
				
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("confirm")) {
					if (confirmation.containsKey(player)) {
						confirmation.get(player).accept(null);
						confirmation.remove(player);
						player.sendMessage("§3Event Bestätigt");
					}
				}
			} else if (args.length >= 3) {
				int startingtime = parseTimeString(args[1]);
				int stoptime = parseTimeString(args[2]);
				int strenght;

				if (startingtime == -1 || stoptime == -1) {
					player.sendMessage("Falsche zeitwerte");
					return true;
				}
				if (args[0].equalsIgnoreCase("lowgravity")) {
					strenght = (args.length == 4 ? (LOW_GRAVITY.checkStrenght(Integer.valueOf(args[3])) ? Integer.valueOf(args[3]) : 0) : 0);
					confirmation.put(player, s -> {
						CoreSpigot.getInstance().getEventManager()
						.startNewEvent(new LOW_GRAVITY(startingtime, stoptime, strenght));
					});
				} else if (args[0].equalsIgnoreCase("toxicwater")) {
					strenght = (args.length == 4 ? (WATER_IS_TOXIC.checkStrenght(Integer.valueOf(args[3])) ? Integer.valueOf(args[3]) : 2) : 2);
					confirmation.put(player, s -> {
						CoreSpigot.getInstance().getEventManager()
						.startNewEvent(new WATER_IS_TOXIC(startingtime, stoptime, strenght));
					});
				} else if (args[0].equalsIgnoreCase("spectral")) {
					confirmation.put(player, s -> {
						CoreSpigot.getInstance().getEventManager()
						.startNewEvent(new SPECTRAL_DAY(startingtime, stoptime));
					});
					strenght = -1;
				} else {
					return true;
				}

				player.sendMessage("§3Möchtest du das Event §b"+args[0]+" §3starten?");
				player.sendMessage("§3Zeit bis zum Start §7-> §b" + startingtime + " sekunden");
				player.sendMessage("§3Dauer des Events §7-> §b" + stoptime + " sekunden");
				if (strenght != -1) player.sendMessage("§3Stärke des Events §7 -> §b" + strenght + (args.length == 4 ? "" : "§7(§edefault§7)"));
				player.spigot().sendMessage(new TextManager("§7[§aConfirm§7]").addClickCommand("/events confirm").build());
			}
			
		}
		return true;
	}
	
	
	public static int parseTimeString(String timeString) {
		try {
			Date length;
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("mm'm'ss's'");
				length = formatter.parse(timeString);
			} catch (Exception e) {
				try {
					SimpleDateFormat formatter = new SimpleDateFormat("m'm'ss's'");
					length = formatter.parse(timeString);
				} catch (Exception e1) {
					try {
						SimpleDateFormat formatter = new SimpleDateFormat("m'm's's'");
						length = formatter.parse(timeString);
					} catch (Exception e2) {
						try {
							SimpleDateFormat formatter = new SimpleDateFormat("mm'm's's'");
							length = formatter.parse(timeString);
						} catch (Exception e3) {
							try {
								SimpleDateFormat formatter = new SimpleDateFormat("mm'm'");
								length = formatter.parse(timeString);
							} catch (Exception e4) {
								try {
									SimpleDateFormat formatter = new SimpleDateFormat("m'm'");
									length = formatter.parse(timeString);
								} catch (Exception e5) {
									try {
										SimpleDateFormat formatter = new SimpleDateFormat("s's'");
										length = formatter.parse(timeString);
									} catch (Exception e6) {
										SimpleDateFormat formatter = new SimpleDateFormat("ss's'");
										length = formatter.parse(timeString);
									}
								}
							}
						}
					}
				}
			}

			Calendar cal = GregorianCalendar.getInstance();
			cal.setTime(length);

			int time = (cal.get(12) * 60 + cal.get(13));

			return time;
		} catch (ParseException ex) {
			ex.printStackTrace();
			return -1;
		}
	}

	
	
	

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String arg, String[] args) {
		if (!(args.length > 1 || args.length == 0)) {
            List<String> matches = new ArrayList<>();
            if (args.length == 1) {
                String search = args[0].toLowerCase();
                for (String gens : tabcomlete) {
                    if (gens.toLowerCase().startsWith( search)) {
                        matches.add(gens);
                    }
                }
            } else {
                String search = args[1].toLowerCase();
                for (String gens : tabcomlete) {
                    if (gens.toLowerCase().startsWith(search)) {
                        matches.add(gens);
                    }
                }
            }
            return matches;
        }
		return null;
	}
}