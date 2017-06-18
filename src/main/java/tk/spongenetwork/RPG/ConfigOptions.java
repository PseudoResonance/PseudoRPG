package tk.spongenetwork.RPG;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.boss.BarStyle;

import io.github.wolfleader116.wolfapi.bukkit.ConfigOption;
import io.github.wolfleader116.wolfapi.bukkit.Message;
import net.md_5.bungee.api.ChatColor;

public class ConfigOptions implements ConfigOption {

	public static boolean bossbar = true;
	public static String inventoryPrefix = "§c§l";
	public static String bossBarPrefix = "§e§l";
	public static BarStyle bossBarStyle = BarStyle.SEGMENTED_20;
	public static int bossBarDelay = 200;

	public static boolean updateConfig() {
		if (RPG.plugin.getConfig().getInt("Version") == 1) {
			Message.sendConsoleMessage(ChatColor.GREEN + "Config is up to date!");
		} else {
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
				return false;
			}
		}
		return true;
	}

	public void reloadConfig() {
		try {
			String s = RPG.plugin.getConfig().getString("BossBar");
			if (s.equalsIgnoreCase("true")) {
				bossbar = true;
			} else if (s.equalsIgnoreCase("false")) {
				bossbar = false;
			} else {
				bossbar = false;
				Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for BossBar!");
			}
		} catch (Exception e) {
			Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for BossBar!");
		}
		String[] inventoryPrefixs = RPG.plugin.getConfig().getString("InventoryPrefix").split(",");
		List<ChatColor> inventoryPrefixl = new ArrayList<ChatColor>();
		for (String s : inventoryPrefixs) {
			try {
				if (s.length() == 1) {
					if (ChatColor.getByChar(s.charAt(0)) == null) {
						Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for InventoryPrefix!");
					} else {
						inventoryPrefixl.add(ChatColor.getByChar(s.charAt(0)));
					}
				} else {
					inventoryPrefixl.add(Enum.valueOf(ChatColor.class, s.toUpperCase()));
				}
			} catch (Exception e) {
				Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for InventoryPrefix!");
			}
		}
		inventoryPrefix = arrayToString(inventoryPrefixl.toArray(new ChatColor[inventoryPrefixl.size()]));
		String[] bossBarPrefixs = RPG.plugin.getConfig().getString("BossBarPrefix").split(",");
		List<ChatColor> bossBarPrefixl = new ArrayList<ChatColor>();
		for (String s : bossBarPrefixs) {
			try {
				if (s.length() == 1) {
					if (ChatColor.getByChar(s.charAt(0)) == null) {
						Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for BossBarPrefix!");
					} else {
						bossBarPrefixl.add(ChatColor.getByChar(s.charAt(0)));
					}
				} else {
					bossBarPrefixl.add(Enum.valueOf(ChatColor.class, s.toUpperCase()));
				}
			} catch (Exception e) {
				Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for BossBarPrefix!");
			}
		}
		bossBarPrefix = arrayToString(bossBarPrefixl.toArray(new ChatColor[bossBarPrefixl.size()]));
		try {
			String s = RPG.plugin.getConfig().getString("BossBarStyle");
			bossBarStyle = BarStyle.valueOf(s.toUpperCase());
		} catch (Exception e) {
			Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for BossBarStyle!");
		}
		try {
			bossBarDelay = RPG.plugin.getConfig().getInt("BossBarDelay");
		} catch (Exception e) {
			Message.sendConsoleMessage(ChatColor.RED + "Invalid config option for BossBarDelay!");
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
