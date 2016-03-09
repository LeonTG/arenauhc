package com.thetonyk.arena.Listeners;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

import com.thetonyk.arena.Main;
import com.thetonyk.arena.Utils.ArenaUtils;
import com.thetonyk.arena.Utils.DatabaseUtils;
import com.thetonyk.arena.Utils.DisplayUtils;
import com.thetonyk.arena.Utils.ItemsUtils;
import com.thetonyk.arena.Utils.PlayerUtils;

public class InventoryListener implements Listener {
	
	public static HashMap<UUID, String> editKits = new HashMap<UUID, String>();
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {	
		
		if (event.getWhoClicked().getGameMode() != GameMode.ADVENTURE) {
			
			return;
			
		}
		
		if (event.getInventory().getTitle().equals("§8⫸ §4Set kits")) {
			
			event.setCancelled(true);
			
			if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName()) {
				
				for (String id : ArenaUtils.getArenas()) {
					
					if (ArenaUtils.getName(id).equals(event.getCurrentItem().getItemMeta().getDisplayName())) {
						
						if (editKits.containsValue(id)) {
							
							String player = null;
							
							for (UUID uuid : editKits.keySet()) {
								
								if (editKits.get(uuid) == id) {
									
									player = Bukkit.getPlayer(uuid).getName();
									
								}
								
							}
							
							if (player == null) {
								
								event.getWhoClicked().sendMessage(Main.PREFIX + "A player already edit this kit.");
								
							} else {
								
								event.getWhoClicked().sendMessage(Main.PREFIX + "The player '§6" + player + "§7' already edit this kit.");
								
							}
							
							return;
							
						} else {
						
							Bukkit.getPlayer(event.getWhoClicked().getUniqueId()).setGameMode(GameMode.CREATIVE);
							PlayerUtils.clearInventory(Bukkit.getPlayer(event.getWhoClicked().getUniqueId()));
							Bukkit.getPlayer(event.getWhoClicked().getUniqueId()).closeInventory();
							DisplayUtils.sendTitle(Bukkit.getPlayer(event.getWhoClicked().getUniqueId()), "", "§6Open your inventory to set the kit", 2, 30, 2);
							InventoryListener.editKits.put(Bukkit.getPlayer(event.getWhoClicked().getUniqueId()).getUniqueId(), id);
							Bukkit.getPlayer(event.getWhoClicked().getUniqueId()).teleport(new Location(Bukkit.getWorld("lobby"), 500, 1.5, 0));
							
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
								
								Bukkit.getPlayer(event.getWhoClicked().getUniqueId()).getInventory().setItem(i, ItemsUtils.unserializeItemStack(kit[i]));
								
							}
							
							return;
							
						}
						
					}
					
				}
				
				return;
					
			}
			
		}
		
		if (event.getWhoClicked().getWorld().getName().equals("lobby") && event.getAction() == InventoryAction.HOTBAR_SWAP) {

			event.setCancelled(true);
			return;
				
		}
				
		if (event.getWhoClicked().getWorld().getName().equals("lobby")) {
					
			event.setCancelled(true);
			return;
		
		}
			
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClose(InventoryCloseEvent event) {

		if (event.getInventory().getType() == InventoryType.ANVIL) {
			
			if (event.getPlayer().getWorld().getName().equalsIgnoreCase("lobby")) {
				
				if (PlayerListener.anvils.contains(event.getInventory().getLocation())) {
					
					for (Location location : PlayerListener.anvils) {
						
						if (location.equals(event.getInventory().getLocation())) {
							
							event.getPlayer().getWorld().getBlockAt(location).setData((byte) 1);
							
						}
						
					}
					
				}
				
			}
			
		}
		
	}

}
