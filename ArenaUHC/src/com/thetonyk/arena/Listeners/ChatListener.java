package com.thetonyk.arena.Listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.thetonyk.arena.Arena;
import com.thetonyk.arena.Main;
import com.thetonyk.arena.Utils.PlayerUtils;

public class ChatListener implements Listener {

	List<Player> cooldown = new ArrayList<Player>();
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		
		Player player = event.getPlayer();
		
		if (Arena.mute && !player.hasPermission("arena.speak")) {
			
			player.sendMessage(Main.PREFIX + "The chat is muted.");
			event.setCancelled(true);
			return;
			
		}
		
		if (cooldown.contains(player)) {
			
			event.setCancelled(true);
			return;
			
		}
		
		event.setFormat(PlayerUtils.getRank(player.getName()).getPrefix() + "§7" + player.getName() + " §8⫸ §f%2$s");
		
		cooldown.add(player);
		
		new BukkitRunnable() {
			
			public void run() {
				
				cooldown.remove(player);
				
			}
			
		}.runTaskLater(Main.arena, 40);
		
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		
		Player player = event.getPlayer();
		String message = event.getMessage();
		String command = message.split(" ")[0];
		command = command.substring(1).toLowerCase();
		
		if (!player.isOp()) {
			
			switch (command) {
			case "achievement":
			case "ban-ip": 
			case "banlist": 
			case "blockdata":
			case "clone": 
			case "debug": 
			case "deop":
			case "defaultgamemode":
			case "difficulty":
			case "enchant":
			case "effect":
			case "entitydata":
			case "execute":
			case "fill":
			case "filter":
			case "gamerule":
			case "icanhasbukkit":
			case "kill":
			case "me":
			case "op":
			case "packet":
			case "packet_filter":
			case "pardon":
			case "pardon-ip":
			case "particle":
			case "pl":
			case "playsound":
			case "plugins":
			case "pregenerator":
			case "preg":
			case "protocol":
			case "publish":
			case "reload":
			case "replaceitem":
			case "restart":
			case "rl":
			case "save-all":
			case "save-off":
			case "save-on":
			case "say":
			case "scoreboard":
			case "setblock":
			case "setidletimeout":
			case "spawnpoint":
			case "spreadplayer":
			case "stats":
			case "stop":
			case "summon":
			case "tellraw":
			case "time":
			case "timings":
			case "testfor":
			case "testforblock":
			case "testforblocks":
			case "title":
			case "toggledownfall":
			case "trigger":
			case "ver":
			case "version":
			case "weather":
			case "worldborder":
			case "xp": 
			case "?":
				player.sendMessage(Main.NO_PERMS);
				event.setCancelled(true);
				break;
			default:
				break;
				
			}
		
		
			if (command.startsWith("bukkit:") || command.startsWith("minecraft:") || command.startsWith("protocollib:") || command.startsWith("spigot:") || command.startsWith("uhc:")) {
				
					player.sendMessage(Main.NO_PERMS);
					event.setCancelled(true);
				
			}
		
		}
		
	}
			
}
