package io.github.pseudoresonance.pseudorpg;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import io.github.pseudoresonance.pseudoapi.bukkit.PseudoPlugin;
import io.github.pseudoresonance.pseudoapi.bukkit.Chat.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.data.PluginConfig;
import io.github.pseudoresonance.pseudoapi.bukkit.utils.ConfigFile;

public class Config extends PluginConfig {

	public static int maxXpLevel = 100;
	public static int fireworkInterval = 10;
	public static int fireworkCount = 5;
	public static boolean bossBar = true;
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

	public boolean updateConfig() {
		super.updateConfig();
		ConfigFile xp = new ConfigFile("xp.yml", PseudoRPG.plugin);
		InputStream configin = PseudoRPG.plugin.getClass().getResourceAsStream("/xp.yml");
		BufferedReader configreader = new BufferedReader(new InputStreamReader(configin));
		YamlConfiguration configc = YamlConfiguration.loadConfiguration(configreader);
		int configcj = configc.getInt("version");
		try {
			configreader.close();
			configin.close();
		} catch (IOException e1) {
			PseudoRPG.plugin.getChat().sendConsolePluginError(Errors.CUSTOM, "Error while updating XP config!");
			e1.printStackTrace();
			return false;
		}
		if (xp.getConfig().getInt("version") != configcj) {
			try {
				String oldFile = "";
				File conf = new File(PseudoRPG.plugin.getDataFolder(), "xp.yml");
				if (new File(PseudoRPG.plugin.getDataFolder(), "xp.yml.old").exists()) {
					for (int i = 1; i > 0; i++) {
						if (!(new File(PseudoRPG.plugin.getDataFolder(), "xp.yml.old" + i).exists())) {
							conf.renameTo(new File(PseudoRPG.plugin.getDataFolder(), "xp.yml.old" + i));
							oldFile = "xp.yml.old" + i;
							break;
						}
					}
				} else {
					conf.renameTo(new File(PseudoRPG.plugin.getDataFolder(), "xp.yml.old"));
					oldFile = "config.yml.old";
				}
				xp.saveDefaultConfig();
				xp.reload();
				PseudoRPG.plugin.getChat().sendConsolePluginMessage("Config is up to date! Old xp config file renamed to " + oldFile + ".");
				return true;
			} catch (Exception e) {
				PseudoRPG.plugin.getChat().sendConsolePluginError(Errors.CUSTOM, "Error while updating XP config!");
				return false;
			}
		}
		PseudoRPG.plugin.getChat().sendConsolePluginMessage("XP config is up to date!");
		return true;
	}

	public void reloadConfig() {
		FileConfiguration fc = PseudoRPG.plugin.getConfig();
		maxXpLevel = PluginConfig.getInt(fc, "Experience.MaxXPLevel", maxXpLevel);
		fireworkInterval = PluginConfig.getInt(fc, "Experience.FireworkInterval", fireworkInterval);
		fireworkCount = PluginConfig.getInt(fc, "Experience.FireworkCount", fireworkCount);
		bossBar = PluginConfig.getBoolean(fc, "Experience.BossBar", bossBar);
		bossBarStyle = PluginConfig.getEnum(fc, "Experience.BossBarStyle", bossBarStyle);
		bossBarDelay = PluginConfig.getInt(fc, "Experience.BossBarDelay", bossBarDelay);
		bossBarMessage = PluginConfig.getString(fc, "Experience.BossBarMessage", bossBarMessage);
		miningExtraOreChance = PluginConfig.getDouble(fc, "Mining.ExtraOreChance", miningExtraOreChance);
		List<String> extraOreList = PseudoRPG.plugin.getConfig().getStringList("Mining.Ore");
		for (String s : extraOreList) {
			try {
				Material m = Material.valueOf(s.toUpperCase());
				extraOre.add(m);
			} catch (Exception e) {
				PseudoRPG.plugin.getChat().sendConsolePluginError(Errors.CUSTOM, "Invalid config option for Mining.Ore value: " + s + "!");
			}
		}
		smeltingExtraOreChance = PluginConfig.getDouble(fc, "Smelting.ExtraOreChance", smeltingExtraOreChance);
		damageModifier = PluginConfig.getDouble(fc, "Hunting.DamageModifier", damageModifier);
		criticalChance = PluginConfig.getDouble(fc, "Hunting.CriticalChance", criticalChance);
		criticalMultiplier = PluginConfig.getDouble(fc, "Hunting.CriticalMultiplier", criticalMultiplier);
		sweepingCriticalChance = PluginConfig.getDouble(fc, "Hunting.SweepingCriticalChance", sweepingCriticalChance);
		sweepingCriticalMultiplier = PluginConfig.getDouble(fc, "Hunting.SweepingCriticalMultiplier", sweepingCriticalMultiplier);
		DataController.updateBackend();
	}

	public static String arrayToString(ChatColor[] cc) {
		StringBuilder s = new StringBuilder();
		for (ChatColor c : cc) {
			s.append(c);
		}
		return s.toString();
	}
	
	public Config(PseudoPlugin plugin) {
		super(plugin);
	}
}
