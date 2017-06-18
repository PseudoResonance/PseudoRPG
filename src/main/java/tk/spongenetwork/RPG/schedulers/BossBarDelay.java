package tk.spongenetwork.RPG.schedulers;

import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;

public class BossBarDelay extends BukkitRunnable {
	
	private BossBar bb;
	
	public BossBarDelay(BossBar bb) {
		this.bb = bb;
	}

	@Override
	public void run() {
		bb.setVisible(false);
	}

}
