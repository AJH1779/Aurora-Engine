/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.proto.planet2;

/**
 *
 * @author Arthur
 */
public class Planet {
	
	public Planet(PlanetaryShape[] shapes, int[][] neighbours,
					BorderType[][] borders) {
		if(shapes == null) {
			throw new NullPointerException("Shapes provided is null!");
		}
		if(neighbours == null) {
			throw new NullPointerException("Neighbours provided is null!");
		}
		if(borders == null) {
			throw new NullPointerException("Borders provided is null!");
		}
		if(shapes.length != neighbours.length || shapes.length != borders.length) {
			throw new IllegalArgumentException("The array lengths are not the same!");
		}
		
		int ias[] = new int[shapes.length + 1];
		for(int i = 0; i < shapes.length; i++) {
			ias[i+1] = ias[i] + shapes[i].getGlobalRegions();
		}
		children = new GlobalRegion[ias[shapes.length]];
		
		for(int i = 0; i < shapes.length; i++) {
			for(int j = 0; j < shapes[i].getGlobalRegions(); j++) {
				children[ias[i] + j] = new GlobalRegion(this, shapes[i], i);
			}
		}
	}
	
	/**
	 * Every planet is guaranteed to have all global regions loaded by necessity.
	 */
	private final GlobalRegion[] children;
}