package io.github.pseudoresonance.pseudorpg.xp;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.PlayerDataController;
import io.github.pseudoresonance.pseudorpg.ConfigOptions;
import io.github.pseudoresonance.pseudorpg.PseudoRPG;
import io.github.pseudoresonance.pseudorpg.schedulers.BossBarDelay;

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
			BossBar bar = Bukkit.getServer().createBossBar(type.getName(), type.getBarColor(), ConfigOptions.bossBarStyle);
			bar.setProgress(0.0);
			bb.put(type, bar);
		}
	}

	public XP(String uuid) {
		this.uuid = uuid;
		for (XPType type : XPType.values()) {
			xp.put(type, 0);
			xpLevel.put(type, 0);
			BossBar bar = Bukkit.getServer().createBossBar(type.getName(), type.getBarColor(), ConfigOptions.bossBarStyle);
			bar.setProgress(0.0);
			bb.put(type, bar);
		}
	}
	
	public String getUUID() {
		return this.uuid;
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
		if (x >= 0) {
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
		} else {
			int i = xp.get(type);
			i -= x;
			int l = xpLevel.get(type);
			if (i < 0) {
				while (i < 0) {
					if ((l - 1) <= 0) {
						xpLevel.put(type, l);
						xp.put(type, 0);
						break;
					}
					int diff = getXPDifference(l, l - 1);
					int remove = diff + i;
					xpLevel.put(type, l - 1);
					xp.put(type, remove);
					i = remove;
					l--;
				}
			} else {
				xp.put(type, i);
			}
		}
		displayBossBar(type);
	}

	public void setLevel(XPType type, int x) {
		if (x <= 0) {
			x = 1;
		}
		xpLevel.put(type, x);
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
			}
		}
		displayBossBar(type);
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
		if (i % ConfigOptions.fireworkInterval == 0) {
			fireworks(type);
		}
	}

	public void removeXP(XPType type, int x) {
		int i = xp.get(type);
		i -= x;
		int l = xpLevel.get(type);
		if (i < 0) {
			while (i < 0) {
				if ((l - 1) <= 0) {
					xpLevel.put(type, l);
					xp.put(type, 0);
					break;
				}
				int diff = getXPDifference(l, l - 1);
				int remove = diff + i;
				xpLevel.put(type, l - 1);
				xp.put(type, remove);
				i = remove;
				l--;
			}
		} else {
			xp.put(type, i);
		}
		displayBossBar(type);
	}

	public void removeLevel(XPType type, int x) {
		int l = xpLevel.get(type);
		l -= x;
		if (l <= 0) {
			l = 1;
			xp.put(type, 0);
		}
		xpLevel.put(type, l);
		int i = xp.get(type);
		i -= x;
		if (i < 0) {
			while (i < 0) {
				if ((l - 1) <= 0) {
					xpLevel.put(type, l);
					xp.put(type, 0);
					break;
				}
				int diff = getXPDifference(l, l - 1);
				int remove = diff + i;
				xpLevel.put(type, l - 1);
				xp.put(type, remove);
				i = remove;
				l--;
			}
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
			Player p = Bukkit.getPlayer(PlayerDataController.getName(uuid));
			if (p != null) {
				BossBar boss = bb.get(type);
				boss.addPlayer(p);
				boss.setProgress(percent);
				String format = ConfigOptions.bossBarMessage;
				format = format.replace("%SKILL%", type.getName());
				format = format.replace("%LEVEL%", String.valueOf(l));
				format = format.replace("%XP%", String.valueOf(i));
				format = format.replace("%LEVELXP%", String.valueOf(total));
				int xpObtained = getXPTotal(l) + i;
				format = format.replace("%TOTALXP%", String.valueOf(xpObtained));
				boss.setTitle(format);
				boss.setVisible(true);
				if (bbd.containsKey(type)) {
					bbd.get(type).cancel();
					bbd.remove(type);
				}
				BossBarDelay delay = new BossBarDelay(boss);
				bbd.put(type, delay);
				delay.runTaskLater(PseudoRPG.plugin, ConfigOptions.bossBarDelay);
			}
		}
	}

	public void fireworks(XPType type) {
		Player p = Bukkit.getServer().getPlayer(PlayerDataController.getName(uuid));
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
	
	public int getNextXP(XPType type) {
		int xpl = xpLevel.get(type);
		return getXPDifference(xpl + 1, xpl);
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