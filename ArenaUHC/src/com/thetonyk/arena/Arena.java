package com.thetonyk.arena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public class Arena {
	
	public static Boolean mute = false;
	public static List<Location> blocks = new ArrayList<Location>();
	public static Map<Player, List<Location>> playerBlocks = new HashMap<Player, List<Location>>();
	public static List<Location> water = new ArrayList<Location>();
	public static Map<Player, Long> joinTime = new HashMap<Player, Long>();
	public static Map<Player, Scoreboard> scoreboard = new HashMap<Player, Scoreboard>();
	public static Map<Player, Integer> kills = new HashMap<Player, Integer>();
	public static Map<Player, Integer> death = new HashMap<Player, Integer>();
	public static Map<Player, Integer> killstreak = new HashMap<Player, Integer>();
	public static Map<Player, Double> longshot = new HashMap<Player, Double>();
	public static Map<Player, Integer> currentKillstreak = new HashMap<Player, Integer>();
	public static List<Player> nodamages = new ArrayList<Player>();

}
