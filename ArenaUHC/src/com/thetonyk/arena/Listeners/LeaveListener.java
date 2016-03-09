package com.thetonyk.arena.Listeners;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.thetonyk.arena.Arena;
import com.thetonyk.arena.Utils.ItemsUtils;
import com.thetonyk.arena.Utils.PermissionsUtils;
import com.thetonyk.arena.Utils.PlayerUtils;

public class LeaveListener implements Listener{
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		
		Player player = event.getPlayer();
		
		if (Arena.joinTime.containsKey(player)) {
			
			Calendar time = new GregorianCalendar();
			time.setTime(new Date(new Date().getTime() - Arena.joinTime.get(player)));
			int minutes = time.get(Calendar.MINUTE);
			PlayerUtils.setTime(player, PlayerUtils.getTime(player) + minutes);
			Arena.joinTime.remove(player);
			
		}
		
		if (!player.getWorld().getName().equalsIgnoreCase("lobby")) {
			
			player.getWorld().dropItem(player.getLocation(), ItemsUtils.createItem(Material.GOLDEN_APPLE, "§b§lCommandsPVP §r§6Golden Apple", 1, 0));
			player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.ARROW, 8));
			if (new Random().nextDouble() < 0.35) {
				player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.INK_SACK, 1, (short) 4));
			}
			if (new Random().nextDouble() < 0.20) {
				player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.DIAMOND, 1));
			}
			
			int death = Arena.death.get(player) + 1;
			Arena.death.remove(player);
			Arena.death.put(player, death);
			if (Arena.currentKillstreak.get(player) > Arena.killstreak.get(player)) {
				Arena.killstreak.remove(player);
				Arena.killstreak.put(player, Arena.currentKillstreak.get(player));
			}
			
		}
		
		PlayerUtils.updateScores(player);
		
		Arena.kills.remove(player);
		Arena.death.remove(player);
		Arena.killstreak.remove(player);
		Arena.longshot.remove(player);
		Arena.currentKillstreak.remove(player);
		
		player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
		Arena.scoreboard.remove(player);
		
		if (Arena.playerBlocks.containsKey(player)) {
		
			for (Location block : Arena.playerBlocks.get(player)) {
				
				block.getBlock().setType(Material.AIR);
				
				if (Arena.blocks.contains(block)) {
					
					Arena.blocks.remove(block);
					
				}
				
			}
		
			Arena.playerBlocks.remove(player);
			
		}
		
		event.setQuitMessage(null);
		Bukkit.broadcastMessage("§7[§c-§7] " + PlayerUtils.getRank(player.getName()).getPrefix() + "§7" + player.getName());
		
		if (InventoryListener.editKits.containsKey(event.getPlayer().getUniqueId())) {
			
			InventoryListener.editKits.remove(event.getPlayer().getUniqueId());
			
		}
		
		PlayerUtils.leaveUpdatePlayer(player);
		PermissionsUtils.clearPermissions(player);
		
		if (player.isInsideVehicle()) {
			player.leaveVehicle();
		}
		
	}

}
