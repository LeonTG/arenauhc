package com.thetonyk.arena.Commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.thetonyk.arena.Main;

public class WhitelistCommand implements CommandExecutor {
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("arena.whitelist.add")) {
			
			sender.sendMessage(Main.NO_PERMS);
			return true;
			
		}
		
		if (args.length > 0) {
			
			if (args[0].equalsIgnoreCase("add")) {
				
				Bukkit.getOfflinePlayer(args[1]).setWhitelisted(true);
				Bukkit.broadcastMessage(Main.PREFIX + "The player '§6" + Bukkit.getOfflinePlayer(args[1]).getName() + "§7' has been whitelisted.");
				return true;
				
			}
			else if (args[0].equalsIgnoreCase("remove")) {
				
				if (!Bukkit.getOfflinePlayer(args[1]).isWhitelisted()) {
					
					sender.sendMessage(Main.PREFIX + "The player '§6" + Bukkit.getOfflinePlayer(args[1]).getName() + "§7' is not whitelisted.");
					return true;
					
				}
				
				Bukkit.getOfflinePlayer(args[1]).setWhitelisted(false);
				Bukkit.broadcastMessage(Main.PREFIX + "The player '§6" + Bukkit.getOfflinePlayer(args[1]).getName() + "§7' is not longer whitelisted.");
				return true;
				
			}
			else if (args[0].equalsIgnoreCase("status")) {
				
				if (args.length < 2) {
					
					String status = Bukkit.hasWhitelist() ? "on" : "off";
					sender.sendMessage(Main.PREFIX + "The whitelist is §6" + status + "§7.");
					sender.sendMessage(Main.PREFIX + "There are §6" + Bukkit.getWhitelistedPlayers().size() + "§7 whitelisted players.");
					return true;
					
				}
				
				String status = Bukkit.getOfflinePlayer(args[1]).isWhitelisted() ? "is" : "is not";
					
				sender.sendMessage(Main.PREFIX + "The player '§6" + Bukkit.getOfflinePlayer(args[1]).getName() + "§7' " + status + " whitelisted.");
				return true;
				
			}
			else if (args[0].equalsIgnoreCase("list")) {
				
				if (Bukkit.getWhitelistedPlayers().size() <= 0) {
					
					sender.sendMessage(Main.PREFIX + "There are no whitelised players.");
					return true;
					
				}
				
				sender.sendMessage(Main.PREFIX + "List of whitelisted players:");
				
				for (OfflinePlayer player : Bukkit.getWhitelistedPlayers()) {
					
					sender.sendMessage("§8⫸ §7'§6" + player.getName() + "§7'");
					
				}
				
				sender.sendMessage(Main.PREFIX + "§6" + Bukkit.getWhitelistedPlayers().size() + "§7 whitelisted players listed.");
				return true;
				
			}
			if (sender.hasPermission("arena.whitelist")) {
				
				if (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("off")) {
					
					Boolean setWhitelist = args[0].equalsIgnoreCase("on") ? true : false;
					
					if (args[0].equalsIgnoreCase("on") && Bukkit.hasWhitelist()) {
						
						sender.sendMessage(Main.PREFIX + "The whitelist is already §a" + args[0].toLowerCase() + "§7.");
						return true;
						
					}
					
					if (args[0].equalsIgnoreCase("off") && !Bukkit.hasWhitelist()) {
						
						sender.sendMessage(Main.PREFIX + "The whitelist is already §a" + args[0].toLowerCase() + "§7.");
						return true;
						
					}

					Bukkit.setWhitelist(setWhitelist);
					Bukkit.broadcastMessage(Main.PREFIX + "The whitelist is now §a" + args[0].toLowerCase() + "§7.");
					return true;
					
				}
				else if (args[0].equalsIgnoreCase("all")) {
					
					for (Player player : Bukkit.getOnlinePlayers()) {
						
						player.setWhitelisted(true);
						
					}
					
					Bukkit.broadcastMessage(Main.PREFIX + "All players has been whitelisted.");
					return true;
					
				}
				else if (args[0].equalsIgnoreCase("clear")) {
					
					if (Bukkit.getWhitelistedPlayers().size() <= 0) {
						
						sender.sendMessage(Main.PREFIX + "There are no whitelised players.");
						return true;
						
					}
					
					for (OfflinePlayer player : Bukkit.getWhitelistedPlayers()) {
						
						player.setWhitelisted(false);
						
					}
					
					Bukkit.broadcastMessage(Main.PREFIX + "The whitelist has been cleared.");
					return true;
					
				}
				
			}
			
		}
		
		sender.sendMessage(Main.PREFIX + "Usage of /whitelist:");
		sender.sendMessage("§8⫸ §6/whitelist add <player> §8- §7Add a player.");
		sender.sendMessage("§8⫸ §6/whitelist remove <player> §8- §7Remove a player.");
		
		if (sender.hasPermission("uhc.whitelist")) {
			
			sender.sendMessage("§8⫸ §6/whitelist on|off §8- §7Enable/Disable the whitelist.");
			sender.sendMessage("§8⫸ §6/whitelist all §8- §7Whitelist all players.");
			sender.sendMessage("§8⫸ §6/whitelist clear §8- §7Clear the whitelist.");
			
		}
		
		sender.sendMessage("§8⫸ §6/whitelist status [player] [player] §8- §7See status of whitelist.");
		sender.sendMessage("§8⫸ §6/whitelist list §8- §7List whitelisted players.");
		return true;
		
	}

}

