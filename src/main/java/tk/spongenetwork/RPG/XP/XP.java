package tk.spongenetwork.RPG.XP;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import io.github.wolfleader116.wolfapi.bukkit.WolfAPI;
import tk.spongenetwork.RPG.ConfigOptions;
import tk.spongenetwork.RPG.RPG;
import tk.spongenetwork.RPG.schedulers.BossBarDelay;

public class XP {

	private String uuid = "";
	private HashMap<XPType, Integer> xp = new HashMap<XPType, Integer>();
	private HashMap<XPType, Integer> xpLevel = new HashMap<XPType, Integer>();
	private HashMap<XPType, BossBar> bb = new HashMap<XPType, BossBar>();
	private HashMap<XPType, BossBarDelay> bbd = new HashMap<XPType, BossBarDelay>();

	public XP(String uuid, HashMap<XPType, Integer> xp, HashMap<XPType, Integer> xpLevel) {
		this.uuid = uuid;
		this.xp = xp;
		this.xpLevel = xpLevel;
		for (XPType type : XPType.values()) {
			BossBar bar = Bukkit.getServer().createBossBar(ConfigOptions.bossBarPrefix + type.getName(), type.getBarColor(), BarStyle.SEGMENTED_20);
			bar.setProgress(0.0);
			bb.put(type, bar);
		}
	}

	public XP(String uuid) {
		this.uuid = uuid;
		for (XPType type : XPType.values()) {
			xp.put(type, 0);
			xpLevel.put(type, 0);
			BossBar bar = Bukkit.getServer().createBossBar(ConfigOptions.bossBarPrefix + type.getName(), type.getBarColor(), BarStyle.SEGMENTED_10);
			bar.setProgress(0.0);
			bb.put(type, bar);
		}
	}
	
	public void setInitialXP(XPType type, int x) {
		XPLevel xpl = getXPLevel(x);
		xpLevel.put(type, xpl.getLevel());
		xp.put(type, xpl.getXP());
	}
	
	public int getTotalXP(XPType type) {
		int i = xp.get(type);
		int l = xpLevel.get(type);
		int tot = getXPTotal(l);
		int add = tot + i;
		return add;
	}
	
	public int getXP(XPType type) {
		int i = xp.get(type);
		return i;
	}
	
	public int getLevel(XPType type) {
		int i = xpLevel.get(type);
		return i;
	}
	
	public void setXP(XPType type, int x) {
		int l = xpLevel.get(type);
		int diff = getXPDifference(l + 1, l);
		int total = 0;
		int current = 0;
		int displayLevel = l;
		if (x >= diff) {
			int add = x - diff;
			xpLevel.put(type, l + 1);
			xp.put(type, add);
			total = getXPDifference(l + 2, l + 1);
			current = add;
			displayLevel++;
		} else {
			xp.put(type, x);
			total = diff;
			current = x;
		}
		double percent = (double) current / (double) total;
		if (percent < 0.0) {
			percent = 0.0;
		} else if (percent > 1.0) {
			percent = 1.0;
		}
		if (ConfigOptions.bossbar) {
			Player p = Bukkit.getPlayer(WolfAPI.getName(uuid));
			if (p.isOnline()) {
				BossBar boss = bb.get(type);
				boss.addPlayer(p);
				boss.setProgress(percent);
				boss.setTitle(ConfigOptions.bossBarPrefix + type.getName() + ": " + displayLevel);
				boss.setVisible(true);
				if (bbd.containsKey(type)) {
					bbd.get(type).cancel();
					bbd.remove(type);
				}
				BossBarDelay delay = new BossBarDelay(boss);
				bbd.put(type, delay);
				delay.runTaskLater(RPG.plugin, ConfigOptions.bossBarDelay);
			}
		}
	}
	
	public void setLevel(XPType type, int x) {
		xpLevel.put(type, x);
	}
	
	public void addXP(XPType type, int x) {
		int i = xp.get(type);
		i += x;
		int l = xpLevel.get(type);
		int diff = getXPDifference(l + 1, l);
		int total = 0;
		int current = 0;
		int displayLevel = l;
		if (i >= diff) {
			int add = i - diff;
			xpLevel.put(type, l + 1);
			xp.put(type, add);
			total = getXPDifference(l + 2, l + 1);
			current = add;
			displayLevel++;
		} else {
			xp.put(type, i);
			total = diff;
			current = i;
		}
		double percent = (double) current / (double) total;
		if (percent < 0.0) {
			percent = 0.0;
		} else if (percent > 1.0) {
			percent = 1.0;
		}
		if (ConfigOptions.bossbar) {
			Player p = Bukkit.getPlayer(WolfAPI.getName(uuid));
			if (p.isOnline()) {
				BossBar boss = bb.get(type);
				boss.addPlayer(p);
				boss.setProgress(percent);
				boss.setTitle(ConfigOptions.bossBarPrefix + type.getName() + ": " + displayLevel);
				boss.setVisible(true);
				if (bbd.containsKey(type)) {
					bbd.get(type).cancel();
					bbd.remove(type);
				}
				BossBarDelay delay = new BossBarDelay(boss);
				bbd.put(type, delay);
				delay.runTaskLater(RPG.plugin, ConfigOptions.bossBarDelay);
			}
		}
	}
	
	public void addLevel(XPType type, int x) {
		int i = xpLevel.get(type);
		i += x;
		xpLevel.put(type, i);
	}
	
	public void removeXP(XPType type, int x) {
		int i = xp.get(type);
		i -= x;
		int total = 0;
		int current = 0;
		int l = xpLevel.get(type);
		int displayLevel = l;
		if (i < 0) {
			int diff = getXPDifference(l, l - 1);
			int remove = diff + i;
			xpLevel.put(type, l - 1);
			xp.put(type, remove);
			total = diff;
			current = remove;
			displayLevel--;
		} else {
			xp.put(type, i);
			total = getXPDifference(l + 1, l);
			current = i;
		}
		double percent = (double) current / (double) total;
		if (percent < 0.0) {
			percent = 0.0;
		} else if (percent > 1.0) {
			percent = 1.0;
		}
		if (ConfigOptions.bossbar) {
			Player p = Bukkit.getPlayer(WolfAPI.getName(uuid));
			if (p.isOnline()) {
				BossBar boss = bb.get(type);
				boss.addPlayer(p);
				boss.setProgress(percent);
				boss.setTitle(ConfigOptions.bossBarPrefix + type.getName() + ": " + displayLevel);
				boss.setVisible(true);
				if (bbd.containsKey(type)) {
					bbd.get(type).cancel();
					bbd.remove(type);
				}
				BossBarDelay delay = new BossBarDelay(boss);
				bbd.put(type, delay);
				delay.runTaskLater(RPG.plugin, ConfigOptions.bossBarDelay);
			}
		}
	}
	
	public void removeLevel(XPType type, int x) {
		int i = xpLevel.get(type);
		i -= x;
		xpLevel.put(type, i);
	}
	
	public static int getXPDifference(int target, int current) {
		double p1 = (10 * Math.pow(target, 2)) + 1010 * target;
		double p2 = (10 * Math.pow(current, 2)) + 1010 * current;
		int x = (int) (p1 - p2);
		return x;
	}
	
	public static int getXPTotal(int level) {
		double p1 = 1000 * level;
		double p2 = Math.pow((level - 1), 2);
		double p3 = level - 1;
		double p4 = p2 + p3;
		double p5 = 10 * p4;
		int x = (int) (p1 + p5);
		return x;
	}
	
	public static XPLevel getXPLevel(int xp) {
		double p1 = 5 * (53045 + (2 * xp));
		double p2 = Math.sqrt(p1);
		double p3 = -515 + p2;
		double p4 = p3 / (double) 10;
		int level = (int) Math.floor(p4);
		int used = getXPTotal(level);
		int remainder = xp - used;
		XPLevel xpl = new XPLevel(level, remainder);
		return xpl;
	}

}