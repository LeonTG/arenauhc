package com.thetonyk.arena.Utils;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.thetonyk.arena.Main;

public class ScatterUtils {
	
	public static Location getSpawns(World world, int size) {
		
		//Inspired by @LeonTG77
			
		for (int i = 0; i < 3000; i++) {
			
			if (i == 2999) {
				
				for (Player playerSend : Bukkit.getOnlinePlayers()) {
					
					if (playerSend.hasPermission("arena.warning")) {
						
						playerSend.sendMessage(Main.PREFIX + "Error to teleport a player.");
						
					}
					
				}
				
			}
			
			Random random = new Random();
			
			int x = random.nextInt(size) - (size / 2);
			int z = random.nextInt(size) - (size / 2);
			
			Location spawn = new Location(world, x + 0.5, 0, z + 0.5);
			
			Boolean valid = true;
			
			if (world.getHighestBlockYAt(x, z) < 60) {
				
				valid = false;
				
			}
			
			Material block = new Location(world, x + 0.5, world.getHighestBlockYAt(x, z) - 1, z + 0.5).getBlock().getType();
			
			switch (block) {
			
			case CACTUS:
			case LAVA:
			case STATIONARY_LAVA:
			case STATIONARY_WATER:
			case WATER:
			case LEAVES:
			case LEAVES_2:
				valid = false;
				break;
			default:
				break;
			
			}
			
			if (valid) {
				
				spawn.setY(world.getHighestBlockYAt((int) spawn.getX(), (int) spawn.getY()) + 1.5);
				return spawn;
				
			}
			
		}
		
		return new Location(world, 0, world.getHighestBlockYAt(0, 0), 0);
		
	}
	
}
