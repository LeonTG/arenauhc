package com.thetonyk.arena.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.thetonyk.arena.Main;

public class GamemodeCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			
		if (!sender.hasPermission("arena.gamemode")) {
			
			sender.sendMessage(Main.NO_PERMS);
			return true;
			
		}
			
		if (args.length < 1) {
			
			sender.sendMessage(Main.PREFIX + "Usage: /gamemode <gamemode> [player]");
			return true;
			
		}
						
		GameMode gamemode = null;
		
		try {
			
			int gamemodeNumber = Integer.parseInt(args[0]);
			
			if (gamemodeNumber == 0) {
				gamemodeNumber = 1;
			}
			else if (gamemodeNumber == 1) {
				gamemodeNumber = 0;
			}
			
			GameMode[] gamemodeList = GameMode.values();
			gamemode = gamemodeList[gamemodeNumber];
			
		} catch (Exception e) {
			
			String gamemodeString = args[0].toUpperCase();
			gamemode = GameMode.valueOf(gamemodeString);
			
		}
		
		if (gamemode == null) {
			
			sender.sendMessage(Main.PREFIX + "Usage: /gamemode <gamemode> [player]");
			return true;
			
		}
			
		if (args.length > 1) {
			
			Player player = Bukkit.getPlayer(args[1]);
			
			if (player == null) {
				
				sender.sendMessage(Main.PREFIX + "§6" + args[1] + " §7is not online.");
				
			} else {
				
				if (sender.getName() != player.getName()) {
					
					sender.sendMessage(Main.PREFIX + "The gamemode of '§6" + player.getName() + "§7' has been set to §6" + gamemode.name().toLowerCase() + "§7.");
					
				}
				
				player.sendMessage(Main.PREFIX + "Your gamemode was set to §6" + gamemode.name().toLowerCase() + "§7.");
				
				player.setGameMode(gamemode);
				
			}
			
			return true;
			
		}
			
		Player player = Bukkit.getPlayer(sender.getName());
		
		player.sendMessage(Main.PREFIX + "Your gamemode was set to §6" + gamemode.name().toLowerCase() + "§7.");
		
		player.setGameMode(gamemode);
			
		return true;
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
			
		List<String> tabCompletions = new ArrayList<String>();
		
		if (args.length == 1) {
		
			tabCompletions.add("survival");
			tabCompletions.add("creative");
			tabCompletions.add("adventure");
			tabCompletions.add("spectator");
			return tabCompletions;
			
		} 
		
		return null;
		
	}

}
