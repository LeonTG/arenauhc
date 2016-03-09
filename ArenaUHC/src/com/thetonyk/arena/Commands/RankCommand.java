package com.thetonyk.arena.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.thetonyk.arena.Main;
import com.thetonyk.arena.Utils.PlayerUtils;
import com.thetonyk.arena.Utils.PlayerUtils.Rank;

public class RankCommand implements CommandExecutor, TabCompleter {
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			
		if (!sender.hasPermission("arena.rank")) {
			
			sender.sendMessage(Main.NO_PERMS);
			return true;
			
		}
		
		if (args.length < 1) {
			
			sender.sendMessage(Main.PREFIX + "Usage: /rank <player> <rank>");
			return true;
			
		}
		
		if (args.length < 2) {
				
			sender.sendMessage(Main.PREFIX + PlayerUtils.getRanks());
			return true;
				
		}
				
		OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
		
		if (!PlayerUtils.exist(player)) {
			
			sender.sendMessage(Main.PREFIX + "This player is not known on this server");
			return true;
		
		}
			
		Rank rank = null;
		
		try {
			
			rank = Rank.valueOf(args[1].toUpperCase());
			
		} catch (Exception e) {
			
			sender.sendMessage(Main.PREFIX + PlayerUtils.getRanks());
			
		}
		
		if (rank == null) {
			
			sender.sendMessage(Main.PREFIX + PlayerUtils.getRanks());
			return true;
			
		}
			
		PlayerUtils.setRank(player.getName(), rank);
		PlayerUtils.updateNametag(player.getName());
		
		if (player.isOnline() && sender.getName() != player.getName()) {
				
				player.getPlayer().sendMessage(Main.PREFIX + "Your rank was set to '§6" + rank.toString().toLowerCase() + "§7'.");
			
		}
		
		sender.sendMessage(Main.PREFIX + "The rank of '§6" + player.getName() + "§7' has been set to '§6" + rank.toString().toLowerCase() + "§7'.");
		
		return true;
		
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		
		List<String> tabCompletions = new ArrayList<String>();
			
		if (args.length == 2) {
				
			for (Rank rank : Rank.values()) {
				tabCompletions.add(rank.name().toLowerCase());
			}
				
			return tabCompletions;
				
		}
		
		return null;
		
	}

}
