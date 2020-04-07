package io.github.pseudoresonance.pseudorpg.xp;

import org.bukkit.boss.BarColor;

public enum XPType {
	
	WOOD(BarColor.GREEN),
	MINING(BarColor.WHITE),
	FISHING(BarColor.BLUE),
	HUNTING(BarColor.PURPLE),
	SMELTING(BarColor.PINK),
	CRAFTING(BarColor.YELLOW),
	BLACKSMITH(BarColor.RED);
	
	BarColor barcolor;
	
	XPType(BarColor barcolor) {
		this.barcolor = barcolor;
	}
	
	public BarColor getBarColor() {
		return this.barcolor;
	}

}
