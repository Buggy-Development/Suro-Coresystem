package de.TheHolyException.suro.utils;

import java.text.DecimalFormat;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import de.TheHolyException.suro.CoreSpigot;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.minecraft.server.v1_14_R1.MinecraftServer;

@SuppressWarnings("deprecation")
public class CustomPlaceholders extends PlaceholderExpansion {

	private DecimalFormat df = new DecimalFormat("#0.00");
	@Override
	public String onPlaceholderRequest(Player player, String args) {
		
		if (player == null) return null;
		
		if (args.equals("mspt")) {
			return CoreSpigot.getInstance().getMSPT().getMSPT()+"";
		}
		if (args.equals("tps")) {
			return df.format(MinecraftServer.getServer().recentTps[0])+"";
		}
		
		return null;
	}

	@Override
	public String getAuthor() {
		return "TheHolyException";
	}

	@Override
	public String getIdentifier() {
		return "coresystem";
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}

	@Override
	public String onRequest(OfflinePlayer player, String identifier) {
		
		if (player == null) return null;
		if (identifier.equalsIgnoreCase("mspt")) {
			return CoreSpigot.getInstance().getMSPT().getMSPT()+"";
		}
		if (identifier.equalsIgnoreCase("tps")) {
			return df.format(MinecraftServer.getServer().recentTps[0])+"";
		}
		
		return null;
	}
	
}
