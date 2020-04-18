package de.TheHolyException.suro.listeners.bungee;

import java.util.Arrays;
import java.util.List;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.PlayerInfo;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class onProxyPing implements Listener {
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onProxyPingEvent(ProxyPingEvent event) {
		
		ServerPing ping = event.getResponse();
		List<String> lines;

		
		lines = Arrays.asList(             "§6§l╔═════════════════╗"
				,                          "             §e§lMineBug SuRo"
				,                          ""
				,                          " §7§lSpieler auf §6§lLobby   §8§l-> §e" + ProxyServer.getInstance().getServerInfo("Lobby").getPlayers().size()
				,                          " §7§lSpieler auf §b§lSuRo    §8§l-> §e" + ProxyServer.getInstance().getServerInfo("suro").getPlayers().size()
				,                          ""
				,                          "     §6§lWebSite §8§l-> §eMineBug.de"
				,                          "     §6§lTeamSpeak §8§l-> §eMineBug.de"
				,                          "     §6§lEmail     §8§l-> §email@minebug.de"
				,                          ""
				,                          "§6§l╚═════════════════╝");

		PlayerInfo[] sample = new PlayerInfo[lines.size()];
		for (int i = 0; i < sample.length; i ++) {
			sample[i] = new PlayerInfo(lines.get(i), "");
		}
		ping.setDescription("§3§lSuRo §bProjektServer §7- §bOhne Sicherheitslücken");
		ping.getPlayers().setSample(sample);
		
	}

}
