package me.razermc.CommandInterface.events;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import me.razermc.CommandInterface.Main;

public class swapEvent implements Listener {
	private Main plugin;

	public swapEvent(Main pl) {
		plugin = pl;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onItemSwap(PlayerSwapHandItemsEvent event) {
		Player player = event.getPlayer();
		double price = plugin.mainConfig.getDouble("FKeyPrice");
		if (plugin.mainConfig.getBoolean("FKeyEnabled")) {

			if (plugin.mainConfig.getString("FKeyMode").equalsIgnoreCase("menu")) {
				event.setCancelled(true);

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
				plugin.processGui(event.getPlayer(), plugin.mainConfig.getString("FKeyMenu"));
			}
			ArrayList<String> cmd = new ArrayList<String>();
			if (plugin.mainConfig.getString("FKeyMode").equalsIgnoreCase("command")) {
				event.setCancelled(true);
				for (String cmdBuffer : plugin.mainConfig.getStringList("FKeyCommand")) {
					cmd.add(plugin.stringProc(event.getPlayer(), null, cmdBuffer, price));
				}
				plugin.cmdProc(player, cmd, price, null);
			}
		}

	}
}
