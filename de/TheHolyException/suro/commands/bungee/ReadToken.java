package de.TheHolyException.suro.commands.bungee;

import java.util.List;

import org.json.simple.JSONObject;

import de.TheHolyException.suro.managers.TokenManager;
import de.TheHolyException.suro.utils.JSONDataReader;
import de.TheHolyException.suro.utils.JSONReader;
import de.TheHolyException.suro.utils.LuckPermsUtils;
import de.TheHolyException.suro.utils.Messages;
import me.lucko.luckperms.api.DataMutateResult;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReadToken extends Command {
	
	public ReadToken() {
		super("readtoken");
	}

	@SuppressWarnings({"unchecked","deprecation"})
	public void execute(CommandSender sender, String[] args) {
		ProxiedPlayer player = (ProxiedPlayer) sender;
		if (args.length == 1) {
			JSONReader settings = JSONReader.getJSONReader("settings");
			if (settings.getBoolean("tokenreading", false)) {
				List<String> tokens = TokenManager.getAllUnusedTokens();
				if (tokens.contains(args[0])) {
					JSONReader serverreader = JSONReader.getJSONReader("tokens");
					JSONObject obj = serverreader.getJSONObject(args[0], null);
					if (obj != null) {
						if (!Boolean.valueOf(obj.get("used").toString())) {
							player.sendMessage(Messages.prefix + "Du hast diesen Token erfolgreich eingelößt.");
							JSONDataReader reader = JSONDataReader.getDataReader(player.getUniqueId());
							if (reader.containsKey("token")) {
								serverreader.remove(reader.getString("token", null));
							}
							obj.put("used", true);
							obj.put("userid", player.getUniqueId().toString());
							serverreader.set(args[0], obj);
							reader.set("token", args[0]);
							String group;
							if (obj.containsKey("streamer") && Boolean.valueOf(obj.get("streamer").toString())) {
								player.sendMessage(Messages.prefix + "Dies ist ein Streamer Token, du kannst daher weitere Features nutzen");
								reader.set("streamer", true);
								group = "streamer";
							} else {
								group = "spieler";
							}
							DataMutateResult result = LuckPermsUtils.setGroup(player.getUniqueId(), group);
							if (result == DataMutateResult.FAIL) {
								player.sendMessage(Messages.prefix + "§cEs ist ein Fehler beim setzen der Rechte aufgetreten, bitte wende dich an einen Gamemaster");
							}
						} else player.sendMessage(Messages.prefix + "Dieser Token ist ungültig");
					} else player.sendMessage(Messages.prefix + "§cEs ist ein Fehler aufgetreten, bitte wende dich an einen Gamemaster");
				} else player.sendMessage(Messages.prefix + "Dieser Token ist ungültig");
			} else player.sendMessage(Messages.prefix + "Es können keine Tokens mehr eingelößt werden");
		}
	}
}
