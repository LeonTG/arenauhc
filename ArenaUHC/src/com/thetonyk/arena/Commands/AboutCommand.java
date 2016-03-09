package com.thetonyk.arena.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.thetonyk.arena.Main;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class AboutCommand implements CommandExecutor {
	
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	
    	Player player = Bukkit.getPlayer(sender.getName());
        	
    	if (!sender.hasPermission("arena.about")) {
    		
    		sender.sendMessage(Main.NO_PERMS);
    		return true;
    	}
    	
        sender.sendMessage(Main.PREFIX + "Plugin by TheTonyk for CommandsPVP.");
        TextComponent hover1 = new TextComponent("Visit the Twitter of ");
        hover1.setColor(ChatColor.GRAY);
        TextComponent hover2 = new TextComponent("TheTonyk");
        hover2.setColor(ChatColor.GREEN);
        TextComponent hover3 = new TextComponent(".");
        hover3.setColor(ChatColor.GRAY);
        BaseComponent[] hover = { hover1, hover2, hover3 };
        TextComponent message = new TextComponent("Twitter: ");
        message.setColor(ChatColor.GRAY);
        TextComponent twitter = new TextComponent("@TheTonyk_");
        twitter.setColor(ChatColor.AQUA);
        twitter.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://twitter.com/TheTonyk_"));
        twitter.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover));
        BaseComponent[] finalMessage = { Main.PREFIX_COMPONENT, message, twitter };
        player.spigot().sendMessage(finalMessage);

    	return true;
    	
    }

}
