package tk.spongenetwork.RPG.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tk.spongenetwork.RPG.ConfigOptions;

public class SmelterInv {

	public static Inventory getDefaultInventory() {
		Inventory inv = Bukkit.getServer().createInventory(null, 27, getInventoryName());
        inv.setItem(9, new ItemStack(Material.GOLD_INGOT));
        inv.setItem(13, new ItemStack(Material.DIAMOND));
        inv.setItem(17, new ItemStack(Material.IRON_INGOT));
		return inv;
	}

	public static String getInventoryName() {
		return ConfigOptions.inventoryPrefix + "Smelter";
	}

}
