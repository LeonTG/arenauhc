package com.thetonyk.arena.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.thetonyk.arena.Main;

public class BroadcastCommand implements CommandExecutor, TabCompleter {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!sender.hasPermission("arena.broadcast")) {
			
			sender.sendMessage(Main.NO_PERMS);
    		return true;
    		
		}
				
		if (args.length < 1) {
			
			sender.sendMessage(Main.PREFIX + "Usage: /broadcast <message>");
			return true;
			
		}
			
		StringBuilder message = new StringBuilder();
		
		for (int i = 0; i < args.length; i++) {
			message.append(args[i] + " ");
		}
		
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(Main.PREFIX + "§b§l" + message.toString());
		Bukkit.broadcastMessage(" ");
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
		}
		
		return true;
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		
		return null;
		
	}

}
