/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.world;

import java.util.HashSet;
import java.util.logging.Logger;

public class SolarSystem extends CelestialFeature {

	private static final Logger LOG = Logger.getLogger(SolarSystem.class.getName());

	public SolarSystem() {
		//
	}

	private final HashSet<Planet> planets = new HashSet<>();
	private final HashSet<Planet> stars = new HashSet<>();
}
