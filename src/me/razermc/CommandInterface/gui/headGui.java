package me.razermc.CommandInterface.gui;

import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.razermc.CommandInterface.Main;

public class headGui {

	private Main plugin;

	public headGui(Main pl) {
		plugin = pl;
	}

	public void gui(Player player) {
		int maxSlots = 9;
		int row = plugin.getServer().getOnlinePlayers().size();

		if (row >= 10) {
			maxSlots = 18;
		}
		if (row >= 19) {
			maxSlots = 27;
		}
		if (row >= 28) {
			maxSlots = 36;
		}
		if (row >= 37) {
			maxSlots = 45;
		}
		if (row >= 46) {
			maxSlots = 54;
		}

		Inventory inv = plugin.getServer().createInventory(null, maxSlots,
				plugin.stringProc(player, null, plugin.mainConfig.getString("menuTitle"), 0));
		int i = 0;
		for (Player playerName : plugin.getServer().getOnlinePlayers()) {

			if (player != playerName) {
				ItemStack head = new ItemStack(Material.SKULL_ITEM, 1);
				ItemMeta myMeta = head.getItemMeta();
				myMeta.setDisplayName(playerName.getName());

				ArrayList<String> itemLore = new ArrayList<String>();
				for (String loreBuffer : plugin.mainConfig.getStringList("headLores")) {
					itemLore.add(plugin.stringProc(playerName, playerName.getName(), loreBuffer, 0));
				}
				myMeta.setLore(itemLore);

				head.setItemMeta(myMeta);
				head.setDurability((short) 3);

				inv.setItem(i, head);
				i = i + 1;
			}

		}

		player.openInventory(inv);
	}

}
