package tk.spongenetwork.RPG.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import tk.spongenetwork.RPG.inventories.BlacksmithInv;
import tk.spongenetwork.RPG.inventories.SmelterInv;

public class PlayerInteractEH implements Listener {

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (e.getClickedBlock().getType() == Material.ANVIL) {
				e.setCancelled(true);
				e.getPlayer().openInventory(BlacksmithInv.getDefaultInventory());
			} else if (e.getClickedBlock().getType() == Material.FURNACE || e.getClickedBlock().getType() == Material.BURNING_FURNACE) {
				e.setCancelled(true);
				e.getPlayer().openInventory(SmelterInv.getDefaultInventory());
			}
		}
	}
}
