package io.github.pseudoresonance.pseudorpg.xp;

public class XPLevel {
	
	private int level = 0;
	private int xp = 0;
	
	public XPLevel(int level, int xp) {
		this.level = level;
		this.xp = xp;
	}
	
	public int getLevel() {
		return this.level;
	}
	
	public int getXP() {
		return this.xp;
	}

}
