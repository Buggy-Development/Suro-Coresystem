package de.TheHolyException.suro.utils;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;

public class TextManager {
	
	private TextComponent componment;
	
	public TextManager(String text) {
		componment = new TextComponent(text);
	}
	
	public TextManager(TextComponent textc) {
		componment = textc;
	}
	
	public TextManager addClickCommand(String command) {
		componment.setClickEvent(new ClickEvent(Action.RUN_COMMAND, command));
		return this;
	}
	
	public TextManager addClickCommandSuggest(String command) {
		componment.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, command));
		return this;
	}
	
	public TextManager addHoverEvent(String message) {
		componment.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(message).create()));
		return this;
	}
	
	public TextManager attachOtherComponent(TextComponent textcomponent) {
		componment.addExtra(textcomponent);
		return this;
	}
	
	public TextComponent build() {
		return componment;
	}

}
