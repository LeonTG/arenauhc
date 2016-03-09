package com.thetonyk.arena.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.thetonyk.arena.Main;
import com.thetonyk.arena.Utils.PlayerUtils;

public class PCommand implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("arena.private")) {
			
			sender.sendMessage(Main.NO_PERMS);
			return true;
			
		}
		
		if (args.length < 1) {
			
			sender.sendMessage(Main.PREFIX + "Usage: /p <message>");
			return true;
			
		}
		
		StringBuilder message = new StringBuilder();
		
		for (int i = 0; i < args.length; i++) {
			message.append(args[i] + " ");
		}
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			if (player.hasPermission("arena.private")) {
		
				player.sendMessage("§cStaff §8| " + PlayerUtils.getRank(sender.getName()).getPrefix() + "§7" + sender.getName() + " §8⫸ §f" + message);
				
			}
		
		}
		
		return true;
		
	}

}

