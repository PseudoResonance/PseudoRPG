package io.github.pseudoresonance.pseudorpg.listeners;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.PlayerDataController;
import io.github.pseudoresonance.pseudorpg.PseudoRPG;
import io.github.pseudoresonance.pseudorpg.xp.XP;
import io.github.pseudoresonance.pseudorpg.xp.XPManager;
import io.github.pseudoresonance.pseudorpg.xp.XPType;

public class PlayerJoinLeaveL implements Listener {

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent e) {
		XP ret = new XP(e.getPlayer());
		HashMap<String, Object> settings;
		try {
			settings = PlayerDataController.getPlayerSettings(e.getPlayer().getUniqueId().toString()).get();
			for (XPType type : XPType.values()) {
				Object value = settings.get("xp" + type.toString().toLowerCase());
				if (value != null) {
					if (value instanceof Integer) {
						int xpValue = (Integer) value;
						ret.setInitialXP(type, xpValue);
					}
				}
			}
			XPManager.setPlayerXP(e.getPlayer(), ret);
			PlayerDataController.setPlayerSetting(e.getPlayer().getUniqueId().toString(), "ip", e.getRealAddress().getHostAddress());
		} catch (InterruptedException | ExecutionException e1) {
			PseudoRPG.plugin.getLogger().severe("Error while getting XP stats from database for player: " + e.getPlayer().getName());
			e1.printStackTrace();
		}
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		XP xp = XPManager.removePlayerXP(e.getPlayer());
		HashMap<String, Object> data = new HashMap<String, Object>();
		for (XPType type : XPType.values()) {
			data.put("xp" + type.toString().toLowerCase(), xp.getTotalXP(type));
		}
		PlayerDataController.setPlayerSettings(e.getPlayer().getUniqueId().toString(), data);
	}

}
