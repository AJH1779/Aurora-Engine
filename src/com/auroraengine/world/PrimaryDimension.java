/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.world;

import com.auroraengine.threading.SynchroCore;
import java.util.HashSet;
import java.util.logging.Logger;

/**
 *
 * @author LittleRover
 */
public class PrimaryDimension extends Dimension {

	private static final Logger LOG = Logger.getLogger(PrimaryDimension.class.getName());

	public PrimaryDimension(String name, SynchroCore master) {
		super(name, master);
	}

	private final HashSet<CelestialFeature> celestial_features = new HashSet<>();
}
