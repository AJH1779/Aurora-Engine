/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.world;

import com.auroraengine.math.LDRef;

/**
 *
 * @author ajh17
 */
public class LocalTransition implements Transition<LocalRegion, LDRef> {
	
	private final LocalRegion dest;
	private final LDRef transform;
	
	public LocalTransition(LocalRegion dest, LDRef transform) {
		this.dest = dest;
		this.transform = new LDRef(transform);
	}
	
	@Override
	public LocalRegion getDestination() {
		return dest;
	}
	
	@Override
	public LDRef getTransform() {
		return transform.toLD();
	}
	
	@Override
	public LDRef transform(LDRef ref) {
		return LDRef.mult(ref, this.transform); // NOTE: This may be the wrong way around
		// TODO: Test this.
	}
	
	public LocalNode transform(LocalNode n) {
		n.setPosition(transform(n.getPosition()));
		n.setRegion(dest);
		return n;
	}
}
