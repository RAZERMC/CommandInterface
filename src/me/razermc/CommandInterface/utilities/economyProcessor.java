package me.razermc.CommandInterface.utilities;

import org.bukkit.entity.Player;

import me.razermc.CommandInterface.Main;
import net.milkbowl.vault.economy.EconomyResponse;

public class economyProcessor {
	private Main plugin;

	public economyProcessor(Main pl) {
		plugin = pl;
	}

	@SuppressWarnings("deprecation")
	public boolean economyMain(Player player, double amount, String mode) {
		if (mode.equalsIgnoreCase("balance")) {
			if (Main.econ.getBalance(player.getName()) >= amount) {
				return true;
			} else {
				plugin.msgSend(player, "notEnoughBalance");
			}
		}
		if (mode.equalsIgnoreCase("withdraw")) {
			EconomyResponse r = Main.econ.withdrawPlayer(player, amount);
			if (r.transactionSuccess()) {
				plugin.msgSend(player, "transactionSuccess");
				return true;
			} else {
				plugin.msgSend(player, "transactionFailed");
			}
		}
		if (mode.equalsIgnoreCase("deposit")) {
			EconomyResponse r = Main.econ.depositPlayer(player, amount);
			if (r.transactionSuccess()) {
				plugin.msgSend(player, "transactionSuccess");
				return true;
			} else {
				plugin.msgSend(player, "transactionFailed");
			}
		}
		return false;
	}

}
