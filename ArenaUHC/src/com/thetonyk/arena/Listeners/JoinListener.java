package com.thetonyk.arena.Listeners;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;

import com.thetonyk.arena.Arena;
import com.thetonyk.arena.Main;
import com.thetonyk.arena.Utils.ArenaUtils;
import com.thetonyk.arena.Utils.DatabaseUtils;
import com.thetonyk.arena.Utils.DisplayUtils;
import com.thetonyk.arena.Utils.PlayerUtils;
import com.thetonyk.arena.Utils.PermissionsUtils;

public class JoinListener implements Listener {
	
	@EventHandler
	public void onConnect(PlayerLoginEvent event) {
		
		PermissionsUtils.setPermissions(event.getPlayer());
		
		if (event.getResult() == Result.KICK_BANNED) {
			
			if (Bukkit.getBanList(Type.IP).getBanEntry(event.getAddress().getHostAddress()) != null) {
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					
					if (player.hasPermission("uhc.alerts")) {
						
						player.sendMessage(Main.PREFIX + "The player '§6" + event.getPlayer().getName() + "§7' try to join but is IP banned for: §a" + Bukkit.getBanList(Type.IP).getBanEntry(event.getAddress().getHostAddress()).getReason() + "§7.");
						
					}
					
				}
				
				String expire = "";
				
				if (Bukkit.getBanList(Type.IP).getBanEntry(event.getAddress().getHostAddress()).getExpiration() != null) {
					
					expire = "\n§6Expire: §8⫸ " + Bukkit.getBanList(Type.IP).getBanEntry(event.getAddress().getHostAddress()).getExpiration().getTime();
					
				}
				
				event.setKickMessage("§8⫸ §7You are §6IP-banned §7from the server §8⫷\n\n§6Reason: §8⫸ §7" + Bukkit.getBanList(Type.IP).getBanEntry(event.getAddress().getHostAddress()).getReason() + expire + "\n\n§8⫸ §7To appeal, contact us on Twitter §a@CommandsPVP §8⫷");
				
			}
			else if (Bukkit.getBanList(Type.NAME).getBanEntry(event.getPlayer().getName()) != null) {
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					
					if (player.hasPermission("uhc.alerts")) {
						
						player.sendMessage(Main.PREFIX + "The player '§6" + event.getPlayer().getName() + "§7' try to join but is banned for: §a" + Bukkit.getBanList(Type.NAME).getBanEntry(event.getPlayer().getName()).getReason() + "§7.");
						
					}
					
				}
				
				String expire = "";
				
				if (Bukkit.getBanList(Type.NAME).getBanEntry(event.getPlayer().getName()).getExpiration() != null) {
					
					expire = "\n§6Expire: §8⫸ " + Bukkit.getBanList(Type.NAME).getBanEntry(event.getPlayer().getName()).getExpiration().getTime();
					
				}
				
				event.setKickMessage("§8⫸ §7You are §6banned §7from the server §8⫷\n\n§6Reason: §8⫸ §7" + Bukkit.getBanList(Type.NAME).getBanEntry(event.getPlayer().getName()).getReason() + expire + "\n\n§8⫸ §7To appeal, contact us on Twitter §a@CommandsPVP §8⫷");
				
			} else {
				
				event.allow();
				
			}
			
		}
		else if (event.getResult() == Result.KICK_WHITELIST) {
			
			if(event.getPlayer().isOp() || event.getPlayer().hasPermission("uhc.bypasswhitelist")) {
				
				event.allow();
				return;
				
			}
			
			event.setKickMessage("§8⫸ §7You are not whitelisted §8⫷");
			
		}
		
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		
		Player player = event.getPlayer();
		
		int players = 0;
		
		if (!Bukkit.getOnlinePlayers().isEmpty()) {
			
			for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
				
				if (onlinePlayer.isOnline()) {
				
					players++;
					
				}
			}
		}
		
		DisplayUtils.sendTitle(player, "§aCommandsPVP", "§7Welcome on the §aUHC Arena §7⋯ §a" + players + " §7players onlines", 0, 80, 10);
		
		event.setJoinMessage(null);
		Bukkit.broadcastMessage("§7[§a+§7] " + PlayerUtils.getRank(player.getName()).getPrefix() + "§7" + player.getName());
		player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(1024);
		
		if (player.isDead()) {
			player.spigot().respawn();
		}
		
		if (PlayerUtils.isNew(player)) {
			Bukkit.broadcastMessage(Main.PREFIX + "Welcome to §a" + player.getName() + " §7on the server! §8(§7#§6" + (PlayerUtils.uniquePlayers() + 1) + "§8)");
		}
		
		PlayerUtils.joinUpdatePlayer(player);
		
		Arena.joinTime.put(player, new Date().getTime());
		
		Arena.scoreboard.put(player, Bukkit.getScoreboardManager().getNewScoreboard());
		Arena.scoreboard.get(player).registerNewObjective("below", "dummy");
		Arena.scoreboard.get(player).getObjective("below").setDisplaySlot(DisplaySlot.BELOW_NAME);
		Arena.scoreboard.get(player).getObjective("below").setDisplayName("§4♥");
		Arena.scoreboard.get(player).registerNewObjective("list", "dummy");
		Arena.scoreboard.get(player).getObjective("list").setDisplaySlot(DisplaySlot.PLAYER_LIST);
		Arena.scoreboard.get(player).registerNewObjective("sidebar", "dummy");
		Arena.scoreboard.get(player).getObjective("sidebar").setDisplaySlot(DisplaySlot.SIDEBAR);
		Arena.scoreboard.get(player).getObjective("sidebar").setDisplayName(Main.PREFIX + "§oStats");
		player.setScoreboard(Arena.scoreboard.get(player));
		
		PlayerUtils.setupScore(player);
		PlayerUtils.updateScoreboard(player);
		PlayerUtils.updateNametag(player.getName());
		
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
		
		if (player.hasPermission("arena.fly")) {
			
			player.setAllowFlight(true);
			
		}
		
		new BukkitRunnable() {
			
			public void run() {
				
				if (!Arena.kills.containsKey(player) && !Arena.death.containsKey(player) && !Arena.killstreak.containsKey(player) && !Arena.longshot.containsKey(player)) {
					
					PlayerUtils.setupScore(player);
					
				}
				
				if (PlayerUtils.isNew(player)) {
					
					try {
						
						Statement sql = DatabaseUtils.getConnection().createStatement();
						sql.executeUpdate("INSERT INTO scores (`id`, `kills`, `death`, `longshots`, `killstreak`, `time`) VALUES ('" + PlayerUtils.getId(player.getUniqueId()) + "', 0, 0, 0, 0, 0);");
					
					} catch (SQLException e) {
						
						Main.arena.getLogger().severe("§7[PlayerUtils] §cError to insert new player scores §6" + player.getName() + "§c.");
						
					}
					
				}
				
			}
			
		}.runTaskLater(Main.arena, 600);
		
	}

}
