package me.razermc.CommandInterface.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.razermc.CommandInterface.Main;

public class coinCommand implements CommandExecutor {

	private Main plugin;

	public coinCommand(Main pl) {
		plugin = pl;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] cmdArgs) {

		if (cmd.getName().equalsIgnoreCase("coin")) {
			Player player = (Player) sender;

			if (sender instanceof Player) {
				if (sender.hasPermission("coin.use")) {
					if (cmdArgs.length != 0) {
						if (cmdArgs[0].equalsIgnoreCase("reload")) {
							if (sender.hasPermission("coin.reload")) {
								plugin.configReload();
								plugin.msgSend(sender, "configLoaded");

							} else {
								plugin.msgSend(sender, "noPermission");
							}
						} else if (cmdArgs[0].equalsIgnoreCase("open")) {

							if (cmdArgs.length > 1) {

								if (plugin.menusDatabase.contains("menus." + cmdArgs[1])) {
									plugin.processGui(sender, cmdArgs[1]);

								} else {
									plugin.msgSend(sender, "invalidMenu");
								}

							} else {
								plugin.msgSend(sender, "invalidMenu");
							}

						} else if (cmdArgs[0].equalsIgnoreCase("list")) {
							if (sender.hasPermission("coin.list")) {
								sender.sendMessage(plugin.stringProc(player, null, plugin.langConfig.getString("pluginPrefix"), 0));
								sender.sendMessage(plugin.stringProc(player, null, "&2&lAvailable Menus", 0));
								for (String bufferString : plugin.menusDatabase.getConfigurationSection("menus")
										.getKeys(false)) {
									sender.sendMessage(plugin.stringProc(player, null, bufferString, 0));

								}
							} else {
								plugin.msgSend(sender, "noPermission");
							}
						} else {
							plugin.msgSend(sender, "invalidArgument");
						}
					} else {
						plugin.msgSend(sender, "help");
					}
				} else {
					plugin.msgSend(sender, "noPermission");
				}
			}

		}

		return false;
	}

}
