package me.razermc.CommandInterface.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.razermc.CommandInterface.Main;

public class playerLeaveEvent implements Listener {

	private Main plugin;

	public playerLeaveEvent(Main pl) {
		plugin = pl;
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		if (plugin.playerDatabase.contains(event.getPlayer().getUniqueId().toString())) {
			plugin.playerDatabase.set(event.getPlayer().getUniqueId().toString(), null);
		}

		if (plugin.playerCmdBuffer.contains(event.getPlayer().getUniqueId().toString())) {
			plugin.playerCmdBuffer.set(event.getPlayer().getUniqueId().toString(), null);

		}

	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		if (plugin.playerDatabase.contains(event.getPlayer().getUniqueId().toString())) {
			plugin.playerDatabase.set(event.getPlayer().getUniqueId().toString(), null);
		}

		if (plugin.playerCmdBuffer.contains(event.getPlayer().getUniqueId().toString())) {
			plugin.playerCmdBuffer.set(event.getPlayer().getUniqueId().toString(), null);

		}

	}

}
