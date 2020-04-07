package io.github.pseudoresonance.pseudorpg.xp;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class XPManager {
	
	private static HashMap<String, XP> xp = new HashMap<String, XP>();
	
	public static void setXP(HashMap<String, XP> xp) {
		XPManager.xp = xp;
	}
	
	public static HashMap<String, XP> getXP() {
		return XPManager.xp;
	}
	
	public static XP getPlayerXP(Player p) {
		return xp.get(p.getUniqueId().toString());
	}
	
	public static XP setPlayerXP(Player p, XP xpObj) {
		xp.put(p.getUniqueId().toString(), xpObj);
		return xpObj;
	}
	
	public static XP addPlayerXP(Player p) {
		XP xpObj = xp.get(p.getUniqueId().toString());
		if (xpObj == null) {
			xpObj = new XP(p);
			xp.put(p.getUniqueId().toString(), xpObj);
		}
		return xpObj;
	}
	
	public static XP removePlayerXP(Player p) {
		if (xp.containsKey(p.getUniqueId().toString())) {
			return xp.remove(p.getUniqueId().toString());
		}
		return null;
	}

}
