package de.TheHolyException.suro.utils;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Messages {
	
	public static String prefix = "§3SURO §7>> §b";

	@SuppressWarnings("deprecation")
	public static void send(ProxiedPlayer player, String msg) {
		player.sendMessage(prefix + msg);
	}
	
	public static void send(Player player, String msg) {
		player.sendMessage(prefix + msg);
	}
	
    public static TextComponent createHoverText(String text, String hovertext) {
		TextComponent t1 = new TextComponent();
		t1.setText(text);
		t1.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder(hovertext).create()));
		return t1;
    }
    
    public static TextComponent createHoverText(TextComponent t1, String hovertext) {
		t1.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder(hovertext).create()));
		return t1;
    }
    
    public static TextComponent createCommandHoverText(String text, String hovertext, String command) {
    	TextComponent t1 = new TextComponent();
    	t1.setText(text);
    	t1.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder(hovertext).create()));
    	t1.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, command));
    	return t1;
    	
    }
    
    public static TextComponent createCommandHoverText(TextComponent t1, String hovertext, String command) {
    	t1.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder(hovertext).create()));
    	t1.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, command));
    	return t1;
    	
    }
    
    public static TextComponent createCommandFillText(String text, String command) {
    	TextComponent t1 = new TextComponent();
    	t1.setText(text);
    	t1.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND, command));
    	return t1;
    }
    
    public static TextComponent createCommandFillHoverText(String text, String hovertext, String command) {
    	TextComponent t1 = new TextComponent();
    	t1.setText(text);
    	t1.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder(hovertext).create()));
    	t1.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND, command));
    	return t1;
    }
    
    public static TextComponent stackComponent(TextComponent t1, TextComponent... t2) {
    	for (TextComponent t : t2) {
    		t1.addExtra(t);
    	}
    	return t1;
    }
	
}
