package com.thetonyk.arena.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Team;

import com.thetonyk.arena.Arena;
import com.thetonyk.arena.Main;

public class PlayerUtils {
	
	public static void clearInventory(Player player) {
		
        PlayerInventory inventory = player.getInventory();

        inventory.clear();
        inventory.setArmorContents(null);
        player.setItemOnCursor(new ItemStack(Material.AIR));

        InventoryView openedInventory = player.getOpenInventory();
        
        if (openedInventory.getType() == InventoryType.CRAFTING) {
        	
            openedInventory.getTopInventory().clear();
            
        }
        
    }
	
	public static void clearXp(Player player) {
	
		player.setTotalExperience(0);
        player.setLevel(0);
        player.setExp(0F);
		
	}
	
	public static void feed(Player player) {
		
        player.setSaturation(5.0F);
        player.setExhaustion(0F);
        player.setFoodLevel(20);
        
    }
	
	public static void heal(Player player) {
		
		player.setHealth(player.getMaxHealth());
		
	}
	
	public static void clearEffects(Player player) {
		
		Collection<PotionEffect> activeEffects = player.getActivePotionEffects();

        for (PotionEffect activeEffect : activeEffects) {
        	
            player.removePotionEffect(activeEffect.getType());
            
        }
		
	}
	
	public static Boolean isNew(Player player) {
		
		Boolean isNew = true;
		
		try {
		
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet name = sql.executeQuery("SELECT * FROM users WHERE uuid='" + player.getUniqueId().toString() + "';");
			
			if (name.next()) {
				
				isNew = false;
				
			} else {
				
				isNew = true;
				
			}
			
		} catch (SQLException e) {
			
			Main.arena.getLogger().severe("§7[PlayerUtils] §cError to check if player §6" + player.getName() + "§c is new.");
			return false;
			
		}
		
		return isNew;
		
	}
	
	public static int uniquePlayers() {
		
		int players = 0;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet count = sql.executeQuery("SELECT COUNT(*) AS NumbersOfPlayers FROM users;");
			
			if (count.next()) {
				
				players = Integer.parseInt(count.getString("NumbersOfPlayers"));
				
			}
			
		} catch (SQLException e) {
			
			Main.arena.getLogger().severe("§7[PlayerUtils] §cError to get to number of players.");
			return 0;
			
		}
		
		return players;
		
	}
	
	public static void joinUpdatePlayer(Player player) {
		
		if (isNew(player)) {
			
			try {
				
				Statement sql = DatabaseUtils.getConnection().createStatement();
				sql.executeUpdate("INSERT INTO users (`name`, `uuid`, `ip`, `firstJoin`, `lastJoin`, `lastQuit`, `rank`, `muteState`, `muteReason`, `muteTime`) VALUES ('" + player.getName() + "', '" + player.getUniqueId().toString() + "', '" + player.getAddress().getAddress().getHostAddress() + "', '" + new Date().getTime() + "', '" + new Date().getTime() + "', '0', 'PLAYER', false, '0', '-1');");
				sql.executeUpdate("INSERT INTO scores (`id`, `kills`, `death`, `longshot`, `killstreak`, `time`) VALUES ('" + PlayerUtils.getId(player.getUniqueId()) + "', 0, 0, 0, 0, 0);");
			
			} catch (SQLException e) {
				
				Main.arena.getLogger().severe("§7[PlayerUtils] §cError to insert new player §6" + player.getName() + "§c.");
				
			}
			
		} else {
			
			try {
				
				Statement sql = DatabaseUtils.getConnection().createStatement();
				sql.executeUpdate("UPDATE users SET name = '" + player.getName() + "', ip = '" + player.getAddress().getAddress().getHostAddress() + "', lastJoin = '" + new Date().getTime() + "' WHERE uuid = '" + player.getUniqueId().toString() + "';");
			
			} catch (SQLException e) {
				
				Bukkit.getLogger().severe("§7[PlayerUtils] §cError to update player §6" + player.getName() + "§c.");
				
			}
			
		}
		
	}
	
	public static void leaveUpdatePlayer(Player player) {
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			sql.executeUpdate("UPDATE users SET lastQuit = '" + new Date().getTime() + "' WHERE uuid = '" + player.getUniqueId().toString() + "';");
		
		} catch (SQLException e) {
			
			Bukkit.getLogger().severe("§7[PlayerUtils] §cError to update player §6" + player.getName() + "§c.");
			
		}
		
		if (Arena.scoreboard.containsKey(player)) {
		
			if (Arena.scoreboard.get(player).getTeam(player.getName()) != null) {
				
				Arena.scoreboard.get(player).getTeam(player.getName()).unregister();
				
			}
			
		}
		
	}
	
	public enum Rank {
		
		PLAYER(""), FAMOUS("§bFamous §8| "), BUILDER("§2Build §8| "),STAFF("§cStaff §8| "), MOD("§9Mod §8| "), ADMIN("§4Admin §8| ");
		
		String prefix;
		
		private Rank(String prefix) {
			
			this.prefix = prefix;
			
		}
		
		public String getPrefix() {
			
			return prefix;
			
		}
		
	}
	
	public static Boolean setRank(String player, Rank rank) {
		
		Boolean success;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			sql.executeUpdate("UPDATE users SET rank = '" + rank + "' WHERE name = '" + player + "';");
			
			if (Bukkit.getPlayer(player) != null && Bukkit.getPlayer(player).isOnline()) {
				
				PermissionsUtils.clearPermissions(Bukkit.getPlayer(player));
				PermissionsUtils.setPermissions(Bukkit.getPlayer(player));
			
			}
			
			success = true;
			
		} catch (SQLException e) {
			
			Bukkit.getLogger().severe("§7[PlayerUtils] §cError to update rank of player §6" + player + "§c.");
			success = false;
			
		}
		
		return success;
		
	}
	
	public static Rank getRank(String player) {
		
		Rank rank = Rank.PLAYER;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT rank FROM users WHERE name = '" + player + "';");
			
			if (req.next()) {
				
				rank = Rank.valueOf(req.getString("rank"));
				
			}
			
		} catch (SQLException e) {
			
			Main.arena.getLogger().severe("§7[PlayerUtils] §cError to get the rank of player §6" + player + "§c.");
			
		}
		
		return rank;
		
	}
	
	public static String getRanks() {
		
		String list = "Availables ranks: §aplayer";
		
		for (Rank rank : Rank.values()) {
			
			if (!rank.name().toLowerCase().equalsIgnoreCase("player")) {
				
				list = list + " §7| §a" + rank.name().toLowerCase();
				
			}
			
		}
		
		list = list + "§7.";
		
		return list;
		
	}
	
	public static void updateNametag(String player) {
		
		for (Player scoreboard : Arena.scoreboard.keySet()) {
		
			if (Arena.scoreboard.get(scoreboard).getTeam(player) == null) {
				
				Arena.scoreboard.get(scoreboard).registerNewTeam(player);
				
				Arena.scoreboard.get(scoreboard).getTeam(player).setAllowFriendlyFire(true);
				Arena.scoreboard.get(scoreboard).getTeam(player).setCanSeeFriendlyInvisibles(true);
				Arena.scoreboard.get(scoreboard).getTeam(player).setDisplayName(player + " team");
				Arena.scoreboard.get(scoreboard).getTeam(player).setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
				
			}
		
			String prefix = getRank(player).getPrefix();
			Arena.scoreboard.get(scoreboard).getTeam(player).setPrefix(prefix + "§7");
			Arena.scoreboard.get(scoreboard).getTeam(player).setSuffix("§f");
			
			Arena.scoreboard.get(scoreboard).getTeam(player).addEntry(player);
		
		}
		
		for (Player players : Bukkit.getOnlinePlayers()) {
			
			if (Arena.scoreboard.get(Bukkit.getPlayer(player)).getTeam(players.getName()) == null) {
				
				Arena.scoreboard.get(Bukkit.getPlayer(player)).registerNewTeam(players.getName());
				
				Arena.scoreboard.get(Bukkit.getPlayer(player)).getTeam(players.getName()).setAllowFriendlyFire(true);
				Arena.scoreboard.get(Bukkit.getPlayer(player)).getTeam(players.getName()).setCanSeeFriendlyInvisibles(true);
				Arena.scoreboard.get(Bukkit.getPlayer(player)).getTeam(players.getName()).setDisplayName(player + " team");
				Arena.scoreboard.get(Bukkit.getPlayer(player)).getTeam(players.getName()).setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
				
			}
		
			String prefix = getRank(players.getName()).getPrefix();
			Arena.scoreboard.get(Bukkit.getPlayer(player)).getTeam(players.getName()).setPrefix(prefix + "§7");
			Arena.scoreboard.get(Bukkit.getPlayer(player)).getTeam(players.getName()).setSuffix("§f");
			
			Arena.scoreboard.get(Bukkit.getPlayer(player)).getTeam(players.getName()).addEntry(players.getName());
		
		}
		
		
		
	}
	
	public static String getInfo(Player player, String info) {
		
		String value = null;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT * FROM users WHERE uuid = '" + player.getUniqueId().toString() + "';");
			
			if (req.next()) {
				
				value = req.getString("info");
				
			}
			
		} catch (SQLException e) {
			
			Main.arena.getLogger().severe("§7[PlayerUtils] §cError to get the column §6" + info + " §cof player §6" + player.getName() + "§c.");
			
		}
		
		return value;
		
	}
	
	public static Boolean exist(Player player) {
		
		return exist(Bukkit.getOfflinePlayer(player.getUniqueId()));
		
	}
	
	public static Boolean exist(OfflinePlayer player) {
		
		Boolean exist = false;
		
		try {
		
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT * FROM users WHERE uuid='" + player.getUniqueId().toString() + "';");
			
			if (req.next()) {
				
				exist = true;
				
			} else {
				
				exist = false;
				
			}
			
		} catch (SQLException e) {
			
			Main.arena.getLogger().severe("§7[PlayerUtils] §cError to check if player §6" + player.getName() + "§c is new.");
			
		}
		
		return exist;
		
	}
	
	public static int getId (String name) {
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT id FROM users WHERE name ='" + name + "';");
			
			req.next();
			
			return req.getInt("id");
			
		} catch (SQLException e) {
			
			Main.arena.getLogger().severe("§7[PlayerUtils] §cError to get id of player §6" + name + "§c.");
			return 0;
			
		}
		
	}
	
	public static int getId (UUID uuid) {
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT id FROM users WHERE uuid ='" + uuid + "';");
			
			req.next();
			
			return req.getInt("id");
			
		} catch (SQLException e) {
			
			Main.arena.getLogger().severe("§7[PlayerUtils] §cError to get id of player with UUID §6" + uuid + "§c.");
			return 0;
			
		}
		
	}
	
	public static int getTime (Player player) {
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT time FROM scores WHERE id ='" + PlayerUtils.getId(player.getUniqueId()) + "';");
			
			req.next();
			
			return req.getInt("time");
			
		} catch (SQLException e) {
			
			Main.arena.getLogger().severe("§7[PlayerUtils] §cError to get time of player §6" + player.getName() + "§c.");
			return 0;
			
		}
		
	}
	
	public static void setTime(Player player, int minutes) {
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			sql.executeUpdate("UPDATE scores SET time = '" + minutes + "' WHERE id = '" + PlayerUtils.getId(player.getUniqueId()) + "';");
			return;
			
		} catch (SQLException e) {
			
			Bukkit.getLogger().severe("§7[PlayerUtils] §cError to update time of player §6" + player.getName() + "§c.");
			return;
			
		}
		
	}
	
	public static void updateScoreboard (Player player) {
		
		for (String score : Arena.scoreboard.get(player).getEntries()) {
			
			if (score.startsWith(" ")) {
			
				Arena.scoreboard.get(player).resetScores(score);
				
			}
			
		}
		
		Arena.scoreboard.get(player).getObjective("sidebar").getScore(" ").setScore(10);
		Arena.scoreboard.get(player).getObjective("sidebar").getScore("  §6Kills §8⫸ §a" + Arena.kills.get(player)).setScore(9);
		Arena.scoreboard.get(player).getObjective("sidebar").getScore("  §6Death §8⫸ §a" + Arena.death.get(player)).setScore(8);
		double ratio = 0;
		if (Arena.death.get(player) > 0) {
			ratio = (double) Arena.kills.get(player) / (double) Arena.death.get(player);
		}
		DecimalFormat formatRatio = new DecimalFormat("##.##");
		Arena.scoreboard.get(player).getObjective("sidebar").getScore("  §6Ratio §8⫸ §a" + formatRatio.format(ratio)).setScore(7);
		Arena.scoreboard.get(player).getObjective("sidebar").getScore("  ").setScore(6);
		Arena.scoreboard.get(player).getObjective("sidebar").getScore("  §6Killstreak §8⫸ §a" + Arena.currentKillstreak.get(player)).setScore(5);
		Arena.scoreboard.get(player).getObjective("sidebar").getScore("   ").setScore(4);
		DecimalFormat format = new DecimalFormat("##.#");
		Arena.scoreboard.get(player).getObjective("sidebar").getScore("  §6Best Longshot §8⫸ §a" + format.format(Arena.longshot.get(player)) + "m").setScore(3);
		Arena.scoreboard.get(player).getObjective("sidebar").getScore("  §6Best Killstreak §8⫸ §a" + Arena.killstreak.get(player)).setScore(2);
		Arena.scoreboard.get(player).getObjective("sidebar").getScore("    ").setScore(1);
		Arena.scoreboard.get(player).getObjective("sidebar").getScore("  §b@CommandsPVP").setScore(0);
		
	}
	
	public static int getKills (Player player) {
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT kills FROM scores WHERE id ='" + PlayerUtils.getId(player.getUniqueId()) + "';");
			
			req.next();
			
			return req.getInt("kills");
			
		} catch (SQLException e) {
			
			Main.arena.getLogger().severe("§7[PlayerUtils] §cError to get kills of player §6" + player.getName() + "§c.");
			return 0;
			
		}
		
	}
	
	public static int getDeath (Player player) {
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT death FROM scores WHERE id ='" + PlayerUtils.getId(player.getUniqueId()) + "';");
			
			req.next();
			
			return req.getInt("death");
			
		} catch (SQLException e) {
			
			Main.arena.getLogger().severe("§7[PlayerUtils] §cError to get death of player §6" + player.getName() + "§c.");
			return 0;
			
		}
		
	}
	
	public static int getKillstreak (Player player) {
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT killstreak FROM scores WHERE id ='" + PlayerUtils.getId(player.getUniqueId()) + "';");
			
			req.next();
			
			return req.getInt("killstreak");
			
		} catch (SQLException e) {
			
			Main.arena.getLogger().severe("§7[PlayerUtils] §cError to get killstreak of player §6" + player.getName() + "§c.");
			return 0;
			
		}
		
	}
	
	public static double getLongshot (Player player) {
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT longshot FROM scores WHERE id ='" + PlayerUtils.getId(player.getUniqueId()) + "';");
			
			req.next();
			
			return req.getDouble("longshot");
			
		} catch (SQLException e) {
			
			Main.arena.getLogger().severe("§7[PlayerUtils] §cError to get longshot of player §6" + player.getName() + "§c.");
			return 0;
			
		}
		
	}
	
	public static void setupScore (Player player) {
		
		if (Arena.kills.containsKey(player)) {
			
			Arena.kills.remove(player);
			
		}
		
		if (Arena.death.containsKey(player)) {
			
			Arena.death.remove(player);
			
		}

		if (Arena.killstreak.containsKey(player)) {
			
			Arena.killstreak.remove(player);
			
		}
		
		if (Arena.longshot.containsKey(player)) {
			
			Arena.longshot.remove(player);
			
		}
		
		if (Arena.currentKillstreak.containsKey(player)) {
			
			Arena.currentKillstreak.remove(player);
			
		}
		
		Arena.kills.put(player, PlayerUtils.getKills(player));
		Arena.death.put(player, PlayerUtils.getDeath(player));
		Arena.killstreak.put(player, PlayerUtils.getKillstreak(player));
		Arena.longshot.put(player, PlayerUtils.getLongshot(player));
		Arena.currentKillstreak.put(player, 0);
		
	}
	
	public static void updateScores (Player player) {
		
		try {
		
			Statement sql = DatabaseUtils.getConnection().createStatement();
			sql.executeUpdate("UPDATE scores SET kills = " + Arena.kills.get(player) + ", death = " + Arena.death.get(player) + ", killstreak = " + Arena.killstreak.get(player) + ", longshot = " + Arena.longshot.get(player) + " WHERE id = '" + PlayerUtils.getId(player.getUniqueId()) + "';");
			return;
			
		} catch (SQLException e) {
			
			Bukkit.getLogger().severe("§7[PlayerUtils] §cError to update scores of player §6" + player.getName() + "§c.");
			return;
			
		}
		
	}

}
