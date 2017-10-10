package me.razermc.CommandInterface.utilities;

import org.bukkit.plugin.RegisteredServiceProvider;

import me.razermc.CommandInterface.Main;
import net.milkbowl.vault.economy.Economy;

public class vaultSupport {
	private static Main plugin;
	public vaultSupport(Main pl) {
		plugin = pl;
	}
	
	public void vaultEnable() {
		if (plugin.getServer().getPluginManager().getPlugin("Vault") != null) {
			plugin.getLogger().info("Vault plugin detected, enabling economy support...");
			plugin.vaultAvailable = true;
			
			RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
			Main.econ = rsp.getProvider();
			
		} else {
			plugin.getLogger().info("Vault plugin not detected, disabling economy support...");
			plugin.vaultAvailable = false;
		}
	}
	
	
	
	
	

}
