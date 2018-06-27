package io.github.pseudoresonance.pseudorpg.xp;

import java.util.HashMap;

import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.PlayerDataController;

public class XPManager {
	
	private static HashMap<String, XP> xp = new HashMap<String, XP>();
	
	public static void setXP(HashMap<String, XP> xp) {
		XPManager.xp = xp;
	}
	
	public static HashMap<String, XP> getXP() {
		return XPManager.xp;
	}
	
	public static XP getPlayerXP(String name) {
		return xp.get(PlayerDataController.getUUID(name));
	}
	
	public static void addPlayerXP(String name) {
		if (!xp.containsKey(PlayerDataController.getUUID(name))) {
			xp.put(PlayerDataController.getUUID(name), new XP(PlayerDataController.getUUID(name)));
		}
	}
	
	public static void removePlayerXP(String name) {
		if (xp.containsKey(PlayerDataController.getUUID(name))) {
			xp.remove(PlayerDataController.getUUID(name));
		}
	}

}
