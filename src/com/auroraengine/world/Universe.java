/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.world;

import com.auroraengine.debug.AuroraException;
import com.auroraengine.debug.AuroraLogs;
import com.auroraengine.threading.SynchroCore;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LittleRover
 */
public class Universe extends SynchroCore {
	private static final Logger LOG = AuroraLogs.getLogger(Universe.class
					.getName());

	public Universe(String name, SynchroCore master) {
		super(name, master);
	}
	private final HashSet<Dimension> dimensions = new HashSet<>(1);

	@Override
	protected void initialise()
					throws AuroraException {
		// The dimensions are initialised here from some form of file that designates
		// which ones are to be loaded.
	}

	@Override
	protected boolean isRunning()
					throws AuroraException {
		return dimensions.size() > 0;
	}

	@Override
	protected void processException(AuroraException ex) {
		LOG.log(Level.SEVERE, "Universe Fatal Exception: ", ex);
	}

	@Override
	protected void shutdown() {
		// Close down all dimensions safely.

	}

	@Override
	protected void update()
					throws AuroraException {
		// This checks the health and compatibility of the dimensions.
	}

}
