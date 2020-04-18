package de.TheHolyException.suro.utils;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import de.TheHolyException.suro.CoreSpigot;
import de.TheHolyException.suro.managers.TimeManager;

public class LobbyScoreboard implements ScoreboardModule {

	private TimeManager tm = CoreSpigot.getInstance().getTimeManager();
	
	@Override
	public ScoreboardData getScoreboardData(Player player) {

		if (CoreSpigot.getInstance().getSuroEngine().isIntroRunning()) return null;
		
		JSONDataReader reader = JSONDataReader.getDataReader(player.getUniqueId());
		ArrayList<String> list = new ArrayList<String>();
		
		list.add(" ");
		list.add("§bÖffnet in:");
		list.add("  §8- §7" + tm.getRemainingTime());
		list.add(" ");
		list.add("§bÖffnungszeiten:");
		list.add("  §8- §7"+tm.getStartTimeString()+" §8- §7"+tm.getStopTimeString());
		list.add(" ");
		
		if (!reader.containsKey("token")) {
			list.add("§cDu hast noch keinen §aToken");
			list.add("§ceingelößt, löse einen ein");
			list.add("§cmit §e/readtoken §7<Token>");
		} else {
			JSONObject tokeninfo = JSONReader.getJSONReader("tokens").getJSONObject(reader.getString("token", "na"), null);
			if (tokeninfo != null && tokeninfo.containsKey("banned") && Boolean.valueOf(tokeninfo.get("banned").toString())) {
				list.add("§c§lDein aktueller");
				list.add("§c§lToken ist Gesperrt!");
			} else list.add("§3Token: §aGültig");
		}
		
		
		return new ScoreboardData("§3§lSuRo", list);
	}
}
