package com.thetonyk.arena.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.thetonyk.arena.Arena;
import com.thetonyk.arena.Main;

public class UtilsCommand implements CommandExecutor, TabCompleter {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!sender.hasPermission("arena.utils")) {
			
			sender.sendMessage(Main.NO_PERMS);
    		return true;
    		
		}
		
		if (args.length < 1) {
			
			sender.sendMessage(Main.PREFIX + "Usage of /utils:");
			sender.sendMessage("§8⫸ §6/utils clear §8- §7Clear the chat.");
			sender.sendMessage("§8⫸ §6/utils mute §8- §7Mute the chat.");
			return true;
			
		}
		
		if (args[0].equalsIgnoreCase("clear")) {
			
			for (int i = 0; i < 150; i++) {
				
				Bukkit.broadcastMessage("§r");
				
			}
			
			Bukkit.broadcastMessage(Main.PREFIX + "The chat has been cleared.");
			return true;
			
		}
		else if (args[0].equalsIgnoreCase("mute")) {
			
			if (Arena.mute) {
				
				Arena.mute = false;
				Bukkit.broadcastMessage(Main.PREFIX + "The chat has been enabled.");
				return true;
				
			}
			
			Arena.mute = true;
			Bukkit.broadcastMessage(Main.PREFIX + "The chat has been disabled.");
			
		}
		
		return true;
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		
		return null;
		
	}

}
