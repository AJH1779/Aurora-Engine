/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.world;

import com.auroraengine.debug.AuroraException;
import com.auroraengine.threading.SynchroCore;

/**
 *
 * @author LittleRover
 */
public class Dimension extends SynchroCore {
	public Dimension(String name, SynchroCore master) {
		super(name, master);
	}
	
	@Override
	protected void initialise() throws AuroraException {
		
	}

	@Override
	protected boolean isRunning() throws AuroraException {
		return true;
	}

	@Override
	protected void update() throws AuroraException {
		
	}

	@Override
	protected void processException(AuroraException ex) {
		
	}

	@Override
	protected void shutdown() {
		
	}
}
