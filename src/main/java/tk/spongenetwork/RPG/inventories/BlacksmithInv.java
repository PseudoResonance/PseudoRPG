package tk.spongenetwork.RPG.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tk.spongenetwork.RPG.ConfigOptions;

public class BlacksmithInv {

	public static Inventory getDefaultInventory() {
		Inventory inv = Bukkit.getServer().createInventory(null, 54, getInventoryName());
        inv.setItem(9, new ItemStack(Material.IRON_CHESTPLATE));
        inv.setItem(11, new ItemStack(Material.GOLD_BLOCK));
        inv.setItem(13, new ItemStack(Material.COAL));
        inv.setItem(15, new ItemStack(Material.IRON_BLOCK));
        inv.setItem(17, new ItemStack(Material.IRON_INGOT));
		return inv;
	}

	public static String getInventoryName() {
		return ConfigOptions.inventoryPrefix + "Blacksmith";
	}

}
