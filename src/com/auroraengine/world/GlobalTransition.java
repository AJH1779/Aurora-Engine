/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.world;

import com.auroraengine.math.HDRef;

/**
 * 
 * @author ajh17
 */
public class GlobalTransition implements Transition<GlobalRegion, HDRef> {
	
	private final GlobalRegion dest;
	private final HDRef transform;
	
	public GlobalTransition(GlobalRegion dest, HDRef transform) {
		this.dest = dest;
		this.transform = new HDRef(transform);
	}
	
	@Override
	public GlobalRegion getDestination() {
		return dest;
	}
	
	@Override
	public HDRef getTransform() {
		return transform.toHD();
	}
	
	@Override
	public HDRef transform(HDRef ref) {
		return HDRef.mult(ref, this.transform); // NOTE: This may be the wrong way around
		// TODO: Test this.
	}
}
