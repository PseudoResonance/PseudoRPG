package tk.spongenetwork.RPG;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;

import io.github.wolfleader116.wolfapi.bukkit.Config;
import io.github.wolfleader116.wolfapi.bukkit.data.Backend;
import io.github.wolfleader116.wolfapi.bukkit.data.Data;
import io.github.wolfleader116.wolfapi.bukkit.data.FileBackend;
import tk.spongenetwork.RPG.XP.XP;
import tk.spongenetwork.RPG.XP.XPManager;
import tk.spongenetwork.RPG.XP.XPType;
import tk.spongenetwork.RPG.XP.XPTypeYield;
import tk.spongenetwork.RPG.XP.XPYield;

public class DataController {

	public static HashMap<String, XPYield> yield = new HashMap<String, XPYield>();
	private static Config xp = new Config("xp.yml", RPG.plugin);;
	private static Backend backend;
	
	private static Config c;
	
	public static void updateBackend() {
		backend = Data.getBackend();
		if (backend instanceof FileBackend) {
			FileBackend fb = (FileBackend) backend;
			c = new Config(fb.getFolder(), fb.getFile(), RPG.plugin);
		}
		loadXP();
	}
	
	public static void loadXP() {
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
		ConfigurationSection se = DataController.xp.getConfig().getConfigurationSection("");
		if (se != null) {
			for (String block : se.getKeys(false)) {
				XPYield xpy = new XPYield();
				ConfigurationSection cs = DataController.xp.getConfig().getConfigurationSection(block);
				if (cs != null) {
					for (String category : cs.getKeys(false)) {
						int value = DataController.xp.getConfig().getInt(block + "." + category);
						XPTypeYield xpty = new XPTypeYield(XPType.valueOf(category.toUpperCase()), value);
						xpy.addYield(xpty);
					}
					yield.put(block, xpy);
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
