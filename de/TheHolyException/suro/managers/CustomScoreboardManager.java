package de.TheHolyException.suro.managers;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import de.TheHolyException.suro.CoreSpigot;
import de.TheHolyException.suro.utils.ScoreboardData;
import de.TheHolyException.suro.utils.ScoreboardModule;
 
public class CustomScoreboardManager {
	
	private ScoreboardModule module = null;
	
	public CustomScoreboardManager() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if (module != null) {
					Bukkit.getOnlinePlayers().forEach(player -> {
						sendScoreboard(player, module.getScoreboardData(player));
					});
				}
			}
		}.runTaskTimer(CoreSpigot.getInstance(), 10, 20);
	}
	
	public void setModule(ScoreboardModule module) {
		this.module = module;
	}
	
	public void forcesendScoreboard(Player player) {
		if (module != null) sendScoreboard(player, module.getScoreboardData(player));
	}
	
	private HashMap<Player, ScoreboardData> boards = new HashMap<Player, ScoreboardData>();
	private void sendScoreboard(Player player, ScoreboardData data) {
		
		if (data == null) {
			player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
			return;
		}
		
		Scoreboard board;
		int size = data.getLines().size();
		
		if (boards.containsKey(player) && boards.get(player).getBoard() != null) {
			if (data.getLines().size() != boards.get(player).getLines().size()) {
				boards.remove(player);
				sendScoreboard(player, data);
				return;
			}
			board = boards.get(player).getBoard();
			int i = 0;
			for (String line : data.getLines()) {
				Team team = board.getTeam("t-"+i);
				if (team == null) continue;
				team.setSuffix(line);
				i++;
			}
			
		} else {
			board = Bukkit.getScoreboardManager().getNewScoreboard();
			@SuppressWarnings("deprecation")
			Objective sidebar = board.registerNewObjective("bugboard", "dummy");
			sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
			sidebar.setDisplayName(data.getDisplayname());
			
			int i = 0;
			for (String line : data.getLines()) {
				Team team = board.registerNewTeam("t-"+i);
				team.setSuffix(line);
				String entry = getEntry(i);
				team.addEntry(entry);
				sidebar.getScore(entry).setScore(size-i);
				i++;
			}
		}
		data.setBoard(board);
		boards.put(player, data);
		player.setScoreboard(board);
	}
	
	private String getEntry(int i) {
		if (i < 10) {
			return "§"+i;
		} else if (i < 16) {
			if (i == 10) return "§a";
			if (i == 11) return "§b";
			if (i == 12) return "§c";
			if (i == 13) return "§d";
			if (i == 14) return "§e";
			if (i == 15) return "§f";
		} else return "§"+(i-16)+"§l";
		return null;
	}
	
}
