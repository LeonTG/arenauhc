package com.thetonyk.arena.Utils;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.thetonyk.arena.Main;

import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_9_R1.MinecraftServer;
import net.minecraft.server.v1_9_R1.PacketPlayOutChat;
import net.minecraft.server.v1_9_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_9_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_9_R1.PacketPlayOutTitle.EnumTitleAction;
import net.minecraft.server.v1_9_R1.PlayerConnection;

public class DisplayUtils {
	
	public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		
		PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;

		IChatBaseComponent jsonTitle = ChatSerializer.a("{\"text\":\"" + title + "\"}");
		IChatBaseComponent jsonSubtitle = ChatSerializer.a("{\"text\":\"" + subtitle + "\"}");

		PacketPlayOutTitle sendTitle = new PacketPlayOutTitle(EnumTitleAction.TITLE, jsonTitle, fadeIn, stay, fadeOut);
		PacketPlayOutTitle sendSubtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, jsonSubtitle);

		connection.sendPacket(sendSubtitle);
		connection.sendPacket(sendTitle);
		
	}
	
	public static void sendActionBar(Player player, String message){
		
		PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
		
		IChatBaseComponent jsonText = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message + "\"}");
		PacketPlayOutChat packet = new PacketPlayOutChat(jsonText, (byte) 2);
		
		connection.sendPacket(packet);
	}

	public static void sendTab(Player player) {
		
		PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
		
		String name = player.getName();
		int ping = ((CraftPlayer)player).getHandle().ping;
		
		@SuppressWarnings("deprecation")
		double rawTps = MinecraftServer.getServer().recentTps[0];
		DecimalFormat format = new DecimalFormat("00.00");
		String tps = format.format(rawTps);
		
		int players = 0;
		
		if (!Bukkit.getOnlinePlayers().isEmpty()) {
			
			for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
				
				if (onlinePlayer.isOnline()) {
				
					players++;
					
				}
			}
		}
		
		IChatBaseComponent jsonHeader = ChatSerializer.a("{\"text\":\"\n §7Welcome on the Arena, §a" + name + " §7! \n §b@CommandsPVP  §7⋯  §aTS: §bcommandspvp.com \n\"}");
		IChatBaseComponent jsonFooter = ChatSerializer.a("{\"text\":\"\n §7Players: §a" + players + "  §7⋯  Ping: §a" + ping + "ms  §7⋯  TPS: §a" + tps + " \n\"}");
		
		PacketPlayOutPlayerListHeaderFooter tabHeader = new PacketPlayOutPlayerListHeaderFooter(jsonHeader);
		
		try {
			
			Field sendTab = tabHeader.getClass().getDeclaredField("b");
			sendTab.setAccessible(true);
			sendTab.set(tabHeader, jsonFooter);
			sendTab.setAccessible(!sendTab.isAccessible());
			
		} catch (Exception e) {
			
			Bukkit.getLogger().severe("§cError to set tab header & footer to " + name);
			
		}
		
		connection.sendPacket(tabHeader);
		
	}
	
	public static void versionName() {
		
		final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
		
		protocolManager.addPacketListener(new PacketAdapter(Main.arena, ListenerPriority.NORMAL, Arrays.asList(PacketType.Status.Server.OUT_SERVER_INFO), ListenerOptions.ASYNC) {
					
			@Override
			public void onPacketSending(PacketEvent event) {
				
				event.getPacket().getServerPings().read(0).setVersionName("§71.9.x §a| §6No-cooldown");
				event.getPacket().getServerPings().read(0).setPlayers(Arrays.asList(
					new WrappedGameProfile(UUID.randomUUID(), " "),
					new WrappedGameProfile(UUID.randomUUID(), "§r                §6§k|||§r §a§lCommandsPVP Arena §6§k|||"),
					new WrappedGameProfile(UUID.randomUUID(), " "),
					new WrappedGameProfile(UUID.randomUUID(), "§7  CommandsPVP is now updated to §aMinecraft 1.9 §7!  "),
					new WrappedGameProfile(UUID.randomUUID(), "§7  There are no cooldown in the main arena, so enjoy.  "),
					new WrappedGameProfile(UUID.randomUUID(), "§7  You can try the cooldown PVP in the second arena.  "),
					new WrappedGameProfile(UUID.randomUUID(), " "),
					new WrappedGameProfile(UUID.randomUUID(), "§r                          §6§lGood fight !"),
					new WrappedGameProfile(UUID.randomUUID(), " ")
				));
				
				
			}
				
		});
		
	}
	
}
