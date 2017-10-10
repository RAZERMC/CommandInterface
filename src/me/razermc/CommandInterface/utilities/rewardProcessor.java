package me.razermc.CommandInterface.utilities;

import java.util.ArrayList;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import me.razermc.CommandInterface.Main;
import net.md_5.bungee.api.ChatColor;

public class rewardProcessor {

	private Main plugin;
	public rewardProcessor(Main pl) {
		plugin = pl;
	}

	public void executeCmd(Player player, ArrayList<String> cmds, double price, String message) {
		if (plugin.vaultAvailable) {
			if (plugin.mainConfig.getBoolean("economyEnabled")) {
				boolean balance = plugin.ecoProc(player, price, "balance");
				if (balance) {
					plugin.ecoProc(player, price, "withdraw");
				} else {
					return;
				}
			}
		}

		for (String cmdBuffer : cmds) {

			cmdBuffer = ChatColor.translateAlternateColorCodes('&',
					cmdBuffer.replaceAll("%cPlayer%", player.getName()).trim());

			if (cmdBuffer.startsWith("-c-")) {
				cmdBuffer = cmdBuffer.replaceAll("-c-", "").trim();
				ConsoleCommandSender console = plugin.getServer().getConsoleSender();
				plugin.getServer().dispatchCommand(console, cmdBuffer);
			} else {
				player.performCommand(cmdBuffer);
			}

		}
		
		if (message != null && !message.equalsIgnoreCase("null") && !message.equalsIgnoreCase("")) {
			player.sendMessage(plugin.stringProc(player, null, plugin.langConfig.getString("pluginPrefix") + ": " + message, price));
		}
		
		
	}
}
