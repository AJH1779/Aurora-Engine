/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.server;

import com.auroraengine.debug.AuroraException;
import com.auroraengine.debug.AuroraLogs;
import com.auroraengine.threading.SynchroCore;
import java.util.logging.Logger;

/**
 *
 * @author Arthur
 */
public class WorldCore extends SynchroCore {

	private static final Logger LOG = AuroraLogs.getLogger(WorldCore.class);

	public WorldCore(String name, SynchroCore dependent) {
		super(name, dependent);
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
	protected void shutdown() {

	}

	@Override
	protected void processException(AuroraException ex) {

	}
}
