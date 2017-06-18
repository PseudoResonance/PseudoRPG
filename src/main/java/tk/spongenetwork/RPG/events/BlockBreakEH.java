package tk.spongenetwork.RPG.events;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import tk.spongenetwork.RPG.DataController;
import tk.spongenetwork.RPG.XP.XP;
import tk.spongenetwork.RPG.XP.XPManager;
import tk.spongenetwork.RPG.XP.XPTypeYield;
import tk.spongenetwork.RPG.XP.XPYield;

public class BlockBreakEH implements Listener {

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		GameMode gm = e.getPlayer().getGameMode();
		if (gm == GameMode.SURVIVAL || gm == GameMode.ADVENTURE) {
			Block b = e.getBlock();
			Material m = b.getType();
			@SuppressWarnings("deprecation")
			byte id = b.getData();
			XPYield xpy = null;
			String primary = m.toString().toLowerCase();
			String string = m.toString().toLowerCase() + "," + String.valueOf(id);
			if (DataController.yield.containsKey(string)) {
				xpy = DataController.yield.get(string);
			} else if (DataController.yield.containsKey(primary)) {
				xpy = DataController.yield.get(primary);
			}
			if (xpy != null) {
				XP xp = XPManager.getPlayerXP(e.getPlayer().getName());
				for (XPTypeYield xpty : xpy.getYield()) {
					xp.addXP(xpty.getType(), xpty.getAmount());
				}
			}
		}
	}
}
