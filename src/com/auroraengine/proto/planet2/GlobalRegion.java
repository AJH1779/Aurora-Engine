/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.proto.planet2;

/**
 * The global regions 
 * @author Arthur
 */
public class GlobalRegion extends PlanetaryRegion {
	public GlobalRegion(Planet planet, PlanetaryShape shape, int gn) {
		this.planet = planet;
		this.children = new ContinentalRegion[shape.getContinentalRegionsPerGlobal()];
		this.face = shape.getGlobalFace(gn);
	}
	private final Planet planet;
	private final ContinentalRegion[] children;
	private final PlanetaryFace face;
}
