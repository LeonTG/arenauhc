package com.thetonyk.arena.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.thetonyk.arena.Main;
import com.thetonyk.arena.Inventories.ArenasInventory;
import com.thetonyk.arena.Listeners.InventoryListener;
import com.thetonyk.arena.Utils.ArenaUtils;
import com.thetonyk.arena.Utils.DisplayUtils;
import com.thetonyk.arena.Utils.PlayerUtils;
import com.thetonyk.arena.Utils.WorldUtils;

public class ArenaCommand implements CommandExecutor, TabCompleter {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			
		if (!sender.hasPermission("arena.arena")) {
			
			sender.sendMessage(Main.NO_PERMS);
			return true;
			
		}
			
		if (args.length < 1) {
			
			sender.sendMessage(Main.PREFIX + "Usage of /arena:");
			sender.sendMessage("§8⫸ §6/arena create §8- §7Create an arena.");
			sender.sendMessage("§8⫸ §6/arena delete §8- §7Delete an arena.");
			sender.sendMessage("§8⫸ §6/arena reload §8- §7Reload all arenas.");
			sender.sendMessage("§8⫸ §6/arena list §8- §7List all arenas.");
			sender.sendMessage("§8⫸ §6/arena kits §8- §7Change kits of arenas.");
			sender.sendMessage("§8⫸ §6/arena enable §8- §7Enable a arena.");
			sender.sendMessage("§8⫸ §6/arena disable §8- §7Disable a arena.");
			return true;
			
		}
					
		if (args[0].equalsIgnoreCase("create")) {
			
			if (args.length < 3) {
				
				sender.sendMessage(Main.PREFIX + "Usage: /arena create <id> <world> <slot> <name>");
				return true;
				
			}
				
			if (ArenaUtils.getArenas().contains(args[1])) {
				
				sender.sendMessage(Main.PREFIX + "The arena '§6" + args[1] + "§7' is already created.");
				return true;
				
			}
			
			if (args[1].equalsIgnoreCase("arena")) {
				
				sender.sendMessage(Main.PREFIX + "The arena '§6" + args[1] + "§7' is already created.");
				return true;
				
			}
			
			if (!WorldUtils.exist(args[2])) {
				
				sender.sendMessage(Main.PREFIX + "The world '§6" + args[2] + "§7' doesn't exist.");
				return true;
				
			}
			
			for (String id : ArenaUtils.getArenas()) {
				
				if (ArenaUtils.getWorld(id).equalsIgnoreCase(args[2])) {
					
					sender.sendMessage(Main.PREFIX + "The world '§6" + args[2] + "§7' is already used?");
					return true;
					
				}
				
			}
			
			int slot;
			
			try {
				
				slot = Integer.parseInt(args[3]);
				
			} catch (Exception e) {
				
				sender.sendMessage(Main.PREFIX + "The slot is not valid.");
				return true;
				
			}
			
			if (slot < 0 && slot > 8) {
				
				sender.sendMessage(Main.PREFIX + "Please specify a slot between 0 and 9");
				return true;
				
			}
			
			for (String id : ArenaUtils.getArenas()) {
				
				if (ArenaUtils.getSlot(id) == slot) {
					
					sender.sendMessage(Main.PREFIX + "The slot §a" + slot + "§7is already used.");
					return true;
					
				}
				
			}
			
			StringBuilder name = new StringBuilder();
			
			for (int i = 4; i < args.length; i++) {
				
				name.append(args[i] + " ");
				
			}
			
			String nameArena = "§b§l" + name.substring(0, name.length() - 1);
			
			sender.sendMessage(Main.PREFIX + "Creation of arena '§6" + args[1] + "§7'...");
			sender.sendMessage("§8⫸ §7Name: §a" + nameArena);
			sender.sendMessage("§8⫸ §7World: §a" + args[2]);
			sender.sendMessage("§8⫸ §7Slot: §a" + slot);
			
			ArenaUtils.create(args[1], args[2], slot, nameArena);
			
			sender.sendMessage(Main.PREFIX + "The arena '§6" + args[1] + "§7' has been created.");
			
			Bukkit.getPlayer(sender.getName()).setGameMode(GameMode.CREATIVE);
			PlayerUtils.clearInventory(Bukkit.getPlayer(sender.getName()));
			Bukkit.getPlayer(sender.getName()).closeInventory();
			DisplayUtils.sendTitle(Bukkit.getPlayer(sender.getName()), "", "§6Open your inventory to set the kit", 2, 30, 2);
			InventoryListener.editKits.put(Bukkit.getPlayer(sender.getName()).getUniqueId(), args[1]);
			Bukkit.getPlayer(sender.getName()).teleport(new Location(Bukkit.getWorld("lobby"), 500, 1.5, 0));
		
			return true;
				
		}			
		else if (args[0].equalsIgnoreCase("delete")) {
			
			if (args.length < 2) {
				
				sender.sendMessage(Main.PREFIX + "Usage: /arena delete <arena>");
				return true;
				
			}
			
			if (!ArenaUtils.getArenas().contains(args[1])) {
				
				sender.sendMessage(Main.PREFIX + "The arena '§6" + args[1] + "§7' doesn't exist.");
				return true;
				
			}
			
			ArenaUtils.delete(args[1]);
			
			sender.sendMessage(Main.PREFIX + "The arena '§6" + args[1] + "§7' has been deleted.");
			
			return true;							
						
		}
		else if (args[0].equalsIgnoreCase("reload")) {
						
			ArenaUtils.reload();
										
			sender.sendMessage(Main.PREFIX + "All arenas are been reloaded.");
									
						
		}
		else if (args[0].equalsIgnoreCase("list")) {
						
			sender.sendMessage(Main.PREFIX + "Existing arena:");
			
			int i = 0;
						
			for (String id : ArenaUtils.getArenas()) {
				
				sender.sendMessage("§8⫸ §6" + id + " §8- §7" + ArenaUtils.getName(id) + "§7.");
				i++;
				
			}
			
			sender.sendMessage(Main.PREFIX + "§a" + i + " §7arenas listed.");
						
		}
		else if (args[0].equalsIgnoreCase("kits")) {
			
			Bukkit.getPlayer(sender.getName()).openInventory(ArenasInventory.getArenas("Set kits"));
			return true;
			
		}
		else if (args[0].equalsIgnoreCase("enable")) {
			
			if (args.length < 2) {
				
				sender.sendMessage(Main.PREFIX + "Usage: /arena enable <arena>");
				return true;
				
			}
			
			if (!ArenaUtils.getArenas().contains(args[1])) {
				
				sender.sendMessage(Main.PREFIX + "The arena '§6" + args[1] + "§7' doesn't exist.");
				return true;
				
			}
			
			if (ArenaUtils.isEnabled(args[1])) {
				
				sender.sendMessage(Main.PREFIX + "The arena '§6" + args[1] + "§7' is already enabled.");
				return true;
				
			}
			
			ArenaUtils.enable(args[1]);
			
			Bukkit.broadcastMessage(Main.PREFIX + "The arena '§6" + ArenaUtils.getName(args[1]) + "§7' is now enabled.");
			return true;
			
		}
		else if (args[0].equalsIgnoreCase("disable")) {
			
			if (args.length < 2) {
				
				sender.sendMessage(Main.PREFIX + "Usage: /arena disable <arena>");
				return true;
				
			}
			
			if (!ArenaUtils.getArenas().contains(args[1])) {
				
				sender.sendMessage(Main.PREFIX + "The arena '§6" + args[1] + "§7' doesn't exist.");
				return true;
				
			}
			
			if (!ArenaUtils.isEnabled(args[1])) {
				
				sender.sendMessage(Main.PREFIX + "The arena '§6" + args[1] + "§7' is already disabled.");
				return true;
				
			}
			
			ArenaUtils.disable(args[1]);
			
			Bukkit.broadcastMessage(Main.PREFIX + "The arena '§6" + ArenaUtils.getName(args[1]) + "§7' is now disabled.");
			return true;
			
		}
		
		return true;
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		
		List<String> tabCompletions = new ArrayList<String>();
		
		return tabCompletions;
		
	}

}
