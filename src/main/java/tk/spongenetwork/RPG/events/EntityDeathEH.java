package tk.spongenetwork.RPG.events;

import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import tk.spongenetwork.RPG.DataController;
import tk.spongenetwork.RPG.XP.XP;
import tk.spongenetwork.RPG.XP.XPManager;
import tk.spongenetwork.RPG.XP.XPTypeYield;
import tk.spongenetwork.RPG.XP.XPYield;

public class EntityDeathEH implements Listener {

	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		LivingEntity ent = e.getEntity();
		Player kill = ent.getKiller();
		if (kill != null) {
			GameMode gm = kill.getGameMode();
			if (gm == GameMode.SURVIVAL || gm == GameMode.ADVENTURE) {
				EntityType et = ent.getType();
				if (DataController.huntYield.containsKey(et)) {
					XPYield xpy = DataController.huntYield.get(et);
					XP xp = XPManager.getPlayerXP(kill.getName());
					for (XPTypeYield xpty : xpy.getYield()) {
						int i = xpty.getAmount();
						if (i > 0) {
							xp.addXP(xpty.getType(), i);
						} else if (i < 0) {
							xp.removeXP(xpty.getType(), i);
						}
					}
				}
			}
		}
	}
}
