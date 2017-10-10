package me.razermc.CommandInterface.events;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.razermc.CommandInterface.Main;
import net.md_5.bungee.api.ChatColor;

public class inventoryClickEvent implements Listener {
	private Main plugin;

	public inventoryClickEvent(Main pl) {
		plugin = pl;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {

		if (event.getCurrentItem() == null) {
			return;
		}

		if (event.getCurrentItem().getItemMeta() == null) {
			return;
		}

		if (plugin.mainConfig.getBoolean("clickCooldownEnabled")) {
			if (plugin.playerClickCooldownDatabase.contains(event.getWhoClicked().getUniqueId().toString())) {
				event.setCancelled(true);
				plugin.msgSend(event.getWhoClicked(), "clickCooldown");
				if (plugin.mainConfig.getBoolean("closeMenuOnCooldown")) {
					event.getWhoClicked().closeInventory();
				}
			}

			if (!plugin.playerClickCooldownDatabase.contains(event.getWhoClicked().getUniqueId().toString())) {
				plugin.playerClickCooldownDatabase.createSection(event.getWhoClicked().getUniqueId().toString());

				new BukkitRunnable() {
					@Override
					public void run() {
						String puuId = event.getWhoClicked().getUniqueId().toString();
						plugin.playerClickCooldownDatabase.set(puuId, null);
					}
				}.runTaskLater(plugin, plugin.mainConfig.getInt("clickCooldown"));
			}
		}
		String message = null;
		Player player = (Player) event.getWhoClicked();
		if (plugin.playerDatabase.contains(event.getWhoClicked().getUniqueId().toString())) {
			if (event.getInventory().getName().equalsIgnoreCase(
					plugin.playerDatabase.getString(player.getUniqueId().toString() + ".menuDisplayName"))) {
				event.setCancelled(true);
				FileConfiguration configBuffer = new YamlConfiguration();
				String menuName = plugin.playerDatabase.getString(player.getUniqueId().toString() + ".menuName");
				try {
					configBuffer.loadFromString(plugin.menusDatabase.getString("menus." + menuName + ".config"));
				} catch (InvalidConfigurationException e) {
					e.printStackTrace();
				}
				ItemMeta currentItem = event.getCurrentItem().getItemMeta();

				ArrayList<String> commandBuffer = new ArrayList<String>();
				String function = null;
				boolean closeMenu = false;
				double price = 0;
				String permission = null;
				String menuModeName = null;

				for (String itemNameBuffer : configBuffer.getConfigurationSection("Menu").getKeys(false)) {

					ItemStack myItemStack = new ItemStack(
							Material.matchMaterial(configBuffer.getString("Menu." + itemNameBuffer + ".type")));
					ItemMeta configItemMeta = myItemStack.getItemMeta();
					configItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
							configBuffer.getString("Menu." + itemNameBuffer + ".name")));

					ArrayList<String> itemLore = new ArrayList<String>();
					price = configBuffer.getDouble("Menu." + itemNameBuffer + ".price");
					for (String loreBuffer : configBuffer.getStringList("Menu." + itemNameBuffer + ".lore")) {
						itemLore.add(plugin.stringProc(player, null, loreBuffer, price));
					}
					configItemMeta.setLore(itemLore);

					if (currentItem.equals(configItemMeta)) {

						commandBuffer = (ArrayList<String>) configBuffer
								.getStringList("Menu." + itemNameBuffer + ".commands");
						function = configBuffer.getString("Menu." + itemNameBuffer + ".function");
						closeMenu = configBuffer.getBoolean("Menu." + itemNameBuffer + ".closeMenu");
						price = configBuffer.getDouble("Menu." + itemNameBuffer + ".price");
						permission = configBuffer.getString("Menu." + itemNameBuffer + ".permission");
						message = configBuffer.getString("Menu." + itemNameBuffer + ".message");
						menuModeName = configBuffer.getString("Menu." + itemNameBuffer + ".menuName");
						
						break;
					} else {
						commandBuffer = null;
					}
				}
				if (!(permission == null)) {
					if (!permission.equalsIgnoreCase("none")) {
						if (!player.hasPermission(permission)) {
							plugin.msgSend(player, "noPermission");
							if (closeMenu) {
								player.closeInventory();
								return;
							}
							return;
						}
					}
				}

				if (commandBuffer == null) {
					return;
				}

				if (function.equalsIgnoreCase("none")) {
					plugin.cmdProc(player, commandBuffer, price, message);

					if (closeMenu) {
						player.closeInventory();
					}

				} else if (function.equalsIgnoreCase("player")) {
					plugin.playerCmdBuffer.set(player.getUniqueId().toString() + ".commands", commandBuffer);
					plugin.playerCmdBuffer.set(player.getUniqueId().toString() + ".price", price);
					plugin.playerCmdBuffer.set(player.getUniqueId().toString() + ".message", message);

					plugin.playerSelection(player);
				} else if (function.equalsIgnoreCase("menu")) {
					player.closeInventory();
					plugin.processGui(event.getWhoClicked(), menuModeName);

				}

			}

		}

		if (event.getInventory().getName().equalsIgnoreCase(
				ChatColor.translateAlternateColorCodes('&', plugin.mainConfig.getString("menuTitle")))) {
			event.setCancelled(true);

			ArrayList<String> cmd = new ArrayList<String>();
			double price = plugin.playerCmdBuffer.getDouble(player.getUniqueId().toString() + ".price");
			for (String cmdBuffer : plugin.playerCmdBuffer
					.getStringList(player.getUniqueId().toString() + ".commands")) {
				cmdBuffer = cmdBuffer.replaceAll("%cInput%",
						event.getCurrentItem().getItemMeta().getDisplayName().trim());
				cmd.add(plugin.stringProc(player, event.getCurrentItem().getItemMeta().getDisplayName(), cmdBuffer,
						price));
			}
			message = plugin.playerCmdBuffer.getString(player.getUniqueId().toString() + ".message");
			plugin.cmdProc(player, cmd, price, message);

			player.closeInventory();

		}

		return;
	}

}
