package me.razermc.CommandInterface.utilities;

import me.razermc.CommandInterface.Main;
import me.razermc.CommandInterface.events.inventoryClickEvent;
import me.razermc.CommandInterface.events.inventoryCloseEvent;
import me.razermc.CommandInterface.events.playerLeaveEvent;
import me.razermc.CommandInterface.events.swapEvent;

public class registerEvents {
	private Main plugin;

	public registerEvents(Main pl) {
		plugin = pl;
	}

	public void regEvents() {

		plugin.getServer().getPluginManager().registerEvents(new inventoryClickEvent(plugin), plugin);
		plugin.getServer().getPluginManager().registerEvents(new inventoryCloseEvent(plugin), plugin);
		plugin.getServer().getPluginManager().registerEvents(new playerLeaveEvent(plugin), plugin);
		if (plugin.serverVersion.contains("1.9") || plugin.serverVersion.contains("1.10")
				|| plugin.serverVersion.contains("1.11") || plugin.serverVersion.contains("1.12")) {
			plugin.getServer().getPluginManager().registerEvents(new swapEvent(plugin), plugin);
		}

	}

}
