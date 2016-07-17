/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.world;

import com.auroraengine.math.LDRef;
import com.auroraengine.math.LDVec;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * A local region is one which operates on the floating point scale.
 * @author Arthur
 * @param <ParentRegion>
 * @param <ParentTransition>
 */
public abstract class LocalRegion<ParentRegion, ParentTransition extends Transition<ParentRegion, ?>> {

	private final ParentTransition parent;
	private final HashMap<LocalBoundary, LocalTransition> neighbours = new HashMap<>();
	private final HashSet<LocalTransition> children = new HashSet<>();
	
	public LocalRegion(ParentTransition toparent) {
		this.parent = toparent; // TODO: Make duplicate?
	}
	
	public ParentRegion getParentRegion() {
		return parent.getDestination();
	}
	
	public ParentTransition getToParent() {
		return parent;
	}
	
	public LDRef transform(LDRef ref) {
		LDVec p = ref.getOrigin();
		for(Entry<LocalBoundary, LocalTransition> neighbour : neighbours.entrySet()) {
			if(neighbour.getKey().isBeyond(p)) {
				if(neighbour.getValue() == null) {
					return null; // TODO: Change this to transform a coordinate to a coordinate.
				} else {
					return neighbour.getValue().getDestination().transform(neighbour.getValue().transform(ref));
					// TODO: Make this less amiguous.
					// TODO: Protect against rogue loops.
				}
			}
		}
		return ref;
	}
}
