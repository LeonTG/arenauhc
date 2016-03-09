package com.thetonyk.arena.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.thetonyk.arena.Main;

public class HelpCommand implements CommandExecutor, TabCompleter {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!sender.hasPermission("arena.help")) {
			
			sender.sendMessage(Main.NO_PERMS);
    		return true;
    		
		}
		
		sender.sendMessage(Main.PREFIX + "Useful commands:");
		sender.sendMessage("§8⫸ §6/lag §8- §7See server performance.");
		sender.sendMessage("§8⫸ §6/lobby §8- §7Back to lobby.");
		
		return true;
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		
		return null;
		
	}

}
