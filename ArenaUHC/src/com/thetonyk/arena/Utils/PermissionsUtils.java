package com.thetonyk.arena.Utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import com.thetonyk.arena.Main;
import com.thetonyk.arena.Utils.PlayerUtils.Rank;

public class PermissionsUtils {
	
	private static Map<String, PermissionAttachment> permissions = new HashMap<String, PermissionAttachment>();
	
	public static void setPermissions(Player player) {
		
		if (!permissions.containsKey(player.getName())) {
			
			permissions.put(player.getName(), player.addAttachment(Main.arena));
			
		}
		
		PermissionAttachment permission = permissions.get(player.getName());
		Rank rank = PlayerUtils.getRank(player.getName());
		
		if (rank == Rank.ADMIN) {
			
			player.setOp(true);
			return;
			
		}
		
		permission.setPermission("arena.about", true);
		permission.setPermission("arena.lag", true);
		permission.setPermission("arena.ms", true);
		permission.setPermission("arena.help", true);
		permission.setPermission("arena.lobby", true);
		
		if (rank == Rank.PLAYER) {
			return;
		}
		
		permission.setPermission("arena.fly", true);
		
		if (rank == Rank.FAMOUS) {
			return;
		}
		
		permission.setPermission("arena.build", true);
		permission.setPermission("arena.gamemode", true);
		
		if (rank == Rank.BUILDER) {
			return;
		}
		
		permission.setPermission("arena.gamemode", false);
		permission.setPermission("arena.build", false);
		permission.setPermission("arena.speak", true);
		permission.setPermission("arena.private", true);
		
		if (rank == Rank.MOD) {
			return;
		}
					
		permission.setPermission("arena.world", true);	
		permission.setPermission("arena.whitelist", true);
		permission.setPermission("arena.pregen", true);
		permission.setPermission("arena.border", true);
		permission.setPermission("arena.alerts", true);
		permission.setPermission("arena.broadcast", true);
		permission.setPermission("arena.butcher", true);
		permission.setPermission("arena.clear", true);
		permission.setPermission("arena.arena", true);
		permission.setPermission("arena.utils", true);
		
	}
	
	public static void clearPermissions(Player player) {
		
		if (permissions.containsKey(player.getName())) {
			
			try {
				
				player.removeAttachment(permissions.get(player.getName()));
				
			} catch (Exception e) {
				
				Main.arena.getLogger().severe("§7[PermissionsUtils] §cError to clear permissions of player §6" + player.getName() + "§c.");
				
			}
			
			permissions.remove(player.getName());
			
		}
		
	}

}
