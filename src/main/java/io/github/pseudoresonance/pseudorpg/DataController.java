package io.github.pseudoresonance.pseudorpg;

import java.util.HashMap;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import io.github.pseudoresonance.pseudoapi.bukkit.Config;
import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.PlayerDataController;
import io.github.pseudoresonance.pseudorpg.xp.XP;
import io.github.pseudoresonance.pseudorpg.xp.XPManager;
import io.github.pseudoresonance.pseudorpg.xp.XPType;
import io.github.pseudoresonance.pseudorpg.xp.XPTypeYield;
import io.github.pseudoresonance.pseudorpg.xp.XPYield;

public class DataController {

	public static HashMap<String, XPYield> breakYield = new HashMap<String, XPYield>();
	public static HashMap<String, XPYield> smeltYield = new HashMap<String, XPYield>();
	public static HashMap<EntityType, XPYield> huntYield = new HashMap<EntityType, XPYield>();
	private static Config xp = new Config("xp.yml", PseudoRPG.plugin);
	
	public static void updateBackend() {
		loadXP();
	}
	
	public static void loadXP() {
		xp.reload();
		ConfigurationSection se1 = DataController.xp.getConfig().getConfigurationSection("breaking");
		if (se1 != null) {
			for (String key : se1.getKeys(false)) {
				XPYield xpy = new XPYield();
				ConfigurationSection cs = DataController.xp.getConfig().getConfigurationSection("breaking." + key);
				if (cs != null) {
					for (String category : cs.getKeys(false)) {
						int value = DataController.xp.getConfig().getInt("breaking." + key + "." + category);
						XPTypeYield xpty = new XPTypeYield(XPType.valueOf(category.toUpperCase()), value);
						xpy.addYield(xpty);
					}
					breakYield.put(key, xpy);
				}
			}
		}
		ConfigurationSection se2 = DataController.xp.getConfig().getConfigurationSection("smelting");
		if (se2 != null) {
			for (String key : se2.getKeys(false)) {
				XPYield xpy = new XPYield();
				ConfigurationSection cs = DataController.xp.getConfig().getConfigurationSection("smelting." + key);
				if (cs != null) {
					for (String category : cs.getKeys(false)) {
						int value = DataController.xp.getConfig().getInt("smelting." + key + "." + category);
						XPTypeYield xpty = new XPTypeYield(XPType.valueOf(category.toUpperCase()), value);
						xpy.addYield(xpty);
					}
					smeltYield.put(key, xpy);
				}
			}
		}
		ConfigurationSection se3 = DataController.xp.getConfig().getConfigurationSection("hunting");
		if (se3 != null) {
			for (String key : se3.getKeys(false)) {
				XPYield xpy = new XPYield();
				ConfigurationSection cs = DataController.xp.getConfig().getConfigurationSection("hunting." + key);
				if (cs != null) {
					for (String category : cs.getKeys(false)) {
						int value = DataController.xp.getConfig().getInt("hunting." + key + "." + category);
						XPTypeYield xpty = new XPTypeYield(XPType.valueOf(category.toUpperCase()), value);
						xpy.addYield(xpty);
					}
					EntityType put = null;
					for (EntityType et : EntityType.values()) {
						if (et.toString().equalsIgnoreCase(key)) {
							put = et;
						}
					}
					if (put != null) {
						huntYield.put(put, xpy);
					}
				}
			}
		}
	}
	
	public static boolean saveXP() {
		HashMap<String, XP> xp = XPManager.getXP();
		for (String uuid : xp.keySet()) {
			XP xpobj = xp.get(uuid);
			HashMap<String, Object> data = new HashMap<String, Object>();
			for (XPType type : XPType.values()) {
				data.put("xp" + type.getName().toLowerCase(), xpobj.getTotalXP(type));
			}
			PlayerDataController.setPlayerSettings(uuid, data);
		}
		return true;
	}

}
