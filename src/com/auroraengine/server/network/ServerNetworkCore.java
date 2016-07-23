/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.server.network;

import com.auroraengine.debug.AuroraException;
import com.auroraengine.debug.AuroraLogs;
import com.auroraengine.server.ServerCore;
import com.auroraengine.server.ServerException;
import com.auroraengine.threading.SynchroCore;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 *
 * @author Arthur
 */
public class ServerNetworkCore extends SynchroCore {

	private static final Logger LOG = AuroraLogs.getLogger(ServerNetworkCore.class);

	public ServerNetworkCore(String name, ServerCore master) throws ServerException {
		super(name, master);
	}
	
	private final Set<ClientConnection> clients = Collections.synchronizedSet(new HashSet<>(8));

	@Override
	protected void initialise() throws AuroraException {
		// Need to begin by opening the broadcasting ports.
		
		// Need to open the connections for others to join to.
		
	}

	@Override
	protected boolean isRunning() throws AuroraException {
		return true;
	}

	@Override
	protected void update() throws AuroraException {
		// Need to figure out and perform the appropriate handling of packets.
		// The threads here either perform requests to the world thread or
		// provide information to the the world thread.
	}

	@Override
	protected void shutdown() {
		// Close all of the sockets and be sure to send termination packets
		// accordingly.
	}

	@Override
	protected void processException(AuroraException ex) {
		// Broadcast the exception to all admin clients so they know what the issue
		// is.
	}
}
