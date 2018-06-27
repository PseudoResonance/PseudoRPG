package io.github.pseudoresonance.pseudorpg.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import io.github.pseudoresonance.pseudorpg.xp.XPManager;

public class PlayerJoinEH implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		XPManager.addPlayerXP(e.getPlayer().getName());
	}
}
