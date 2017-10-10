package me.razermc.CommandInterface.gui;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.razermc.CommandInterface.Main;
import net.md_5.bungee.api.ChatColor;

public class coinGui {

	private Main plugin;

	public coinGui(Main pl) {
		plugin = pl;
	}

	public void showMenu(CommandSender sender, String menuName) {

		
		ItemStack myItemStack;
		Inventory inv = null;
		FileConfiguration configBuffer = new YamlConfiguration();
		int maxSlots = 9;
		Player player = (Player) sender;
		
		if (plugin.playerDatabase.contains(player.getUniqueId().toString())) {
			plugin.playerDatabase.set(player.getUniqueId().toString(), null);
		}

		if (plugin.playerCmdBuffer.contains(player.getUniqueId().toString())) {
			plugin.playerCmdBuffer.set(player.getUniqueId().toString(), null);

		}

		try {
			configBuffer.loadFromString(plugin.menusDatabase.getString("menus." + menuName + ".config"));
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
		int inventoryPosition;
		for (String menuItem1 : configBuffer.getConfigurationSection("Menu").getKeys(false)) {

			inventoryPosition = configBuffer.getInt("Menu." + menuItem1 + ".inventoryPosition");
			if (inventoryPosition >= 10) {
				maxSlots = 18;
			}
			if (inventoryPosition >= 19) {
				maxSlots = 27;
			}
			if (inventoryPosition >= 28) {
				maxSlots = 36;
			}
			if (inventoryPosition >= 37) {
				maxSlots = 45;
			}
			if (inventoryPosition >= 46) {
				maxSlots = 54;
			}

		}
		String invName = plugin.stringProc(player, null, configBuffer.getString("menuDisplayName"), 0);
		inv = plugin.getServer().createInventory(null, maxSlots, invName);

		for (String itemBuffer : configBuffer.getConfigurationSection("Menu").getKeys(false)) {
			myItemStack = new ItemStack(Material.matchMaterial(configBuffer.getString("Menu." + itemBuffer + ".type",
					configBuffer.getString("Menu." + itemBuffer + ".amount"))));

			ItemMeta myMeta = myItemStack.getItemMeta();
			myMeta.setDisplayName(plugin.stringProc(player, null, configBuffer.getString("Menu." + itemBuffer + ".name"), 0));
			
			ArrayList<String> itemLore = new ArrayList<String>();
			for (String loreBuffer : configBuffer.getStringList("Menu." + itemBuffer + ".lore")) {
				itemLore.add(plugin.stringProc(player, null, loreBuffer, configBuffer.getDouble("Menu." + itemBuffer + ".price")));
			}
			myMeta.setLore(itemLore);
			myItemStack.setItemMeta(myMeta);
			myItemStack.setDurability((short) configBuffer.getInt("Menu." + itemBuffer + ".durability"));
			String permission = configBuffer.getString("Menu." + itemBuffer + ".permission");
			if (!permission.equalsIgnoreCase("none")) {
				if (plugin.mainConfig.getBoolean("hideItemsWithNoPermission")) {
					if (player.hasPermission(permission)) {
						inv.setItem(configBuffer.getInt("Menu." + itemBuffer + ".inventoryPosition") - 1, myItemStack);
					}
				}
			} else {

				inv.setItem(configBuffer.getInt("Menu." + itemBuffer + ".inventoryPosition") - 1, myItemStack);
			}
		}
		plugin.playerDatabase.set(player.getUniqueId().toString() + ".menuName", configBuffer.getString("menuName"));
		plugin.playerDatabase.set(player.getUniqueId().toString() + ".menuDisplayName",
				ChatColor.translateAlternateColorCodes('&', configBuffer.getString("menuDisplayName")));
		player.openInventory(inv);
	}

}
