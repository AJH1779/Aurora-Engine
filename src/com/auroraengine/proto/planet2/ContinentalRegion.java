/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.proto.planet2;

/**
 * The continental regions contain local regions which are brought in as
 * required.
 * 
 * The continental regions are not a required level of detail for processing,
 * with the Global regions being used for processing across the planet whilst
 * the continental regions can be used for allocating structures and the like
 * more locally without the extreme precision of the local regions.
 * 
 * Might not be, might have to be the level of detail required for handling the
 * world with precision.
 * 
 * @author Arthur
 */
public class ContinentalRegion extends PlanetaryRegion {
	public ContinentalRegion(GlobalRegion parent, PlanetaryFace face, int cn) {
		this.parent = parent;
		this.children = new LocalRegion[face.getSubRegions()];
		this.face = face.getSubRegion(cn);
	}
	private final GlobalRegion parent;
	private final LocalRegion[] children;
	private final PlanetaryFace face;
}
