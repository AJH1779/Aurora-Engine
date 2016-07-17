/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.world;

import com.auroraengine.math.LDRef;

/**
 * Denotes a point in the local 3D space. Has zero dimensions of size.
 * @author ajh17
 */
public class LocalNode {
	
	private LocalRegion reg;
	private final LDRef ref;
	
	/**
	 * Creates a new local node at the specified location.
	 * @param reg
	 * @param ref 
	 */
	public LocalNode(LocalRegion reg, LDRef ref) {
		this.reg = reg;
		this.ref = new LDRef(ref);
	}
	
	/**
	 * Returns the region this node is located in.
	 * @return 
	 */
	public LocalRegion getRegion() {
		return reg;
	}
	
	/**
	 * Returns the position of the node within the region.
	 * @return 
	 */
	public LDRef getPosition() {
		return ref.toLD();
	}
	
	/**
	 * Sets the 
	 * @param v 
	 */
	public void setPosition(LDRef v) {
		this.ref.set(v);
	}
	
	/**
	 * Sets the region but leaves the position unchanged.
	 * @param reg 
	 */
	public void setRegion(LocalRegion reg) {
		this.reg = reg;
	}
}
