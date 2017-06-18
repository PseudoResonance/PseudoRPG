package tk.spongenetwork.RPG.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import tk.spongenetwork.RPG.XP.XPManager;

public class PlayerJoinEH implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		XPManager.addPlayerXP(e.getPlayer().getName());
	}
}
