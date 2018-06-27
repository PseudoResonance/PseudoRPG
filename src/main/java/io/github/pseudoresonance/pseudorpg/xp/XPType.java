package io.github.pseudoresonance.pseudorpg.xp;

import org.bukkit.boss.BarColor;

public enum XPType {
	
	WOOD("wood", "Wood", BarColor.GREEN),
	MINING("mining", "Mining", BarColor.WHITE),
	FISHING("fishing", "Fishing", BarColor.BLUE),
	HUNTING("hunting", "Hunting", BarColor.PURPLE),
	SMELTING("smelting", "Smelting", BarColor.PINK),
	CRAFTING("crafting", "Crafting", BarColor.YELLOW),
	BLACKSMITH("blacksmith", "Blacksmith", BarColor.RED);
	
	String id;
	String name;
	BarColor barcolor;
	
	XPType(String id, String name, BarColor barcolor) {
		this.id = id;
		this.name = name;
		this.barcolor = barcolor;
	}
	
	public String getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public BarColor getBarColor() {
		return this.barcolor;
	}

}
