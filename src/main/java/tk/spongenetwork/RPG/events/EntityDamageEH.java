package tk.spongenetwork.RPG.events;

import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.projectiles.ProjectileSource;

import tk.spongenetwork.RPG.ConfigOptions;
import tk.spongenetwork.RPG.DataController;
import tk.spongenetwork.RPG.XP.XP;
import tk.spongenetwork.RPG.XP.XPManager;
import tk.spongenetwork.RPG.XP.XPType;
import tk.spongenetwork.RPG.XP.XPTypeYield;
import tk.spongenetwork.RPG.XP.XPYield;

public class EntityDamageEH implements Listener {

	private static Random random = new Random();

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		Entity entity = e.getEntity();
		Entity damager = e.getDamager();
		DamageCause dc = e.getCause();
		if (damager instanceof Player) {
			Player player = (Player) damager;
			XP xp = XPManager.getPlayerXP(player.getName());
			GameMode gm = player.getGameMode();
			if (gm == GameMode.SURVIVAL || gm == GameMode.ADVENTURE) {
				if (dc == DamageCause.ENTITY_ATTACK) {
					double modifier = ConfigOptions.criticalMultiplier * criticalCalculator(xp.getLevel(XPType.HUNTING));
					e.setDamage(e.getDamage() * modifier);
					calculateXP(entity.getType(), player);
				} else if (dc == DamageCause.ENTITY_SWEEP_ATTACK) {
					double modifier = ConfigOptions.sweepingCriticalMultiplier * sweepingCriticalCalculator(xp.getLevel(XPType.HUNTING));
					e.setDamage(e.getDamage() * modifier);
					calculateXP(entity.getType(), player);
				}
			}
		} else if (damager instanceof Arrow) {
			Arrow arrow = (Arrow) damager;
			ProjectileSource shooter = arrow.getShooter();
			if (shooter instanceof Player) {
				Player player = (Player) shooter;
				XP xp = XPManager.getPlayerXP(player.getName());
				GameMode gm = player.getGameMode();
				if (gm == GameMode.SURVIVAL || gm == GameMode.ADVENTURE) {
					double modifier = ConfigOptions.criticalMultiplier * criticalCalculator(xp.getLevel(XPType.HUNTING));
					e.setDamage(e.getDamage() * modifier);
					calculateXP(entity.getType(), player);
				}
			}
		}
	}
	
	private void calculateXP(EntityType et, Player p) {
		if (DataController.huntYield.containsKey(et)) {
			XPYield xpy = DataController.huntYield.get(et);
			XP xp = XPManager.getPlayerXP(p.getName());
			for (XPTypeYield xpty : xpy.getYield()) {
				int i = xpty.getAmount();
				i *= ConfigOptions.damageModifier;
				if (i > 0) {
					xp.addXP(xpty.getType(), i);
				} else if (i < 0) {
					xp.removeXP(xpty.getType(), i);
				}
			}
		}
	}

	private int criticalCalculator(int level) {
		double chance = ConfigOptions.criticalChance * (double) level;
		int extra = (int) Math.floor(chance / 100);
		double difference = chance - extra;
		int rand = random.nextInt(100);
		if (rand < difference) {
			extra++;
		}
		extra++;
		return extra;
	}

	private int sweepingCriticalCalculator(int level) {
		double chance = ConfigOptions.sweepingCriticalChance * (double) level;
		int extra = (int) Math.floor(chance / 100);
		double difference = chance - extra;
		int rand = random.nextInt(100);
		if (rand < difference) {
			extra++;
		}
		extra++;
		return extra;
	}
}
