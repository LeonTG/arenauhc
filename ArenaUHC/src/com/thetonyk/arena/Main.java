package com.thetonyk.arena;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.material.Wood;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;

import com.thetonyk.arena.Utils.WorldUtils;
import com.thetonyk.arena.Commands.AboutCommand;
import com.thetonyk.arena.Commands.ArenaCommand;
import com.thetonyk.arena.Commands.BorderCommand;
import com.thetonyk.arena.Commands.BroadcastCommand;
import com.thetonyk.arena.Commands.ButcherCommand;
import com.thetonyk.arena.Commands.ClearCommand;
import com.thetonyk.arena.Commands.FlyCommand;
import com.thetonyk.arena.Commands.GamemodeCommand;
import com.thetonyk.arena.Commands.HelpCommand;
import com.thetonyk.arena.Commands.LagCommand;
import com.thetonyk.arena.Commands.LobbyCommand;
import com.thetonyk.arena.Commands.MsCommand;
import com.thetonyk.arena.Commands.PCommand;
import com.thetonyk.arena.Commands.PregenCommand;
import com.thetonyk.arena.Commands.RankCommand;
import com.thetonyk.arena.Commands.UtilsCommand;
import com.thetonyk.arena.Commands.WhitelistCommand;
import com.thetonyk.arena.Commands.WorldCommand;
import com.thetonyk.arena.Listeners.ChatListener;
import com.thetonyk.arena.Listeners.EnvironmentListener;
import com.thetonyk.arena.Listeners.InventoryListener;
import com.thetonyk.arena.Listeners.JoinListener;
import com.thetonyk.arena.Listeners.LeaveListener;
import com.thetonyk.arena.Listeners.PlayerListener;
import com.thetonyk.arena.Utils.ArenaUtils;
import com.thetonyk.arena.Utils.DisplayUtils;
import com.thetonyk.arena.Utils.ItemsUtils;
import com.thetonyk.arena.Utils.PermissionsUtils;
import com.thetonyk.arena.Utils.PlayerUtils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class Main extends JavaPlugin {
	
	public static Main arena;
	
	public static final String NO_PERMS = "§fUnknown command.";
	public static final String PREFIX = "§a§lArena §8⫸ §7";
	public static final TextComponent PREFIX_COMPONENT = getPrefixComponent();
	private static final BossBar bar = Bukkit.createBossBar("§aTeaming §cnot allowed §ain the Arena", BarColor.GREEN, BarStyle.SOLID);
	
	@Override
	public void onEnable() {
		
		getLogger().info("Arena UHC Plugin has been enabled.");
		getLogger().info("Plugin by TheTonyk for CommandsPVP");
		
		arena = this;
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			PermissionsUtils.setPermissions(player);
			
		}
		
		WorldUtils.loadAllWorlds();
		DisplayUtils.versionName();
		Bukkit.clearRecipes();
		
		for (int i = 0; i < TreeSpecies.values().length; i++) {
		
			for (int y = 0; y < TreeSpecies.values().length; y++) {
				
				ShapelessRecipe stick = new ShapelessRecipe(ItemsUtils.createItem(Material.STICK, "§b§lCommandsPVP §r§6Stick", 4, 0));
				stick.addIngredient(new Wood(TreeSpecies.values()[i]));
				stick.addIngredient(new Wood(TreeSpecies.values()[y]));
				Bukkit.addRecipe(stick);
			
			}
			
		}
		
		ShapedRecipe diamondSword1 = new ShapedRecipe(ItemsUtils.createItem(Material.DIAMOND_SWORD, "§b§lCommandsPVP §r§6Sword", 1, 0));
		diamondSword1.shape("BAA", "BAA", "CAA");
		diamondSword1.setIngredient('A', Material.AIR);
		diamondSword1.setIngredient('B', Material.DIAMOND);
		diamondSword1.setIngredient('C', Material.STICK);
		Bukkit.addRecipe(diamondSword1);
		
		ShapedRecipe diamondSword2 = new ShapedRecipe(ItemsUtils.createItem(Material.DIAMOND_SWORD, "§b§lCommandsPVP §r§6Sword", 1, 0));
		diamondSword2.shape("ABA", "ABA", "ACA");
		diamondSword2.setIngredient('A', Material.AIR);
		diamondSword2.setIngredient('B', Material.DIAMOND);
		diamondSword2.setIngredient('C', Material.STICK);
		Bukkit.addRecipe(diamondSword2);
		
		ShapedRecipe diamondSword3 = new ShapedRecipe(ItemsUtils.createItem(Material.DIAMOND_SWORD, "§b§lCommandsPVP §r§6Sword", 1, 0));
		diamondSword3.shape("AAB", "AAB", "AAC");
		diamondSword3.setIngredient('A', Material.AIR);
		diamondSword3.setIngredient('B', Material.DIAMOND);
		diamondSword3.setIngredient('C', Material.STICK);
		Bukkit.addRecipe(diamondSword3);
		
		ShapedRecipe diamondHelmet1 = new ShapedRecipe(ItemsUtils.createItem(Material.DIAMOND_HELMET, "§b§lCommandsPVP §r§6Helmet", 1, 0));
		diamondHelmet1.shape("BBB", "BAB", "AAA");
		diamondHelmet1.setIngredient('A', Material.AIR);
		diamondHelmet1.setIngredient('B', Material.DIAMOND);
		Bukkit.addRecipe(diamondHelmet1);
		
		ShapedRecipe diamondHelmet2 = new ShapedRecipe(ItemsUtils.createItem(Material.DIAMOND_HELMET, "§b§lCommandsPVP §r§6Helmet", 1, 0));
		diamondHelmet2.shape("AAA", "BBB", "BAB");
		diamondHelmet2.setIngredient('A', Material.AIR);
		diamondHelmet2.setIngredient('B', Material.DIAMOND);
		Bukkit.addRecipe(diamondHelmet2);
		
		ShapedRecipe diamondChesteplate = new ShapedRecipe(ItemsUtils.createItem(Material.DIAMOND_CHESTPLATE, "§b§lCommandsPVP §r§6Chestplate", 1, 0));
		diamondChesteplate.shape("BAB", "BBB", "BBB");
		diamondChesteplate.setIngredient('A', Material.AIR);
		diamondChesteplate.setIngredient('B', Material.DIAMOND);
		Bukkit.addRecipe(diamondChesteplate);
		
		ShapedRecipe diamondLeggings = new ShapedRecipe(ItemsUtils.createItem(Material.DIAMOND_LEGGINGS, "§b§lCommandsPVP §r§6Leggings", 1, 0));
		diamondLeggings.shape("BBB", "BAB", "BAB");
		diamondLeggings.setIngredient('A', Material.AIR);
		diamondLeggings.setIngredient('B', Material.DIAMOND);
		Bukkit.addRecipe(diamondLeggings);
		
		ShapedRecipe diamondBoots1 = new ShapedRecipe(ItemsUtils.createItem(Material.DIAMOND_BOOTS, "§b§lCommandsPVP §r§6Boots", 1, 0));
		diamondBoots1.shape("BAB", "BAB", "AAA");
		diamondBoots1.setIngredient('A', Material.AIR);
		diamondBoots1.setIngredient('B', Material.DIAMOND);
		Bukkit.addRecipe(diamondBoots1);

		ShapedRecipe diamondBoots2 = new ShapedRecipe(ItemsUtils.createItem(Material.DIAMOND_BOOTS, "§b§lCommandsPVP §r§6Boots", 1, 0));
		diamondBoots2.shape("AAA", "BAB", "BAB");
		diamondBoots2.setIngredient('A', Material.AIR);
		diamondBoots2.setIngredient('B', Material.DIAMOND);
		Bukkit.addRecipe(diamondBoots2);
		
		this.getCommand("about").setExecutor(new AboutCommand());
		this.getCommand("world").setExecutor(new WorldCommand());
		this.getCommand("whitelist").setExecutor(new WhitelistCommand());
		this.getCommand("rank").setExecutor(new RankCommand());
		this.getCommand("pregen").setExecutor(new PregenCommand());
		this.getCommand("border").setExecutor(new BorderCommand());
		this.getCommand("broadcast").setExecutor(new BroadcastCommand());
		this.getCommand("butcher").setExecutor(new ButcherCommand());
		this.getCommand("clear").setExecutor(new ClearCommand());
		this.getCommand("gamemode").setExecutor(new GamemodeCommand());
		this.getCommand("arena").setExecutor(new ArenaCommand());
		this.getCommand("lag").setExecutor(new LagCommand());
		this.getCommand("ms").setExecutor(new MsCommand());
		this.getCommand("help").setExecutor(new HelpCommand());
		this.getCommand("utils").setExecutor(new UtilsCommand());
		this.getCommand("fly").setExecutor(new FlyCommand());
		this.getCommand("p").setExecutor(new PCommand());
		this.getCommand("lobby").setExecutor(new LobbyCommand());
		
		PluginManager manager = Bukkit.getPluginManager();
		
		manager.registerEvents(new JoinListener(), this);
		manager.registerEvents(new LeaveListener(), this);
		manager.registerEvents(new PlayerListener(), this);
		manager.registerEvents(new ChatListener(), this);
		manager.registerEvents(new EnvironmentListener(), this);
		manager.registerEvents(new InventoryListener(), this);
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
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
			
		}
		
		new BukkitRunnable() {
			
			public void run() {
				
				for (Player scoreboard : Arena.scoreboard.keySet()) {
				
					for (Player player : Bukkit.getOnlinePlayers()) {
						
						Arena.scoreboard.get(scoreboard).getObjective("below").getScore(player.getName()).setScore((int) (((player.getHealth()) / 2) * 10));
						Arena.scoreboard.get(scoreboard).getObjective("list").getScore(player.getName()).setScore((int) (((player.getHealth()) / 2) * 10));
						
					}
					
				}
				
			}
			
		}.runTaskTimer(Main.arena, 1, 1);
		
		new BukkitRunnable() {
			
			int color = 1;
			
			public void run() {
				
				BarColor barColor = color == 1 ? BarColor.GREEN : BarColor.RED;
				String barTitle = color == 1 ? "§cTeaming not allowed in the Arena" : "§aTeaming not allowed in the Arena";
				bar.setColor(barColor);
				bar.setTitle(barTitle);
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					DisplayUtils.sendTab(player);
					bar.addPlayer(player);
					
				}
				
				color = color == 1 ? 2 : 1;
				
				for (Player player : Bukkit.getWorld("lobby").getPlayers()) {
					
					if (player.getGameMode() == GameMode.ADVENTURE) {
					
						ArenaUtils.updateNames(player);
					
					}
				
				}
				
			}
			
		}.runTaskTimer(Main.arena, 1, 20);
		
		new BukkitRunnable() {
			
			int messageType = 1;
			
			public void run() {
				
				String message = messageType == 1 ? "§7Use §a/hotbar §7to customize your kit" : "§7Use §a/help §7to see availables commands";
				
				for (Player player : Bukkit.getWorld("lobby").getPlayers()) {
					
					DisplayUtils.sendActionBar(player, message);
					
				}
				
				new BukkitRunnable() { public void run() {
					
					for (Player player : Bukkit.getWorld("lobby").getPlayers()) {
						
						DisplayUtils.sendActionBar(player, message);
						
					}
				
				} }.runTaskLater(Main.arena, 40);
				
				new BukkitRunnable() { public void run() {
					
					for (Player player : Bukkit.getWorld("lobby").getPlayers()) {
						
						DisplayUtils.sendActionBar(player, message);
						
					}
				
				} }.runTaskLater(Main.arena, 80);
				
				messageType = messageType == 1 ? 2 : 1;
				
			}
			
		}.runTaskTimer(Main.arena, 1, 120);
		
		new BukkitRunnable() {
			
			public void run() {

				for (String id : ArenaUtils.getArenas()) {
					
					if (Bukkit.getWorld(ArenaUtils.getWorld(id)).getPlayers().size() > 1) {
						
						for (Player player : Bukkit.getWorld(ArenaUtils.getWorld(id)).getPlayers()) {
							
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
					
				}

			}
			
		}.runTaskTimer(Main.arena, 1, 600);
			
		new BukkitRunnable() {
			
			public void run() {

				for (String id : ArenaUtils.getArenas()) {
					
					if (Bukkit.getWorld(ArenaUtils.getWorld(id)).getPlayers().size() < 5) {
						
						ArenaUtils.setSize(id, 100);
						
					}
					else if ((Bukkit.getWorld(ArenaUtils.getWorld(id)).getPlayers().size() * 25) < WorldUtils.getSize(ArenaUtils.getWorld(id))) {
						
						ArenaUtils.setSize(id, (Bukkit.getWorld(ArenaUtils.getWorld(id)).getPlayers().size() * 25));
						
					}
					else {
						
						ArenaUtils.setSize(id, WorldUtils.getSize(ArenaUtils.getWorld(id)));
						
					}
					
				}

			}
			
		}.runTaskTimer(Main.arena, 1, 1200);
		
		new BukkitRunnable() {
			
			public void run() {

				for (Location block : Arena.blocks) {
					
					block.getBlock().setType(Material.AIR);
					
				}
				
				for (Player player : Arena.playerBlocks.keySet()) {
					
					for (Location block : Arena.playerBlocks.get(player)) {
						
						block.getBlock().setType(Material.AIR);
						
					}
					
				}
				
				for (Location block : Arena.water) {
					
					block.getBlock().setType(Material.STATIONARY_WATER);
					
				}
				
				Arena.blocks.clear();
				Arena.playerBlocks.clear();
				Arena.water.clear();

			}
			
		}.runTaskTimer(Main.arena, 1, 12000);
		
	}
	
	@Override
	public void onDisable() {
		
		getLogger().info("Arena UHC Plugin has been disabled.");
		
		for (Location block : Arena.blocks) {
			
			block.getBlock().setType(Material.AIR);
			
		}
		
		for (Player player : Arena.playerBlocks.keySet()) {
			
			for (Location block : Arena.playerBlocks.get(player)) {
				
				block.getBlock().setType(Material.AIR);
				
			}
			
		}
		
		for (Location block : Arena.water) {
			
			block.getBlock().setType(Material.STATIONARY_WATER);
			
		}
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			if (Arena.joinTime.containsKey(player)) {
				
				Calendar time = new GregorianCalendar();
				time.setTime(new Date(new Date().getTime() - Arena.joinTime.get(player)));
				int minutes = time.get(Calendar.MINUTE);
				PlayerUtils.setTime(player, PlayerUtils.getTime(player) + minutes);
				Arena.joinTime.remove(player);
				
			}
			
			PlayerUtils.updateScores(player);
			player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
			
		}
		
		Arena.scoreboard.clear();
		Arena.kills.clear();
		Arena.death.clear();
		Arena.killstreak.clear();
		Arena.longshot.clear();
		
		Arena.joinTime.clear();
		
		Arena.blocks.clear();
		Arena.playerBlocks.clear();
		Arena.water.clear();
		
		bar.removeAll();
		
		arena = null;
		
	}
	
	public static TextComponent getPrefixComponent() {
		
		TextComponent prefix1 = new TextComponent("Arena ");
		prefix1.setColor(ChatColor.GREEN);
		prefix1.setBold(true);
		TextComponent prefix2 = new TextComponent("⫸ ");
		prefix2.setColor(ChatColor.DARK_GRAY);
		prefix2.setBold(false);
		prefix1.addExtra(prefix2);
		
		return prefix1;
		
	}
	
}
