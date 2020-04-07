package io.github.pseudoresonance.pseudorpg;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import io.github.pseudoresonance.pseudoapi.bukkit.Chat.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.PlayerDataController;
import io.github.pseudoresonance.pseudoapi.bukkit.utils.ConfigFile;
import io.github.pseudoresonance.pseudorpg.xp.XP;
import io.github.pseudoresonance.pseudorpg.xp.XPManager;
import io.github.pseudoresonance.pseudorpg.xp.XPType;
import io.github.pseudoresonance.pseudorpg.xp.XPTypeYield;
import io.github.pseudoresonance.pseudorpg.xp.XPYield;

public class DataController {

	public static HashMap<Material, XPYield> breakYield = new HashMap<Material, XPYield>();
	public static HashMap<Material, XPYield> smeltYield = new HashMap<Material, XPYield>();
	public static HashMap<EntityType, XPYield> huntYield = new HashMap<EntityType, XPYield>();
	private static ConfigFile xp = new ConfigFile("xp.yml", PseudoRPG.plugin);

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
					if (key.startsWith("#")) {
						try {
							Field tagField = Tag.class.getDeclaredField(key.substring(1, key.length()).toUpperCase());
							@SuppressWarnings("unchecked")
							Tag<Material> tagList = (Tag<Material>) tagField.get(null);
							for (String category : cs.getKeys(false)) {
								int value = DataController.xp.getConfig().getInt("breaking." + key + "." + category);
								XPTypeYield xpty = new XPTypeYield(XPType.valueOf(category.toUpperCase()), value);
								xpy.addYield(xpty);
							}
							for (Material m : tagList.getValues())
								breakYield.put(m, xpy);
						} catch (Exception e) {
							PseudoRPG.plugin.getChat().sendConsolePluginError(Errors.CUSTOM, "Invalid material tag for xp breaking." + key + "!");
						}
					} else {
						try {
							Material m = Material.valueOf(key.toUpperCase());
							for (String category : cs.getKeys(false)) {
								int value = DataController.xp.getConfig().getInt("breaking." + key + "." + category);
								XPTypeYield xpty = new XPTypeYield(XPType.valueOf(category.toUpperCase()), value);
								xpy.addYield(xpty);
							}
							breakYield.put(m, xpy);
						} catch (Exception e) {
							PseudoRPG.plugin.getChat().sendConsolePluginError(Errors.CUSTOM, "Invalid material for xp breaking." + key + "!");
						}
					}
				}
			}
		}
		ConfigurationSection se2 = DataController.xp.getConfig().getConfigurationSection("smelting");
		if (se2 != null) {
			for (String key : se2.getKeys(false)) {
				XPYield xpy = new XPYield();
				ConfigurationSection cs = DataController.xp.getConfig().getConfigurationSection("smelting." + key);
				if (cs != null) {
					if (key.startsWith("#")) {
						try {
							Field tagField = Tag.class.getDeclaredField(key.substring(1, key.length()));
							@SuppressWarnings("unchecked")
							Tag<Material> tagList = (Tag<Material>) tagField.get(null);
							for (String category : cs.getKeys(false)) {
								int value = DataController.xp.getConfig().getInt("smelting." + key + "." + category);
								XPTypeYield xpty = new XPTypeYield(XPType.valueOf(category.toUpperCase()), value);
								xpy.addYield(xpty);
							}
							for (Material m : tagList.getValues())
								smeltYield.put(m, xpy);
						} catch (Exception e) {
							PseudoRPG.plugin.getChat().sendConsolePluginError(Errors.CUSTOM, "Invalid material tag for xp smelting." + key + "!");
						}
					} else {
						try {
							Material m = Material.valueOf(key.toUpperCase());
							for (String category : cs.getKeys(false)) {
								int value = DataController.xp.getConfig().getInt("smelting." + key + "." + category);
								XPTypeYield xpty = new XPTypeYield(XPType.valueOf(category.toUpperCase()), value);
								xpy.addYield(xpty);
							}
							smeltYield.put(m, xpy);
						} catch (Exception e) {
							PseudoRPG.plugin.getChat().sendConsolePluginError(Errors.CUSTOM, "Invalid material for xp smelting." + key + "!");
						}
					}
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
				data.put("xp" + type.toString().toLowerCase(), xpobj.getTotalXP(type));
			}
			PlayerDataController.setPlayerSettings(uuid, data);
		}
		return true;
	}

}
