package me.razermc.CommandInterface.utilities;

import me.razermc.CommandInterface.Main;

public class placeholderAPISupport {
	private Main plugin;
	public placeholderAPISupport(Main pl) {
		plugin = pl;
	}
	
	public void enablePAPI( ) {
	
	if (plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
		plugin.getLogger().info("PlaceholderAPI plugin detected, enabling support...");
		plugin.placeholderApi = true;
	} else {
		plugin.getLogger().info("PlaceholderAPI plugin not detected, disabling support...");
	}
	}
}
