package me.razermc.CommandInterface.utilities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import me.razermc.CommandInterface.Main;

public class placeholderProcessor {
	private Main plugin;
	public placeholderProcessor(Main pl) {
		plugin = pl;
	}
	
	public String pProc(Player player, String input, String text, double price) {
		
		if (plugin.placeholderApi) {
			text = PlaceholderAPI.setPlaceholders(player, text);
		}
		if (player != null) {
		text = text.replaceAll("%cPlayer%", player.getName());
		}
		
		if (text != null) {	
		text = text.replaceAll("%cInput%", input);
		}
		
		if (price != 0) {
			text = text.replaceAll("%cPrice%", String.valueOf(price));
		}
		
		text = ChatColor.translateAlternateColorCodes('&', text);
		
		return text;
	}
	
	public void sendMessage(CommandSender sender, String message) {
		if (!plugin.mainConfig.getBoolean("silentMode")) {
			if (message.equalsIgnoreCase("help")) {
				sender.sendMessage(
						ChatColor.translateAlternateColorCodes('&', plugin.langConfig.getString("pluginPrefix")));
				for (String messageBuffer : plugin.langConfig.getStringList(message)) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messageBuffer));
				}

			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						plugin.langConfig.getString("pluginPrefix") + " " + plugin.langConfig.getString(message)));
			}
		}
	}
}
