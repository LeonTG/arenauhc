package com.thetonyk.arena.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.thetonyk.arena.Main;

public class MsCommand implements CommandExecutor, TabCompleter {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!sender.hasPermission("arena.ms")) {
			
			sender.sendMessage(Main.NO_PERMS);
    		return true;
    		
		}
		
		sender.sendMessage(Main.PREFIX + "Press tab to see your ping.");
		
		return true;
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		
		return null;
		
	}

}
