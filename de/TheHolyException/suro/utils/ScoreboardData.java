package de.TheHolyException.suro.utils;

import java.util.ArrayList;

import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardData {
	
	private String displayname;
	private ArrayList<String> lines;
	private Scoreboard board;
	
	public ScoreboardData(String displayname, ArrayList<String> lines) {
		this.displayname = displayname;
		this.lines = lines;
	}
	
	public String getDisplayname() {
		return displayname;
	}
	
	public ArrayList<String> getLines() {
		return lines;
	}

	public void setBoard(Scoreboard board) {
		this.board = board;
	}
	
	public Scoreboard getBoard() {
		return board;
	}
	
}
