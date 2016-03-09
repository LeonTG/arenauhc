package com.thetonyk.arena.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.thetonyk.arena.Main;

public class FlyCommand implements CommandExecutor, TabCompleter {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!sender.hasPermission("arena.fly.command")) {
			
			sender.sendMessage(Main.NO_PERMS);
    		return true;
    		
		}
		
		if (args.length < 1) {
			
			sender.sendMessage(Main.PREFIX + "Usage: /fly <player>");
			return true;
			
		}
		
		if (Bukkit.getPlayer(args[0]) != null && Bukkit.getPlayer(args[0]).isOnline()) {
		
				
			if (Bukkit.getPlayer(args[0]).getAllowFlight()) {
				
				Bukkit.getPlayer(args[0]).setAllowFlight(false);
				Bukkit.getPlayer(args[0]).setFlying(false);
				sender.sendMessage(Main.PREFIX + "The player '§6" + Bukkit.getPlayer(args[0]).getName() + "§7' can't no longer fly.");
				Bukkit.getPlayer(args[0]).sendMessage(Main.PREFIX + "You are no longer able to fly.");
				return true;
				
			}
			
			Bukkit.getPlayer(args[0]).setAllowFlight(true);
			sender.sendMessage(Main.PREFIX + "The player '§6" + Bukkit.getPlayer(args[0]).getName() + "§7' can now fly.");
			Bukkit.getPlayer(args[0]).sendMessage(Main.PREFIX + "You are now able to fly.");
			return true;
			
		}
		
		sender.sendMessage(Main.PREFIX + "The player '§6" + args[0] + "§7' is not online.");
		return true;
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		
		return null;
		
	}

}
