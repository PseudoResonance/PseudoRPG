package io.github.pseudoresonance.pseudorpg;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import io.github.pseudoresonance.pseudoapi.bukkit.Config;
import io.github.pseudoresonance.pseudoapi.bukkit.data.Backend;
import io.github.pseudoresonance.pseudoapi.bukkit.data.Data;
import io.github.pseudoresonance.pseudoapi.bukkit.data.FileBackend;
import io.github.pseudoresonance.pseudorpg.xp.XP;
import io.github.pseudoresonance.pseudorpg.xp.XPManager;
import io.github.pseudoresonance.pseudorpg.xp.XPType;
import io.github.pseudoresonance.pseudorpg.xp.XPTypeYield;
import io.github.pseudoresonance.pseudorpg.xp.XPYield;

public class DataController {

	public static HashMap<String, XPYield> breakYield = new HashMap<String, XPYield>();
	public static HashMap<String, XPYield> smeltYield = new HashMap<String, XPYield>();
	public static HashMap<EntityType, XPYield> huntYield = new HashMap<EntityType, XPYield>();
	private static Config xp = new Config("xp.yml", PseudoRPG.plugin);;
	private static Backend backend;
	
	private static Config c;
	
	public static void updateBackend() {
		backend = Data.getBackend();
		if (backend instanceof FileBackend) {
			FileBackend fb = (FileBackend) backend;
			c = new Config(fb.getFolder(), "rpg", PseudoRPG.plugin);
		}
		loadXP();
	}
	
	public static void loadXP() {
		xp.reload();
		HashMap<String, XP> xp = new HashMap<String, XP>();
		if (backend instanceof FileBackend) {
			ConfigurationSection cs = c.getConfig().getConfigurationSection("RPG.XP");
			if (cs != null) {
				Set<String> list = cs.getKeys(false);
				for (String s : list) {
					XP ret = new XP(s);
					for (XPType type : XPType.values()) {
						ret.setInitialXP(type, c.getConfig().getInt("RPG.XP." + s + "." + type.getID()));
					}
					xp.put(s, ret);
				}
			}
		}
		XPManager.setXP(xp);
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
	
	public static boolean updateXP() {
		HashMap<String, XP> xp = XPManager.getXP();
		for (String s : xp.keySet()) {
			XP xpobj = xp.get(s);
			for (XPType type : XPType.values()) {
				c.getConfig().set("RPG.XP." + s + "." + type.getID(), xpobj.getTotalXP(type));
			}
		}
		c.save();
		return true;
	}

}
