package io.github.pseudoresonance.pseudorpg.events;

import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.projectiles.ProjectileSource;

import io.github.pseudoresonance.pseudorpg.Config;
import io.github.pseudoresonance.pseudorpg.DataController;
import io.github.pseudoresonance.pseudorpg.xp.XP;
import io.github.pseudoresonance.pseudorpg.xp.XPManager;
import io.github.pseudoresonance.pseudorpg.xp.XPType;
import io.github.pseudoresonance.pseudorpg.xp.XPTypeYield;
import io.github.pseudoresonance.pseudorpg.xp.XPYield;

public class EntityDamageEH implements Listener {

	private static Random random = new Random();

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		Entity entity = e.getEntity();
		Entity damager = e.getDamager();
		DamageCause dc = e.getCause();
		if (entity instanceof LivingEntity) {
			LivingEntity le = (LivingEntity) entity;
			if (damager instanceof Player) {
				Player player = (Player) damager;
				XP xp = XPManager.getPlayerXP(player);
				GameMode gm = player.getGameMode();
				if (gm == GameMode.SURVIVAL || gm == GameMode.ADVENTURE) {
					if (dc == DamageCause.ENTITY_ATTACK) {
						double modifier = Config.criticalMultiplier * criticalCalculator(xp.getLevel(XPType.HUNTING));
						e.setDamage(e.getDamage() * modifier);
						calculateXP(entity.getType(), player, e.getDamage(), le.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
					} else if (dc == DamageCause.ENTITY_SWEEP_ATTACK) {
						double modifier = Config.sweepingCriticalMultiplier * sweepingCriticalCalculator(xp.getLevel(XPType.HUNTING));
						e.setDamage(e.getDamage() * modifier);
						calculateXP(entity.getType(), player, e.getDamage(), le.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
					}
				}
			} else if (damager instanceof Arrow) {
				Arrow arrow = (Arrow) damager;
				ProjectileSource shooter = arrow.getShooter();
				if (shooter instanceof Player) {
					Player player = (Player) shooter;
					XP xp = XPManager.getPlayerXP(player);
					GameMode gm = player.getGameMode();
					if (gm == GameMode.SURVIVAL || gm == GameMode.ADVENTURE) {
						double modifier = Config.criticalMultiplier * criticalCalculator(xp.getLevel(XPType.HUNTING));
						e.setDamage(e.getDamage() * modifier);
						calculateXP(entity.getType(), player, e.getDamage(), le.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
					}
				}
			}
		}
	}
	
	private void calculateXP(EntityType et, Player p, double damage, double mobHealth) {
		if (DataController.huntYield.containsKey(et)) {
			XPYield xpy = DataController.huntYield.get(et);
			XP xp = XPManager.getPlayerXP(p);
			for (XPTypeYield xpty : xpy.getYield()) {
				int i = xpty.getAmount();
				i *= Config.damageModifier;
				double percent = damage / mobHealth;
				if (percent > 1.0) {
					percent = 1.0;
				}
				int xpFinal = (int) Math.floor(i * percent);
				if (i > 0) {
					xp.addXP(xpty.getType(), xpFinal);
				} else if (i < 0) {
					xp.removeXP(xpty.getType(), xpFinal);
				}
			}
		}
	}

	private int criticalCalculator(int level) {
		double chance = Config.criticalChance * (double) level;
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
		double chance = Config.sweepingCriticalChance * (double) level;
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
