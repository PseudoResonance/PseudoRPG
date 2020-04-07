package io.github.pseudoresonance.pseudorpg.xp;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.language.LanguageManager;
import io.github.pseudoresonance.pseudorpg.Config;

public class PlayerXP {

	private final String uuid;
	private HashMap<XPType, Integer> xp;
	private HashMap<XPType, BossBar> bb = new HashMap<XPType, BossBar>();

	public PlayerXP(Player p, HashMap<XPType, Integer> xp) {
		this.uuid = p.getUniqueId().toString();
		this.xp = xp;
		for (XPType type : XPType.values()) {
			BossBar bar = Bukkit.getServer().createBossBar(LanguageManager.getLanguage(p).getMessage("pseudorpg." + type.toString().toLowerCase() + "_name"), type.getBarColor(), Config.bossBarStyle);
			bar.setProgress(0.0);
			bb.put(type, bar);
		}
	}
	
	public PlayerXP(Player p) {
		this.uuid = p.getUniqueId().toString();
		this.xp = new HashMap<XPType, Integer>();
		for (XPType type : XPType.values()) {
			xp.put(type, 0);
			BossBar bar = Bukkit.getServer().createBossBar(LanguageManager.getLanguage(p).getMessage("pseudorpg." + type.toString().toLowerCase() + "_name"), type.getBarColor(), Config.bossBarStyle);
			bar.setProgress(0.0);
			bb.put(type, bar);
		}
	}
	
	public String getUUID() {
		return this.uuid;
	}
	
}
