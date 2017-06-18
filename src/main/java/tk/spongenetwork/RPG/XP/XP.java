package tk.spongenetwork.RPG.XP;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

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
	
	private Random rand = new Random();

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
		int i = xpl.getXP();
		int l = xpl.getLevel();
		xpLevel.put(type, l);
		xp.put(type, i);
		int total = getXPDifference(l + 1, l);
		double percent = (double) i / (double) total;
		if (percent < 0.0) {
			percent = 0.0;
		} else if (percent > 1.0) {
			percent = 1.0;
		}
		bb.get(type).setProgress(percent);
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
		if (x >= diff) {
			while (x >= diff) {
				int add = x - diff;
				xpLevel.put(type, l + 1);
				xp.put(type, add);
				x = add;
				l++;
				diff = getXPDifference(l + 1, l);
				if (l % ConfigOptions.fireworkInterval == 0) {
					fireworks(type);
				}
			}
		} else {
			xp.put(type, x);
		}
		displayBossBar(type);
	}

	public void setLevel(XPType type, int x) {
		xpLevel.put(type, x);
	}

	public void addXP(XPType type, int x) {
		int i = xp.get(type);
		i += x;
		int l = xpLevel.get(type);
		int diff = getXPDifference(l + 1, l);
		if (i >= diff) {
			while (i >= diff) {
				int add = i - diff;
				xpLevel.put(type, l + 1);
				xp.put(type, add);
				i = add;
				l++;
				diff = getXPDifference(l + 1, l);
				if (l % ConfigOptions.fireworkInterval == 0) {
					fireworks(type);
				}
			}
		} else {
			xp.put(type, i);
		}
		displayBossBar(type);
	}

	public void addLevel(XPType type, int x) {
		int i = xpLevel.get(type);
		i += x;
		xpLevel.put(type, i);
		displayBossBar(type);
		fireworks(type);
	}

	public void removeXP(XPType type, int x) {
		int i = xp.get(type);
		i -= x;
		int l = xpLevel.get(type);
		if (i < 0) {
			while (i < 0) {
				int diff = getXPDifference(l, l - 1);
				int remove = diff + i;
				xpLevel.put(type, l - 1);
				xp.put(type, remove);
			}
		} else {
			xp.put(type, i);
		}
		displayBossBar(type);
	}

	public void displayBossBar(XPType type) {
		int i = xp.get(type);
		int l = xpLevel.get(type);
		int total = getXPDifference(l + 1, l);
		double percent = (double) i / (double) total;
		if (percent < 0.0) {
			percent = 0.0;
		} else if (percent > 1.0) {
			percent = 1.0;
		}
		if (ConfigOptions.bossbar) {
			Player p = Bukkit.getPlayer(WolfAPI.getPlayerName(uuid));
			if (p != null) {
				BossBar boss = bb.get(type);
				boss.addPlayer(p);
				boss.setProgress(percent);
				boss.setTitle(ConfigOptions.bossBarPrefix + type.getName() + ": " + l);
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

	public void fireworks(XPType type) {
		Player p = Bukkit.getServer().getPlayer(WolfAPI.getPlayerName(uuid));
		if (p != null) {
			for (int x = 0; x < ConfigOptions.fireworkCount; x++) {
				Firework fw = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
				FireworkMeta fwm = fw.getFireworkMeta();
				Color c1 = Color.fromRGB(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
				Color c2 = Color.fromRGB(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
				FireworkEffect effect = FireworkEffect.builder().flicker(rand.nextBoolean()).withColor(c1).withFade(c2).with(Type.BALL).trail(rand.nextBoolean()).build();
				fwm.addEffect(effect);
				fwm.setPower(1);
				fw.setFireworkMeta(fwm);
			}
		}

	}

	public void removeLevel(XPType type, int x) {
		int i = xpLevel.get(type);
		i -= x;
		xpLevel.put(type, i);
		displayBossBar(type);
	}

	public static int getXPDifference(int target, int current) {
		int a = 0;
		for (int x = current; x < target; x++) {
			a += Math.floor(x + (300 * Math.pow(2, (x / 7.0))));
		}
		int output = (int) Math.floor(a / 4.0);
		return output;
	}

	public static XPLevel getXPLevel(int xp) {
		int out = 0;
		int outx = 0;
		int a = 0;
		for (int x = 1; out < xp; x++) {
			a += Math.floor(x + (300 * Math.pow(2, (x / 7.0))));
			int test = (int) Math.floor(a / 4.0);
			if (test > xp) {
				return new XPLevel(x, xp - out);
			} else if (test == xp) {
				return new XPLevel(x, 0);
			} else {
				out = test;
				outx = x;
			}
		}
		return new XPLevel(outx + 1, xp - out);
	}

	public static int getXPTotal(int level) {
		int a = 0;
		for (int x = 1; x < level; x++) {
			a += Math.floor(x + (300 * Math.pow(2, (x / 7.0))));
		}
		int output = (int) Math.floor(a / 4.0);
		return output;
	}

}