package tk.spongenetwork.RPG.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tk.spongenetwork.RPG.RPG;
import tk.spongenetwork.RPG.inventories.BlacksmithInv;
import tk.spongenetwork.RPG.inventories.SmelterInv;

public class InventoryClickEH implements Listener {

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Inventory inv = e.getClickedInventory();
		if (e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();
			InventoryType it = inv.getType();
			if (it != null) {
				if (it == InventoryType.CHEST) {
					String title = inv.getTitle();
					if (title.equals(BlacksmithInv.getInventoryName())) {
						e.setCancelled(true);
						// TODO Future Code Stuff: Idk what this is supposed to do
						e.getWhoClicked().sendMessage("Clicked in Blacksmith Inventory");
					} else if (title.equals(SmelterInv.getInventoryName())) {
						e.setCancelled(true);
						Inventory pinv = p.getInventory();
						if (e.getCurrentItem().getType() == Material.GOLD_INGOT) {
							if (removeItem(p, Material.GOLD_ORE)) {
								pinv.addItem(new ItemStack(Material.GOLD_INGOT, 1));
							} else {
								RPG.message.sendPluginMessage(p, "You do not have any gold ore!");
							}
						} else if (e.getCurrentItem().getType() == Material.DIAMOND) {
							if (removeItem(p, Material.DIAMOND_ORE)) {
								pinv.addItem(new ItemStack(Material.DIAMOND, 1));
							} else {
								RPG.message.sendPluginMessage(p, "You do not have any diamond ore!");
							}
						} else if (e.getCurrentItem().getType() == Material.IRON_INGOT) {
							if (removeItem(p, Material.IRON_ORE)) {
								pinv.addItem(new ItemStack(Material.IRON_INGOT, 1));
							} else {
								RPG.message.sendPluginMessage(p, "You do not have any iron ore!");
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
}
