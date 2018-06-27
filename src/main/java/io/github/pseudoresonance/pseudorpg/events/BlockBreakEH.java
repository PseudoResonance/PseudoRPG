package io.github.pseudoresonance.pseudorpg.events;

import java.util.Collection;
import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import io.github.pseudoresonance.pseudorpg.ConfigOptions;
import io.github.pseudoresonance.pseudorpg.DataController;
import io.github.pseudoresonance.pseudorpg.xp.XP;
import io.github.pseudoresonance.pseudorpg.xp.XPManager;
import io.github.pseudoresonance.pseudorpg.xp.XPType;
import io.github.pseudoresonance.pseudorpg.xp.XPTypeYield;
import io.github.pseudoresonance.pseudorpg.xp.XPYield;

public class BlockBreakEH implements Listener {

	private static Random random = new Random();

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		GameMode gm = e.getPlayer().getGameMode();
		if (gm == GameMode.SURVIVAL || gm == GameMode.ADVENTURE) {
			if (e.isDropItems()) {
				Block b = e.getBlock();
				Material m = b.getType();
				@SuppressWarnings("deprecation")
				byte id = b.getData();
				XPYield xpy = null;
				String primary = m.toString().toLowerCase();
				String string = m.toString().toLowerCase() + "," + String.valueOf(id);
				if (DataController.breakYield.containsKey(string)) {
					xpy = DataController.breakYield.get(string);
				} else if (DataController.breakYield.containsKey(primary)) {
					xpy = DataController.breakYield.get(primary);
				}
				XP xp = XPManager.getPlayerXP(e.getPlayer().getName());
				if (xpy != null) {
					for (XPTypeYield xpty : xpy.getYield()) {
						if (e.getPlayer().hasPermission("rpg.xp." + xpty.getType().getID())) {
							int i = xpty.getAmount();
							if (i > 0) {
								xp.addXP(xpty.getType(), i);
							} else if (i < 0) {
								xp.removeXP(xpty.getType(), i);
							}
						}
					}
				}
				if (ConfigOptions.extraOre.contains(m)) {
					Collection<ItemStack> drops = b.getDrops();
					for (ItemStack item : drops) {
						int drop = extraOreCalculator(xp.getLevel(XPType.MINING));
						item.setAmount(drop);
						b.getWorld().dropItem(b.getLocation(), item);
					}
				}
			}
		}
	}

	private int extraOreCalculator(int level) {
		double chance = ConfigOptions.miningExtraOreChance * (double) level;
		int extra = (int) Math.floor(chance / 100);
		double difference = chance - extra;
		int rand = random.nextInt(100);
		if (rand < difference) {
			extra++;
		}
		return extra;
	}
}
