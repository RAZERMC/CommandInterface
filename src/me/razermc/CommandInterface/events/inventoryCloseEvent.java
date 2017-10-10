package me.razermc.CommandInterface.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import me.razermc.CommandInterface.Main;
import net.md_5.bungee.api.ChatColor;

public class inventoryCloseEvent implements Listener {
	private Main plugin;

	public inventoryCloseEvent(Main pl) {
		plugin = pl;
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {

		if (plugin.playerDatabase.contains(event.getPlayer().getUniqueId().toString())) {
			plugin.playerDatabase.set(event.getPlayer().getUniqueId().toString(), null);
		}
		if (event.getInventory().getName().equalsIgnoreCase(
				ChatColor.translateAlternateColorCodes('&', plugin.mainConfig.getString("menuTitle")))) {
			if (plugin.playerCmdBuffer.contains(event.getPlayer().getUniqueId().toString())) {
				plugin.playerCmdBuffer.set(event.getPlayer().getUniqueId().toString(), null);

			}
		}
	}
}
