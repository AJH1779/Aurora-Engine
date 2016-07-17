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
public class ServerCore extends SynchroCore {
	private static final Logger LOG = AuroraLogs.getLogger(ServerCore.class);

	public ServerCore(String name) {
		super(name);
	}
	public ServerCore(String name, SynchroCore dependent) {
		super(name, dependent);
	}

	@Override
	protected void initialise() throws AuroraException {
		
	}

	@Override
	protected boolean isRunning() throws AuroraException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	protected void update() throws AuroraException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	protected void processException(AuroraException ex) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	protected void shutdown() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
