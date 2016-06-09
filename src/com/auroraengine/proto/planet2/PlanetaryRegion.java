/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.proto.planet2;

import com.auroraengine.math.LDVolume;

/**
 *
 * @author Arthur
 */
public abstract class PlanetaryRegion {
	public PlanetaryRegion(LDVolume v) {
		this.volume = v;
	}
	private final LDVolume volume;
	
	public final LDVolume getVolume() {
		return volume;
	}
}
