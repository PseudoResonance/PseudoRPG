package io.github.pseudoresonance.pseudorpg.xp;

import java.util.ArrayList;

public class XPYield {
	
	ArrayList<XPTypeYield> xpyield = new ArrayList<XPTypeYield>();
	
	public XPYield() {}
	
	public ArrayList<XPTypeYield> getYield() {
		return this.xpyield;
	}
	
	public void addYield(XPTypeYield xpty) {
		xpyield.add(xpty);
	}

}
