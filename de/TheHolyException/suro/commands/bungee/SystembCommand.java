package de.TheHolyException.suro.commands.bungee;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.text.DecimalFormat;
import java.util.Set;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Command;

//@author Created by TheHolyException at 29.01.2019 - 15:44:22

public class SystembCommand extends Command {
	
	private DecimalFormat df = new DecimalFormat("##.00");

	public SystembCommand() {
		super("systemb");
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender.hasPermission("system.*")) {
			if (args.length == 0) {
				
			} 
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("info")) {
					OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
					double load = os.getSystemLoadAverage();
					String arch = os.getArch();
					
					String platform = System.getProperty("os.name");
					String platformversion = System.getProperty("os.version");
					Set<Thread> threads = Thread.getAllStackTraces().keySet();
					
					long freemem = (Runtime.getRuntime().freeMemory());
					long maxmem = (Runtime.getRuntime().maxMemory());
					long totalmem = (Runtime.getRuntime().totalMemory());
					long usedmem = totalmem - freemem;
					long freememMB = (freemem / 1000000);
					long maxmemMB = (maxmem / 1000000);
					long totalmemMB = (totalmem / 1000000);
					long usedmemMB = totalmemMB - freememMB;
					
					String head = "§3§l§m=============================================";
					sender.sendMessage(head);
					sender.sendMessage("§bPlatform: §3" + arch + "§7(§b" + platform + " " + platformversion +  "§7) §bRunning threads: §3"+threads.size());
					sender.sendMessage("§bCPU usage: §3" + load + "%");
					sender.sendMessage("§bMemory usage: §3" + (df.format((float)((float)usedmem/(float)maxmem)*100.0f)) + "% §7(§3"+usedmemMB+"§7/§3"+maxmemMB+" MB§7)");
					sender.sendMessage("§bJava version: §3" + System.getProperty("java.version"));
					sender.sendMessage("§bServer version: §3" + ProxyServer.getInstance().getVersion() + "§7(§3"+ProxyServer.getInstance().getGameVersion()+"§7)");
					sender.sendMessage("§bServers: ");
					int i = 1;
					for (ServerInfo server : ProxyServer.getInstance().getServers().values()) {
						sender.sendMessage("  §7➥§b" + i + ". §3" + server.getName() + " §b" + server.getPlayers().size() + " Spieler §3" + server.getAddress().toString()+ " Addresse");
						i++;
					}
					sender.sendMessage(head);
				}
			}
		}
	}
}
