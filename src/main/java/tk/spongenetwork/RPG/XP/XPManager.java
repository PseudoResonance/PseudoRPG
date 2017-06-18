package tk.spongenetwork.RPG.XP;

import java.util.HashMap;

import io.github.wolfleader116.wolfapi.bukkit.WolfAPI;

public class XPManager {
	
	private static HashMap<String, XP> xp = new HashMap<String, XP>();
	
	public static void setXP(HashMap<String, XP> xp) {
		XPManager.xp = xp;
	}
	
	public static HashMap<String, XP> getXP() {
		return XPManager.xp;
	}
	
	public static XP getPlayerXP(String name) {
		return xp.get(WolfAPI.getPlayerUUID(name));
	}
	
	public static void addPlayerXP(String name) {
		if (!xp.containsKey(WolfAPI.getPlayerUUID(name))) {
			xp.put(WolfAPI.getPlayerUUID(name), new XP(WolfAPI.getPlayerUUID(name)));
		}
	}
	
	public static void removePlayerXP(String name) {
		if (xp.containsKey(WolfAPI.getPlayerUUID(name))) {
			xp.remove(WolfAPI.getPlayerUUID(name));
		}
	}

}
