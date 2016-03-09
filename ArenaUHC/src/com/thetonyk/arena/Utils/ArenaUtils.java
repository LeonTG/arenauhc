package com.thetonyk.arena.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.arena.Arena;
import com.thetonyk.arena.Main;

public class ArenaUtils {

	public static void create(String id, String world, int slot, String name) {
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			sql.executeUpdate("INSERT INTO arena (`id`, `name`, `slot`, `world`, `kit`, `state`, `size`) VALUES ('" + id + "', '" + name + "', " + slot + ", '" + world + "', '', 0, 100);");
		
		} catch (SQLException e) {
			
			Main.arena.getLogger().severe("§7[ArenaUtils] §cError to insert new arena §6" + id + "§c.");
			return;
			
		}
		
		for (Player player : Bukkit.getWorld("lobby").getPlayers()) {
			
			if (player.getGameMode() == GameMode.ADVENTURE) {
			
				ArenaUtils.placeItems(player);
			
			}
		
		}
		
	}
	
	public static void reload() {
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet arenas = sql.executeQuery("SELECT * FROM arena;");
			
			while (arenas.next()) {
				
				WorldUtils.unloadWorld(arenas.getString("world"));
				WorldUtils.loadWorld(arenas.getString("world"));
				
			}
			
		} catch (SQLException e) {
			
			Main.arena.getLogger().severe("§7[ArenaUtils] §cError to fetch all arenas.");
			
		}
		
		for (Player player : Bukkit.getWorld("lobby").getPlayers()) {
			
			if (player.getGameMode() == GameMode.ADVENTURE) {
			
				ArenaUtils.placeItems(player);
			
			}
		
		}
		
	}
	
	public static void setName(String id, String name) {
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			sql.executeUpdate("UPDATE arena SET name = '" + name + "' WHERE id = '" + id + "';");
		
		} catch (SQLException e) {
			
			Bukkit.getLogger().severe("§7[ArenaUtils] §cError to update name of arena §6" + id + "§c.");
			
		}
		
	}
	
	public static void setWorld(String id, String world) {
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			sql.executeUpdate("UPDATE arena SET world = '" + world + "' WHERE id = '" + id + "';");
		
		} catch (SQLException e) {
			
			Bukkit.getLogger().severe("§7[ArenaUtils] §cError to update world of arena §6" + id + "§c.");
			
		}
		
	}
	
	public static void setWorld(String id, int slot) {
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			sql.executeUpdate("UPDATE arena SET slot = '" + slot + "' WHERE id = '" + id + "';");
		
		} catch (SQLException e) {
			
			Bukkit.getLogger().severe("§7[ArenaUtils] §cError to update slot of arena §6" + id + "§c.");
			
		}
		
	}
	
	public static List<String> getArenas() {
		
		List<String> list = new ArrayList<String>();
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet arenas = sql.executeQuery("SELECT * FROM arena;");
			
			while (arenas.next()) {
				
				list.add(arenas.getString("id"));
				
			}
			
		} catch (SQLException e) {
			
			Main.arena.getLogger().severe("§7[ArenaUtils] §cError to fetch all arenas.");
			
		}
		
		return list;
		
	}
	
	public static String getName(String id) {
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet arenas = sql.executeQuery("SELECT * FROM arena WHERE id = '" + id + "';");
			
			arenas.next();
				
			return arenas.getString("name");
			
		} catch (SQLException e) {
			
			Main.arena.getLogger().severe("§7[ArenaUtils] §cError to get name of arena §6" + id + "§c.");
			
		}
		
		return null;
		
	}
	
	public static int getSlot(String id) {
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet arenas = sql.executeQuery("SELECT * FROM arena WHERE id = '" + id + "';");
			
			arenas.next();
				
			return arenas.getInt("slot");
			
		} catch (SQLException e) {
			
			Main.arena.getLogger().severe("§7[ArenaUtils] §cError to get slot of arena §6" + id + "§c.");
			
		}
		
		return -1;
		
	}

	public static String getWorld(String id) {
	
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet arenas = sql.executeQuery("SELECT * FROM arena WHERE id = '" + id + "';");
			
			arenas.next();
				
			return arenas.getString("world");
			
		} catch (SQLException e) {
			
			Main.arena.getLogger().severe("§7[ArenaUtils] §cError to get world of arena §6" + id + "§c.");
			
		}
		
		return null;
		
	}
	
	public static void delete(String id) {
		
		for (Player player : Bukkit.getWorld(getWorld(id)).getPlayers()) {
			
			player.teleport(Bukkit.getWorld("lobby").getSpawnLocation().add(0.5, 0, 0.5));
			player.setGameMode(GameMode.ADVENTURE);
			PlayerUtils.clearInventory(player);
			PlayerUtils.clearXp(player);
			PlayerUtils.feed(player);
			PlayerUtils.heal(player);
			PlayerUtils.clearEffects(player);
			player.setExp(0);
			player.setTotalExperience(0);
			
		}
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			sql.executeUpdate("DELETE FROM arena WHERE id = '" + id + "';");
		
		} catch (SQLException e) {
			
			Bukkit.getLogger().severe("§7[ArenaUtils] §cError to delete arena §6" + id + "§c.");
			
		}
		
		for (Player player : Bukkit.getWorld("lobby").getPlayers()) {
			
			if (player.getGameMode() == GameMode.ADVENTURE) {
			
				ArenaUtils.placeItems(player);
			
			}
		
		}
		
	}
	
	public static String getId(String name) {
		
		for (String id : getArenas()) {
			
			if (getName(id).equals(name)) {
				
				return id;
				
			}
			
		}
		
		return null;
		
	}
	
	public static void joinArena(Player player, String id) {
		
		if (ArenaUtils.isEnabled(id)) {
		
			player.teleport(ScatterUtils.getSpawns(Bukkit.getWorld(ArenaUtils.getWorld(id)), ArenaUtils.getSize(id)));
			player.setGameMode(GameMode.SURVIVAL);
			PlayerUtils.clearInventory(player);
			PlayerUtils.clearXp(player);
			PlayerUtils.feed(player);
			PlayerUtils.heal(player);
			PlayerUtils.clearEffects(player);
			player.setExp(0);
			player.setTotalExperience(0);
			DisplayUtils.sendActionBar(player, "");
			
			String kitRaw = "";
			
			try {
				
				Statement sql = DatabaseUtils.getConnection().createStatement();
				ResultSet arena = sql.executeQuery("SELECT kit FROM arena WHERE id = '" + id + "';");
				
				arena.next();
				kitRaw = arena.getString("kit");
				
			
			} catch (SQLException e) {
				
				Bukkit.getLogger().severe("§7[ArenaUtils] §cError to get kit of arena §6" + id + "§c.");
				
			}
			
			String[] kit = kitRaw.split(";");
			
			for (int i = 0; i < kit.length; i++) {
				
				player.getInventory().setItem(i, ItemsUtils.unserializeItemStack(kit[i]));
				
			}
			
			Arena.nodamages.add(player);
			
			new BukkitRunnable() {
				
				public void run() {
					
					player.updateInventory();
					
				}
				
			}.runTaskLater(Main.arena, 2);
			
			new BukkitRunnable() {
				
				public void run() {
					
					Arena.nodamages.remove(player);
					
					if (Bukkit.getWorld(ArenaUtils.getWorld(id)).getPlayers().size() > 1) {

						double nearestSize = (double) WorldUtils.getSize(ArenaUtils.getWorld(id));
						Player nearestPlayer = null;
						
						for (Player potential : Bukkit.getWorld(ArenaUtils.getWorld(id)).getPlayers()) {
							
							if (potential != player) {
								
								if (player.getLocation().distance(potential.getLocation()) < nearestSize) {
									
									nearestSize = player.getLocation().distance(potential.getLocation());
									nearestPlayer = potential;
									
								}
								
							}
							
						}
						
						player.setCompassTarget(nearestPlayer.getLocation());
						DisplayUtils.sendActionBar(player, "§7Nearest player is '§6" + nearestPlayer.getName() + "§7' §8(§a" + (int) nearestSize + "§7 blocks§8)");
						
					}
					
				}
				
			}.runTaskLater(Main.arena, 60);
			
		} else {
			
			player.sendMessage(Main.PREFIX + "The arena '§6" + ArenaUtils.getName(id) + "§7' is disabled.");
			
		}
		
	}
	
	public static void saveItems(String id, ItemStack[] items) {
		
		String itemsString = "";
		
		for (int i = 0; i < items.length; i++) {
			
			itemsString += ItemsUtils.serializeItemStack(items[i]) + ";";
			
		}
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			sql.executeUpdate("UPDATE arena SET kit = '" + itemsString + "' WHERE id = '" + id + "';");
		
		} catch (SQLException e) {
			
			Bukkit.getLogger().severe("§7[ArenaUtils] §cError to update world of arena §6" + id + "§c.");
			
		}
		
	}
	
	public static void placeItems (Player player) {
		
		PlayerUtils.clearInventory(player);
		
		for (String id : ArenaUtils.getArenas()) {
			
			ItemStack item = ItemsUtils.createItem(Material.DIAMOND_SWORD, ArenaUtils.getName(id) + " §8(§a" + Bukkit.getWorld(ArenaUtils.getWorld(id)).getPlayers().size() + " §7players§8)", 1, 0);
			item = ItemsUtils.addGlow(item);
			item = ItemsUtils.hideFlags(item);
			
			player.getInventory().setItem(ArenaUtils.getSlot(id), item);
			
		}
		
	}
	
	public static void updateNames (Player player) {
		
		for (String id : ArenaUtils.getArenas()) {
			
			if (player.getInventory().getItem(ArenaUtils.getSlot(id)) != null) {
				
				ItemMeta meta = player.getInventory().getItem(ArenaUtils.getSlot(id)).getItemMeta();
				meta.setDisplayName(ArenaUtils.getName(id) + " §8(§a" + Bukkit.getWorld(ArenaUtils.getWorld(id)).getPlayers().size() + " §7players§8)");
				player.getInventory().getItem(ArenaUtils.getSlot(id)).setItemMeta(meta);
				
			}
			
		}
		
	}
	
	public static void enable (String id) {
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			sql.executeUpdate("UPDATE arena SET state = 1 WHERE id = '" + id + "';");
		
		} catch (SQLException e) {
			
			Bukkit.getLogger().severe("§7[ArenaUtils] §cError to enable arena §6" + id + "§c.");
			
		}
		
	}
	
	public static void disable (String id) {
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			sql.executeUpdate("UPDATE arena SET state = 0 WHERE id = '" + id + "';");
			
			for (Player player : Bukkit.getWorld(ArenaUtils.getWorld(id)).getPlayers()) {
				
				player.teleport(Bukkit.getWorld("lobby").getSpawnLocation().add(0.5, 0, 0.5));
				player.setGameMode(GameMode.ADVENTURE);
				
				PlayerUtils.clearInventory(player);
				PlayerUtils.clearXp(player);
				PlayerUtils.feed(player);
				PlayerUtils.heal(player);
				PlayerUtils.clearEffects(player);
				player.setExp(0);
				player.setTotalExperience(0);
				
				ArenaUtils.placeItems(player);
				
			}
		
		} catch (SQLException e) {
			
			Bukkit.getLogger().severe("§7[ArenaUtils] §cError to disable arena §6" + id + "§c.");
			
		}
		
	}
	
	public static Boolean isEnabled (String id) {
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet arena = sql.executeQuery("SELECT state FROM arena WHERE id = '" + id + "';");
			
			arena.next();
			return arena.getInt("state") == 1 ? true : false;
			
		
		} catch (SQLException e) {
			
			Bukkit.getLogger().severe("§7[ArenaUtils] §cError to get state of arena §6" + id + "§c.");
			
		}
		
		return false;
		
	}
	
	public static int getSize (String id) {
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet arena = sql.executeQuery("SELECT size FROM arena WHERE id = '" + id + "';");
			
			arena.next();
			return arena.getInt("size");
			
		
		} catch (SQLException e) {
			
			Bukkit.getLogger().severe("§7[ArenaUtils] §cError to get size of arena §6" + id + "§c.");
			
		}
		
		return 100;
		
	}
	
	public static void setSize (String id, int size) {
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			sql.executeUpdate("UPDATE arena SET size = " + size + " WHERE id = '" + id + "';");
		
		} catch (SQLException e) {
			
			Bukkit.getLogger().severe("§7[ArenaUtils] §cError to enable arena §6" + id + "§c.");
			
		}
		
	}
	
	
	
} 
