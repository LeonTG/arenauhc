package com.thetonyk.arena.Commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.thetonyk.arena.Main;
import com.thetonyk.arena.Utils.DatabaseUtils;
import com.thetonyk.arena.Utils.WorldUtils;

public class BorderCommand implements CommandExecutor, TabCompleter {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("arena.border")) {
			
			sender.sendMessage(Main.NO_PERMS);
			return true;
			
		}
		
		if (args.length == 0) {
			
			//Incoming border game size
			return true;
			
		}
		
		if (!sender.hasPermission("uhc.setborder")) {
			
			sender.sendMessage(Main.NO_PERMS);
			return true;
			
		}
		
		if (!WorldUtils.exist(args[0])) {
			
			sender.sendMessage(Main.PREFIX + "The world '§6" + args[0] + "§7' doesn't exist.");
			return true;
			
		}
		
		if (args.length == 1) {
			
			int radius;
			
			try {
				
				Statement sql = DatabaseUtils.getConnection().createStatement();
				ResultSet worldDB = sql.executeQuery("SELECT * FROM worlds WHERE name='" + args[0] + "';");
				
				worldDB.next();
				
				radius = worldDB.getInt("size");
				
			} catch (SQLException e) {
				
				Main.arena.getLogger().severe("§7[PregenCommand] §cError to fetch informations of world §6" + args[0] + "§c in DB.");
				sender.sendMessage(Main.PREFIX + "Error to fetch data of '§6" + args[1] + "§7'.");
				return true;
				
			}
			
			sender.sendMessage(Main.PREFIX + "Size of world '§6" + args[0] + "§7': §a" + radius + "§7x§a" + radius + "§7.");
			return true;
			
		}
		
		int size;
		
		try {
			
			size = Integer.parseInt(args[1]);
			
		} catch (Exception e) {
			
			sender.sendMessage(Main.PREFIX + "Please enter a valid size of world.");
			return true;
			
		}
		
		if (size > 10000 || size < 100) {
			
			sender.sendMessage(Main.PREFIX + "Please enter a valid size of world.");
			return true;
			
		}
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			sql.executeUpdate("UPDATE worlds SET size = '" + args[1] + "' WHERE name = '" + args[0] + "';");
			
			if (Bukkit.getWorld(args[0]) != null) {
				
				Bukkit.getWorld(args[0]).getWorldBorder().setSize(size);
				
			}
			
			Bukkit.broadcastMessage(Main.PREFIX + "Size of world '§6" + args[0] + "§7' set to: §a" + size + "§7x§a" + size + "§7.");
			return true;
			
		} catch (SQLException e) {
			
			Main.arena.getLogger().severe("§7[BorderCommand] §cError to update size of world §6" + args[0] + "§c in database.");
			sender.sendMessage(Main.PREFIX + "Error to update size of world §6" + args[0] + "§7 in database.");
			return true;
			
		}
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		
		List<String> tabCompletions = new ArrayList<String>();
		
		if (!sender.hasPermission("uhc.setborder")) {
			
			return null;
			
		}
		
		if (args.length == 1) {
			
			for (World world : Bukkit.getWorlds()) {
				
				tabCompletions.add(world.getName());
				
			}
			
			return tabCompletions;
			
		}
		
		return null;
		
	}

}
