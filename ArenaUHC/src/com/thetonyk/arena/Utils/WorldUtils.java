package com.thetonyk.arena.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import com.thetonyk.arena.Main;

public class WorldUtils {

	public static String loadWorld(String world) {
			
		if (!exist(world)) {
			
			return "§cThe world §6" + world + "§cdoesn't exist.";
			
		} else {
			
			Environment environment = Environment.NORMAL;
			long seed = -89417720380802761l;
			WorldType type = WorldType.NORMAL;
			
			try {
				
				Statement sql = DatabaseUtils.getConnection().createStatement();
				ResultSet worldDB = sql.executeQuery("SELECT * FROM worlds WHERE name='" + world + "';");
				
				worldDB.next();
				
				environment = Environment.valueOf(worldDB.getString("environment"));
				seed = worldDB.getLong("seed");
				type = WorldType.valueOf(worldDB.getString("type"));
				
			} catch (SQLException e) {
				
				Main.arena.getLogger().severe("§7[WorldUtils] §cError to fetch informations of world §6" + world + "§c in DB.");
				return "§cError to fetch informations of world §6" + world + "§c in DB.";
				
			}
			
			WorldCreator worldCreator = new WorldCreator(world);
			worldCreator.environment(environment);
			worldCreator.generateStructures(true);
			worldCreator.generatorSettings("{\"useCaves\":false,\"useStrongholds\":false,\"useVillages\":false,\"useMineShafts\":false,\"useTemples\":false,\"useRavines\":false,\"useMonuments\":false,\"graniteSize\":1,\"graniteCount\":0,\"graniteMinHeight\":0,\"graniteMaxHeight\":0,\"dioriteSize\":1,\"dioriteCount\":0,\"dioriteMinHeight\":0,\"dioriteMaxHeight\":0,\"andesiteSize\":1,\"andesiteCount\":0,\"andesiteMinHeight\":0,\"andesiteMaxHeight\":0}");
			worldCreator.seed(seed);
			worldCreator.type(type);
			
			World newWorld = worldCreator.createWorld();
			newWorld.setDifficulty(Difficulty.HARD);
			newWorld.save();
			
			return null;
		
		}
		
	}
	
	public static String unloadWorld(String world) {
		
		if (Bukkit.getWorld(world) == null) {
			
			Main.arena.getLogger().severe("§7[WorldUtils] §cThe world §6" + world + "§c can't be unloaded.");
			return "§cThe world §6" + world + "§c can't be unloaded.";
			
		}
		
		if (!Bukkit.unloadWorld(world, true)) {
			
			Main.arena.getLogger().severe("§7[WorldUtils] §cError to unload world §6" + world + "§c.");
			return "§cError to unload world §6" + world + "§c.";
			
		} else {
			
			return null;
			
		}
		
	}
	
	public static String deleteWorld(String world) {
		
		if (!exist(world)) {
			
			Main.arena.getLogger().severe("§7[WorldUtils] §cThe world §6" + world + "§c doesn't exist.");
			return "§cThe world §6" + world + "§c doesn't exist.";
			
		}
			
		if (Bukkit.getWorld(world) == null) {
			
			Main.arena.getLogger().severe("§7[WorldUtils] §cThe world §6" + world + "§c can't be unloaded.");
			return "§cThe world §6" + world + "§c can't be unloaded.";
			
		}
				
		World oldWorld = Bukkit.getWorld(world);

		for (Player player : oldWorld.getPlayers()) {
			player.teleport(Bukkit.getWorld("lobby").getSpawnLocation());
		}
		
		String potentialError = unloadWorld(world);
		
		if (potentialError != null) {
			return potentialError;
		}
			
		if (FileUtils.delete(oldWorld.getWorldFolder())) {
			
			try {
				
				Statement sql = DatabaseUtils.getConnection().createStatement();
				sql.executeUpdate("DELETE FROM worlds WHERE name='" + world + "';");
				return null;
				
			} catch (SQLException e) {
				
				Main.arena.getLogger().severe("§7[WorldUtils] §cError to delete world §6" + world + "§c in database.");
				return "§7Error to delete world §6" + world + "§7 in the database.";
				
			}
			
		} else {
			
			Main.arena.getLogger().severe("§7[WorldUtils] §cThe folder of world §6" + world + "§c can't be deleted.");
			return "§cThe folder of world §6" + world + "§c can't be deleted.";
			
		}
		
	}
	

	public static Boolean exist(String world) {
		
		Boolean exist = false;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet name = sql.executeQuery("SELECT * FROM worlds WHERE name='" + world + "';");
			
			if (name.next()) {
				exist = true;
			} else {
				exist = false;
			}
			
		} catch (SQLException e) {
			
			Main.arena.getLogger().severe("§7[WorldUtils] §cError to check if world §6" + world + "§c exist.");
			return false;
			
		}
		
		return exist;
		
	}
	
	public static void loadAllWorlds() {

		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet worlds = sql.executeQuery("SELECT * FROM worlds;");
			
			while (worlds.next()) {
				
				try {
				
					loadWorld(worlds.getString("name"));
					Bukkit.getWorld(worlds.getString("name")).setPVP(true);
					Bukkit.getWorld(worlds.getString("name")).setTime(6000);
					
				} catch (Exception e) {
					
					Main.arena.getLogger().severe("§7[WorldUtils] §cError to load world §6" + worlds.getString("name") + "§c.");
					e.printStackTrace();
					
				}
				
			}
			
		} catch (SQLException e) {
			
			Main.arena.getLogger().severe("§7[WorldUtils] §cError to fetch all worlds.");
			
		}
		
		Bukkit.getWorld("lobby").setPVP(false);
		Bukkit.getWorld("lobby").setTime(18000);
		
	}
	
	public static String createWorld(String world, Environment environment, long seed, WorldType type, int radius) {
		
		WorldCreator worldCreator = new WorldCreator(world);
		
		worldCreator.environment(environment);
		worldCreator.generateStructures(true);
		worldCreator.generatorSettings("{\"useCaves\":false,\"useStrongholds\":false,\"useVillages\":false,\"useMineShafts\":false,\"useTemples\":false,\"useRavines\":false,\"useMonuments\":false,\"graniteSize\":1,\"graniteCount\":0,\"graniteMinHeight\":0,\"graniteMaxHeight\":0,\"dioriteSize\":1,\"dioriteCount\":0,\"dioriteMinHeight\":0,\"dioriteMaxHeight\":0,\"andesiteSize\":1,\"andesiteCount\":0,\"andesiteMinHeight\":0,\"andesiteMaxHeight\":0}");
		worldCreator.seed(seed);
		worldCreator.type(type);
		
		World newWorld = worldCreator.createWorld();
		
		newWorld.setDifficulty(Difficulty.HARD);
		newWorld.setSpawnLocation(0, 200, 0);
		newWorld.getWorldBorder().setSize(radius);
		newWorld.getWorldBorder().setCenter(0, 0);
		newWorld.getWorldBorder().setDamageBuffer(0);
		newWorld.getWorldBorder().setDamageAmount(1);
		newWorld.getWorldBorder().setWarningDistance(15);
		newWorld.getWorldBorder().setWarningTime(1);
		newWorld.setGameRuleValue("spectatorsGenerateChunks", "false");
		newWorld.setGameRuleValue("mobGriefing", "false");
		newWorld.setGameRuleValue("doMobSpawning", "false");
		newWorld.setGameRuleValue("doMobLoot", "false");
		newWorld.setGameRuleValue("doFireTick", "false");
		newWorld.setGameRuleValue("doEntityDrops", "false");
		newWorld.setGameRuleValue("doDaylightCycle", "false");
		newWorld.setTime(6000);
		
		newWorld.save();
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			sql.executeUpdate("INSERT INTO worlds (`name`, `environment`, `seed`, `type`, `size`) VALUES ('" + world + "', '" + environment.name() + "', '" + seed + "', '" + type.name() + "', '" + radius + "');");
		
		} catch (SQLException e) {
			
			Main.arena.getLogger().severe("§7[WorldUtils] §cError to insert new world §6" + world + "§c.");
			return "§cError to insert new world §6" + world + "§c.";
			
		}
		
		return null;
		
	}
	
	public static int getSize (String world) {
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet size = sql.executeQuery("SELECT size FROM worlds WHERE name='" + world + "';");
			
			size.next();
			
			return size.getInt("size");
			
		} catch (SQLException e) {
			
			Main.arena.getLogger().severe("§7[WorldUtils] §cError to get size if world §6" + world + "§c.");
			return 0;
			
		}
		
	}
	
}
