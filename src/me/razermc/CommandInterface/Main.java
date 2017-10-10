package me.razermc.CommandInterface;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.razermc.CommandInterface.commands.coinCommand;
import me.razermc.CommandInterface.gui.coinGui;
import me.razermc.CommandInterface.gui.headGui;
import me.razermc.CommandInterface.utilities.configProcessor;
import me.razermc.CommandInterface.utilities.economyProcessor;
import me.razermc.CommandInterface.utilities.placeholderAPISupport;
import me.razermc.CommandInterface.utilities.placeholderProcessor;
import me.razermc.CommandInterface.utilities.registerEvents;
import me.razermc.CommandInterface.utilities.rewardProcessor;
import me.razermc.CommandInterface.utilities.vaultSupport;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
	
	public FileConfiguration mainConfig = new YamlConfiguration();
	public FileConfiguration langConfig = new YamlConfiguration();
	public FileConfiguration menusDatabase = new YamlConfiguration();
	public FileConfiguration playerDatabase = new YamlConfiguration();
	public FileConfiguration playerCmdBuffer = new YamlConfiguration();
	public FileConfiguration playerClickCooldownDatabase = new YamlConfiguration();
	public String serverVersion = this.getServer().getVersion();
	public boolean vaultAvailable = false;
	public static Economy econ = null;
	public boolean placeholderApi = false;
	
	private configProcessor reload = new configProcessor(this);
	private economyProcessor economyProc = new economyProcessor(this);
	private coinGui menuProc = new coinGui(this);
	private placeholderAPISupport placeHolder = new placeholderAPISupport(this);
	private rewardProcessor rewardProc = new rewardProcessor(this);
	private headGui playerSelect = new headGui(this);
	private vaultSupport vSupport = new vaultSupport(this);
	private registerEvents regEvents = new registerEvents(this);
	private placeholderProcessor pProc = new placeholderProcessor(this); 



	@Override
	public void onEnable() {
		reload.configReload();
		placeHolder.enablePAPI();
		vSupport.vaultEnable();
		regEvents.regEvents();
		this.getCommand("coin").setExecutor(new coinCommand(this));

	}

	public void configReload() {
		for (Player player : this.getServer().getOnlinePlayers()) {
			player.closeInventory();
		}
		reload.configReload();
	}

	public void msgSend(CommandSender sender, String message) {
		pProc.sendMessage(sender, message);
	}

	public void processGui(CommandSender sender, String menuName) {
		menuProc.showMenu(sender, menuName);
	}

	public void cmdProc(Player player, ArrayList<String> cmds, double price, String message) {
		rewardProc.executeCmd(player, cmds, price, this.stringProc(player, null, message, price));
	}

	public void playerSelection(Player player) {
		playerSelect.gui(player);
	}
	
	public boolean ecoProc(Player player, double amount, String mode) {
		boolean transactionSuccess = economyProc.economyMain(player, amount, mode);
		return transactionSuccess;
	}

	public String stringProc(Player player, String input, String text, double price) {
		text = pProc.pProc(player, input, text, price);
		return text;
	}
	
	
	@Override
	public void onDisable() {
		for (Player player : this.getServer().getOnlinePlayers()) {
			player.closeInventory();
		}

	}

}
