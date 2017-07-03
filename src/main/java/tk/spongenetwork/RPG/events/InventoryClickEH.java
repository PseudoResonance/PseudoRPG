package tk.spongenetwork.RPG.events;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tk.spongenetwork.RPG.ConfigOptions;
import tk.spongenetwork.RPG.DataController;
import tk.spongenetwork.RPG.RPG;
import tk.spongenetwork.RPG.XP.XP;
import tk.spongenetwork.RPG.XP.XPManager;
import tk.spongenetwork.RPG.XP.XPType;
import tk.spongenetwork.RPG.XP.XPTypeYield;
import tk.spongenetwork.RPG.XP.XPYield;
import tk.spongenetwork.RPG.inventories.BlacksmithInv;
import tk.spongenetwork.RPG.inventories.SmelterInv;

public class InventoryClickEH implements Listener {

	private static Random random = new Random();

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Inventory inv = e.getClickedInventory();
		if (e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();
			if (inv != null) {
				InventoryType it = inv.getType();
				if (it != null) {
					if (it == InventoryType.CHEST) {
						String title = inv.getTitle();
						if (title.equals(BlacksmithInv.getInventoryName())) {
							e.setCancelled(true);
							// TODO Future Code Stuff: Idk what this is supposed
							// to do
							e.getWhoClicked().sendMessage("Clicked in Blacksmith Inventory");
						} else if (title.equals(SmelterInv.getInventoryName())) {
							e.setCancelled(true);
							XP xp = XPManager.getPlayerXP(p.getName());
							Inventory pinv = p.getInventory();
							if (e.getCurrentItem().getType() == Material.GOLD_INGOT) {
								if (removeItem(p, Material.GOLD_ORE)) {
									HashMap<Integer, ItemStack> drop = pinv.addItem(new ItemStack(Material.GOLD_INGOT, extraOreCalculator(xp.getLevel(XPType.SMELTING))));
									for (ItemStack i : drop.values()) {
										p.getWorld().dropItem(p.getLocation(), i);
									}
									XPYield xpy = null;
									if (DataController.smeltYield.containsKey("gold_ore,0")) {
										xpy = DataController.smeltYield.get("gold_ore,0");
									} else if (DataController.smeltYield.containsKey("gold_ore")) {
										xpy = DataController.smeltYield.get("gold_ore");
									}
									if (xpy != null) {
										for (XPTypeYield xpty : xpy.getYield()) {
											if (p.hasPermission("rpg.xp." + xpty.getType().getID())) {
												int i = xpty.getAmount();
												if (i > 0) {
													xp.addXP(xpty.getType(), i);
												} else if (i < 0) {
													xp.removeXP(xpty.getType(), i);
												}
											}
										}
									}
								} else {
									RPG.message.sendPluginMessage(p, "You do not have any gold ore!");
								}
							} else if (e.getCurrentItem().getType() == Material.DIAMOND) {
								if (removeItem(p, Material.DIAMOND_ORE)) {
									HashMap<Integer, ItemStack> drop = pinv.addItem(new ItemStack(Material.DIAMOND, extraOreCalculator(xp.getLevel(XPType.SMELTING))));
									for (ItemStack i : drop.values()) {
										p.getWorld().dropItem(p.getLocation(), i);
									}
									XPYield xpy = null;
									if (DataController.smeltYield.containsKey("diamond_ore,0")) {
										xpy = DataController.smeltYield.get("diamond_ore,0");
									} else if (DataController.smeltYield.containsKey("diamond_ore")) {
										xpy = DataController.smeltYield.get("diamond_ore");
									}
									if (xpy != null) {
										for (XPTypeYield xpty : xpy.getYield()) {
											if (p.hasPermission("rpg.xp." + xpty.getType().getID())) {
												int i = xpty.getAmount();
												if (i > 0) {
													xp.addXP(xpty.getType(), i);
												} else if (i < 0) {
													xp.removeXP(xpty.getType(), i);
												}
											}
										}
									}
								} else {
									RPG.message.sendPluginMessage(p, "You do not have any diamond ore!");
								}
							} else if (e.getCurrentItem().getType() == Material.IRON_INGOT) {
								if (removeItem(p, Material.IRON_ORE)) {
									HashMap<Integer, ItemStack> drop = pinv.addItem(new ItemStack(Material.IRON_INGOT, extraOreCalculator(xp.getLevel(XPType.SMELTING))));
									for (ItemStack i : drop.values()) {
										p.getWorld().dropItem(p.getLocation(), i);
									}
									XPYield xpy = null;
									if (DataController.smeltYield.containsKey("iron_ore,0")) {
										xpy = DataController.smeltYield.get("iron_ore,0");
									} else if (DataController.smeltYield.containsKey("iron_ore")) {
										xpy = DataController.smeltYield.get("iron_ore");
									}
									if (xpy != null) {
										for (XPTypeYield xpty : xpy.getYield()) {
											if (p.hasPermission("rpg.xp." + xpty.getType().getID())) {
												int i = xpty.getAmount();
												if (i > 0) {
													xp.addXP(xpty.getType(), i);
												} else if (i < 0) {
													xp.removeXP(xpty.getType(), i);
												}
											}
										}
									}
								} else {
									RPG.message.sendPluginMessage(p, "You do not have any iron ore!");
								}
							}
						}
					}
				}
			}
		}
	}

	private boolean removeItem(Player p, Material mat) {
		Inventory pinv = p.getInventory();
		int index = pinv.first(mat);
		if (index >= 0) {
			ItemStack item = pinv.getItem(index);
			item.setAmount(item.getAmount() - 1);
			pinv.setItem(index, item);
			return true;
		} else {
			return false;
		}
	}

	private int extraOreCalculator(int level) {
		double chance = ConfigOptions.smeltingExtraOreChance * (double) level;
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
