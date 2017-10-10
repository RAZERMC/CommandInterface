package me.razermc.CommandInterface.utilities;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.razermc.CommandInterface.Main;

public class configProcessor {

	private Main plugin;

	public configProcessor(Main pl) {
		plugin = pl;
	}

	public void configReload() {
		// Check if configuration files exist
		plugin.getLogger().info("Checking main config file....");
		File configFile = new File(plugin.getDataFolder(), "config.yml");
		if (!configFile.exists()) {
			plugin.getLogger().info("Main config file missing. Creating....");
			plugin.saveResource("config.yml", false);
		}

		plugin.getLogger().info("Checking language config file....");
		File langFile = new File(plugin.getDataFolder(), "lang.yml");
		if (!langFile.exists()) {
			plugin.getLogger().info("Main language config file missing. Creating....");
			plugin.saveResource("lang.yml", false);
		}

		plugin.getLogger().info("Loading Configs....");
		plugin.mainConfig = YamlConfiguration.loadConfiguration(configFile);
		plugin.langConfig = YamlConfiguration.loadConfiguration(langFile);
		plugin.getLogger().info("Configs Loaded");

		plugin.getLogger().info("Checking for menus");
		File sampleMenu = new File(plugin.getDataFolder() + "\\menus\\sampleMenu.yml");
		if (!sampleMenu.exists()) {
			plugin.getLogger().info("sampleMenu.yml not detected. Creating....");
			plugin.saveResource("menus\\sampleMenu.yml", false);

		}
		
		File anotherMenu = new File(plugin.getDataFolder() + "\\menus\\anotherMenu.yml");
		if (!anotherMenu.exists()) {
			plugin.getLogger().info("anotherMenu.yml not detected. Creating....");
			plugin.saveResource("menus\\anotherMenu.yml", false);

		}
		
		File menuDir = new File(plugin.getDataFolder() + "\\menus");
		if (!menuDir.exists()) {
			plugin.getLogger().info("Menu folder not found. Creating....");
			menuDir.mkdirs();
		}
		plugin.getLogger().info("Loading menus");
		int i = 0;
		FileConfiguration menuConfigBuffer = new YamlConfiguration();
		String configStringBuffer;
		String menuName;

		if (plugin.menusDatabase.contains("menus")) {
			plugin.menusDatabase.set("menus", null);
		}
		if (!plugin.menusDatabase.contains("menus")) {
			plugin.menusDatabase.createSection("menus");
		}
		plugin.menusDatabase.set("menus", null);
		for (File menuConfigBufferFile : menuDir.listFiles()) {
			menuConfigBuffer = YamlConfiguration.loadConfiguration(menuConfigBufferFile);
			menuName = menuConfigBuffer.getString("menuName");
			configStringBuffer = menuConfigBuffer.saveToString();

			plugin.menusDatabase.createSection("menus." + menuName);
			plugin.menusDatabase.createSection("menus." + menuName + ".filename");
			plugin.menusDatabase.createSection("menus." + menuName + ".config");
			plugin.menusDatabase.set("menus." + menuName + ".config", configStringBuffer);
			plugin.menusDatabase.set("menus." + menuName + ".filename", menuConfigBufferFile.getName());
			i = i + 1;
		}

		plugin.getLogger().info("Loaded Menus: " + i);
	}

}
