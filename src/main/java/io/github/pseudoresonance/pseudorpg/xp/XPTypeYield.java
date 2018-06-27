package io.github.pseudoresonance.pseudorpg.xp;

public class XPTypeYield {
	
	private XPType type;
	private int amount;
	
	public XPTypeYield(XPType type, int amount) {
		this.type = type;
		this.amount = amount;
	}
	
	public XPType getType() {
		return this.type;
	}
	
	public int getAmount() {
		return this.amount;
	}

}
