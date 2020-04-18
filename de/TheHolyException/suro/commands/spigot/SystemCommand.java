package de.TheHolyException.suro.commands.spigot;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.text.DecimalFormat;
import java.util.Set;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.TheHolyException.suro.CoreSpigot;
import net.minecraft.server.v1_14_R1.MinecraftServer;

public class SystemCommand implements CommandExecutor {
	

	private DecimalFormat df = new DecimalFormat("##.00");
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
		
		if (sender.hasPermission("system.*")) {
			if (args.length == 0) {
				sender.sendMessage("§eNutze §6/system info §efür eine Systemweite übersicht.");
			}
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("info")) {
					OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
					String arch = os.getArch();

					String platform = System.getProperty("os.name");
					String platformversion = System.getProperty("os.version");
					Set<Thread> threads = Thread.getAllStackTraces().keySet();
					long freemem = (Runtime.getRuntime().freeMemory());
					long maxmem = (Runtime.getRuntime().maxMemory());
					long usedmem = maxmem - freemem;
					long freememMB = (freemem / 1000000);
					long maxmemMB = (maxmem / 1000000);
					long usedmemMB = maxmemMB - freememMB;
					double tps = MinecraftServer.getServer().recentTps[0];
					double mspt = CoreSpigot.getInstance().getMSPT().getMSPT();
					
					String head = "§6§l§m=============================================";
					sender.sendMessage(head);
					sender.sendMessage("§ePlatform: §6" + arch + "§7(§e" + platform + " " + platformversion +  "§7) §eRunning threads: §6"+threads.size());
					sender.sendMessage("§eTPS: §6" + df.format(tps));
					sender.sendMessage("§eMSPT: §6" + df.format(mspt));
					sender.sendMessage("§eCPU usage: §6" + getProcessCpuLoad() + "%");
					sender.sendMessage("§eMemory usage: §6" + (df.format((float)((float)usedmem/(float)maxmem)*100.0f)) + "% §7(§6"+usedmemMB+"§7/§6"+maxmemMB+" MB§7)");
					sender.sendMessage("§eJava version: §6" + System.getProperty("java.version"));
					sender.sendMessage("§eServer version: §6" + Bukkit.getBukkitVersion());
					sender.sendMessage("§eWorlds: ");
					int i = 1;
					for (World world : Bukkit.getWorlds()) {
						sender.sendMessage("  §7➥§e" + i + ". §6" + world.getName() + " §e" + world.getLoadedChunks().length + " Chunks §6" + world.getEntities().size() + " Entities §e" + world.getPlayers().size() + " Players");
						i++;
					}
					sender.sendMessage(head);
				}
			}
		}
		return true;
	}
	
	public static double getProcessCpuLoad() {
		try {
		    MBeanServer mbs    = ManagementFactory.getPlatformMBeanServer();
		    ObjectName name    = ObjectName.getInstance("java.lang:type=OperatingSystem");
		    AttributeList list = mbs.getAttributes(name, new String[]{ "ProcessCpuLoad" });

		    if (list.isEmpty())     return Double.NaN;

		    Attribute att = (Attribute)list.get(0);
		    Double value  = (Double)att.getValue();

		    // usually takes a couple of seconds before we get real values
		    if (value == -1.0)      return Double.NaN;
		    // returns a percentage value with 1 decimal point precision
		    return ((int)(value * 1000) / 10.0);
		} catch (Exception ex) {
			ex.printStackTrace();
			return Double.NaN;
		}
	}
}
