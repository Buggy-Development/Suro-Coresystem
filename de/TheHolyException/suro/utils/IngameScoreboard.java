package de.TheHolyException.suro.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import de.TheHolyException.suro.CoreSpigot;
import de.TheHolyException.suro.commands.spigot.SystemCommand;
import de.TheHolyException.suro.managers.eventmanager.EventManager;
import de.TheHolyException.suro.managers.eventmanager.SuroEvent;
import de.TheHolyException.suro.managers.eventmanager.events.LOW_GRAVITY;
import de.TheHolyException.suro.managers.eventmanager.events.WATER_IS_TOXIC;

public class IngameScoreboard implements ScoreboardModule {

	private DecimalFormat df = new DecimalFormat("##.00");
	@Override
	public ScoreboardData getScoreboardData(Player player) {
		ArrayList<String> lines = new ArrayList<String>();
		EventManager eventmanager = CoreSpigot.getInstance().getEventManager();

		if (!eventmanager.getPrepairEventtasks().isEmpty()) {
			lines.add("§3Startende Events§b:");
			for (SuroEvent event : eventmanager.getPrepairEventtasks().keySet()) {
				lines.add(" §b"+event.getName()+"§7("+getStrenght(event)+"§7)");
				lines.add("  §7➥§bStartet in " + eventmanager.getPrepairEventtasks().get(event));
			}
		}
		
		if (!eventmanager.getEventTasks().isEmpty()) {
			lines.add("§3Laufende Events");
			for (SuroEvent event : eventmanager.getEventTasks().keySet()) {
				lines.add(" §b"+event.getName()+"§7("+getStrenght(event)+"§7)");
				lines.add("  §7➥§bEndet in §b" + eventmanager.getEventTasks().get(event));
			}
		}
		
		if (player.getName().equalsIgnoreCase("TheHolyException") || player.getName().equalsIgnoreCase("KaiGermany")) {
			int mb = 1024*1024;
			long freememMB = (Runtime.getRuntime().freeMemory()/mb);
			long maxmemMB = (Runtime.getRuntime().maxMemory()/mb);
			long totalMB = (Runtime.getRuntime().totalMemory()/mb);
			long usedmemMB = (totalMB - freememMB);
			int e = 0;
			int chunks = 0;
			double tps = TPS.getTPS();
			double mspt = CoreSpigot.getInstance().getMSPT().getMSPT();
			for (World world : Bukkit.getWorlds()) {
				e = e+world.getEntities().size();
				chunks = chunks + world.getLoadedChunks().length;
			}
			lines.add("§7==================");
			lines.add("§3TPS:§b"+getTPSString(tps)+" §3MSPT:§b"+getMSPTString(mspt));
			lines.add("§3R:§b"+usedmemMB+"§7/§b"+totalMB+"§7/§b"+maxmemMB);
			lines.add("§3P:§b"+Bukkit.getOnlinePlayers().size()+" §3E:§b"+e + " §3CH:§b"+chunks);
			lines.add("§3T§7(§eA:§b"+Bukkit.getScheduler().getActiveWorkers().size()+" §eP:§b"+Bukkit.getScheduler().getPendingTasks().size()+"§7) "+"§3C:§b"+SystemCommand.getProcessCpuLoad()+"%");
		}
		
		if (lines.size() == 0) return null;
		
		return new ScoreboardData("§3§lSuRo", lines);
	}
	
	private String getStrenght(SuroEvent event) {
		int s = event.getStrenght();
		String a = "none";
		if (event instanceof LOW_GRAVITY) {
			if (s == 0) a = "§aI";
			if (s == 1) a = "§eII";
			if (s == 2) a = "§cIII";
			if (s == 3) a = "§4IV";
		} else if (event instanceof WATER_IS_TOXIC) {
			if (s == 1) a = "§aI";
			if (s == 2) a = "§eII";
			if (s == 3) a = "§eIII";
			if (s == 4) a = "§cIV";
			if (s == 5) a = "§cV";
			if (s == 6) a = "§4VI";
			if (s == 7) a = "§4VII";
			if (s == 8) a = "§4VIII";
			if (s == 9) a = "§4IX";
			if (s == 10) a = "§4X";
			if (s == 11) a = "§4XI";
			if (s == 12) a = "§4XII";
			if (s == 13) a = "§4XIII";
			if (s == 14) a = "§4XIV";
			if (s == 15) a = "§4XV";
			if (s == 16) a = "§4XVI";
			if (s == 17) a = "§4XVII";
			if (s == 18) a = "§4XVIII";
			if (s == 19) a = "§4XIX";
			if (s == 20) a = "§§4XX";
		}
		
		if (!a.equals("none")) {
			a = "§7("+a+"§7)";
		}
		
		return a;
	} 
	
	private String getTPSString(double tps) {
		if (tps < 5) return "§4§l"+df.format(tps);
		if (tps < 10) return "§4"+df.format(tps);
		if (tps < 13) return "§c"+df.format(tps);
		if (tps < 16) return "§6"+df.format(tps);
		if (tps < 19) return "§e"+df.format(tps);
		if (tps < 20) return "§a"+df.format(tps);
		return "§2"+df.format(tps);
	}
	
	private String getMSPTString(double mspt) {
		if (mspt < 5) return "§4§l"+df.format(mspt);
		if (mspt < 10) return "§4"+df.format(mspt);
		if (mspt < 20) return "§c"+df.format(mspt);
		if (mspt < 30) return "§6"+df.format(mspt);
		if (mspt < 40) return "§e"+df.format(mspt);
		if (mspt < 50) return "§a"+df.format(mspt);
		return "§2"+df.format(mspt);
	}
}
