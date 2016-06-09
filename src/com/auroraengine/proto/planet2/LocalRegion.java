/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.proto.planet2;

/**
 * The local region is an area on the planet that the player can move around on.
 * 
 * @author Arthur
 */
public class LocalRegion extends PlanetaryRegion {
	public LocalRegion(ContinentalRegion parent) {
		this.parent = parent;
	}
	private final ContinentalRegion parent;
}