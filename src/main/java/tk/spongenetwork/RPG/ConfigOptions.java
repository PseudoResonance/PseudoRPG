package tk.spongenetwork.RPG;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.boss.BarStyle;

import io.github.wolfleader116.wolfapi.bukkit.Config;
import io.github.wolfleader116.wolfapi.bukkit.ConfigOption;
import io.github.wolfleader116.wolfapi.bukkit.Message;
import net.md_5.bungee.api.ChatColor;

public class ConfigOptions implements ConfigOption {

	public static int fireworkInterval = 10;
	public static int fireworkCount = 5;
	public static boolean bossbar = true;
	public static String inventoryPrefix = "§c§l";
	public static BarStyle bossBarStyle = BarStyle.SEGMENTED_20;
	public static int bossBarDelay = 200;
	public static String bossBarMessage = "§c§l%SKILL% - %LEVEL% (%XP% / %LEVELXP%)";
	public static double miningExtraOreChance = 2.5;
	public static ArrayList<Material> extraOre = new ArrayList<Material>();
	public static double smeltingExtraOreChance = 2.5;
	public static double damageModifier = 0.65;
	public static double criticalChance = 1.5;
	public static double criticalMultiplier = 1.5;
	public static double sweepingCriticalChance = 1.5;
	public static double sweepingCriticalMultiplier = 1.3;

	public static boolean updateConfig() {
		boolean error = false;
		Config xp = new Config("xp.yml", RPG.plugin);
		if (xp.getConfig().getInt("version") != 2) {
			try {
				String oldFile = "";
				File conf = new File(RPG.plugin.getDataFolder(), "xp.yml");
				if (new File(RPG.plugin.getDataFolder(), "xp.yml.old").exists()) {
					for (int i = 1; i > 0; i++) {
						if (!(new File(RPG.plugin.getDataFolder(), "xp.yml.old" + i).exists())) {
							conf.renameTo(new File(RPG.plugin.getDataFolder(), "xp.yml.old" + i));
							oldFile = "xp.yml.old" + i;
							break;
						}
					}
				} else {
					conf.renameTo(new File(RPG.plugin.getDataFolder(), "xp.yml.old"));
					oldFile = "xp.yml.old";
				}
				xp.saveDefaultConfig();
				Message.sendConsoleMessage(ChatColor.GREEN + "XP config is up to date! Old xp config file renamed to " + oldFile + ".");
			} catch (Exception e) {
				Message.sendConsoleMessage(ChatColor.RED + "Error while updating config!");
				error = true;
			}
		}
		if (RPG.plugin.getConfig().getInt("Version") != 5) {
			try {
				String oldFile = "";
				File conf = new File(RPG.plugin.getDataFolder(), "config.yml");
				if (new File(RPG.plugin.getDataFolder(), "config.yml.old").exists()) {
					for (int i = 1; i > 0; i++) {
						if (!(new File(RPG.plugin.getDataFolder(), "config.yml.old" + i).exists())) {
							conf.renameTo(new File(RPG.plugin.getDataFolder(), "config.yml.old" + i));
							oldFile = "config.yml.old" + i;
							break;
						}
					}
				} else {
					conf.renameTo(new File(RPG.plugin.getDataFolder(), "config.yml.old"));
					oldFile = "config.yml.old";
				}
				RPG.plugin.saveDefaultConfig();
				RPG.plugin.reloadConfig();
				Message.sendConsoleMessage(ChatColor.GREEN + "Config is up to date! Old config file renamed to " + oldFile + ".");
			} catch (Exception e) {
				Message.sendConsoleMessage(ChatColor.RED + "Error while updating config!");
				error = true;
			}
		}
		if (!error) {
			Message.sendConsoleMessage(ChatColor.GREEN + "Config is up to date!");
		} else {
			return false;
		}
		return true;
	}

	public void reloadConfig() {
		try {
			fireworkInterval = RPG.plugin.getConfig().getInt("Experience.FireworkInterval");
		} catch (Exception e) {
			Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for Experience FireworkInterval!");
		}
		try {
			fireworkCount = RPG.plugin.getConfig().getInt("Experience.FireworkCount");
		} catch (Exception e) {
			Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for Experience FireworkCount!");
		}
		try {
			String s = RPG.plugin.getConfig().getString("Experience.BossBar");
			if (s.equalsIgnoreCase("true")) {
				bossbar = true;
			} else if (s.equalsIgnoreCase("false")) {
				bossbar = false;
			} else {
				bossbar = false;
				Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for Experience BossBar!");
			}
		} catch (Exception e) {
			Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for Experience BossBar!");
		}
		String[] inventoryPrefixs = RPG.plugin.getConfig().getString("Inventory.InventoryPrefix").split(",");
		List<ChatColor> inventoryPrefixl = new ArrayList<ChatColor>();
		for (String s : inventoryPrefixs) {
			try {
				if (s.length() == 1) {
					if (ChatColor.getByChar(s.charAt(0)) == null) {
						Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for Inventory InventoryPrefix!");
					} else {
						inventoryPrefixl.add(ChatColor.getByChar(s.charAt(0)));
					}
				} else {
					inventoryPrefixl.add(Enum.valueOf(ChatColor.class, s.toUpperCase()));
				}
			} catch (Exception e) {
				Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for Inventory InventoryPrefix!");
			}
		}
		inventoryPrefix = arrayToString(inventoryPrefixl.toArray(new ChatColor[inventoryPrefixl.size()]));
		try {
			String s = RPG.plugin.getConfig().getString("Experience.BossBarStyle");
			bossBarStyle = BarStyle.valueOf(s.toUpperCase());
		} catch (Exception e) {
			Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for Experience BossBarStyle!");
		}
		try {
			bossBarDelay = RPG.plugin.getConfig().getInt("Experience.BossBarDelay");
		} catch (Exception e) {
			Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for Experience BossBarDelay!");
		}
		try {
			bossBarMessage = RPG.plugin.getConfig().getString("Experience.BossBarMessage");
		} catch (Exception e) {
			Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for Experience BossBarMessage!");
		}
		try {
			miningExtraOreChance = RPG.plugin.getConfig().getDouble("Mining.ExtraOreChance");
		} catch (Exception e) {
			Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for Mining ExtraOreChances!");
		}
		List<String> extraOreList = RPG.plugin.getConfig().getStringList("Mining.Ore");
		for (String s : extraOreList) {
			try {
				Material m = Material.valueOf(s.toUpperCase());
				extraOre.add(m);
			} catch (Exception e) {
				Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for Mining Ore value: " + s + "!");
			}
		}
		try {
			smeltingExtraOreChance = RPG.plugin.getConfig().getDouble("Smelting.ExtraOreChance");
		} catch (Exception e) {
			Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for Smelting ExtraOreChances!");
		}
		try {
			damageModifier = RPG.plugin.getConfig().getDouble("Hunting.DamageModifier");
		} catch (Exception e) {
			Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for Hunting DamageModifier!");
		}
		try {
			criticalChance = RPG.plugin.getConfig().getDouble("Hunting.CriticalChance");
		} catch (Exception e) {
			Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for Hunting CriticalChance!");
		}
		try {
			criticalMultiplier = RPG.plugin.getConfig().getDouble("Hunting.CriticalMultiplier");
		} catch (Exception e) {
			Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for Hunting CriticalMultiplier!");
		}
		try {
			sweepingCriticalChance = RPG.plugin.getConfig().getDouble("Hunting.SweepingCriticalChance");
		} catch (Exception e) {
			Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for Hunting SweepingCriticalChance!");
		}
		try {
			sweepingCriticalMultiplier = RPG.plugin.getConfig().getDouble("Hunting.SweepingCriticalMultiplier");
		} catch (Exception e) {
			Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for Hunting SweepingCriticalMultiplier!");
		}
		DataController.updateBackend();
	}

	public static String arrayToString(ChatColor[] cc) {
		StringBuilder s = new StringBuilder();
		for (ChatColor c : cc) {
			s.append(c);
		}
		return s.toString();
	}
}
