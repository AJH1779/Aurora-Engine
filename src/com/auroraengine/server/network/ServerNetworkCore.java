/*
 * Copyright (C) 2017 LittleRover
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
 * This is the manager of the networking side of the server, a thread separate
 * from the other processes of the server.
 *
 * TODO: Not Yet Implemented
 *
 * @author LittleRover
 */
public class ServerNetworkCore extends SynchroCore {

	private static final Logger LOG = AuroraLogs
					.getLogger(ServerNetworkCore.class.getName());

	/**
	 * Creates a new server network core with the specified name that runs so long
	 * as the specified server core is running.
	 *
	 * @param name   The name of the server
	 * @param master The server core to run with
	 *
	 * @throws ServerException If an exception occurs when creating.
	 */
	public ServerNetworkCore(String name, ServerCore master)
					throws ServerException {
		super(name, master);
	}

	private final Set<ClientConnection> clients = Collections.synchronizedSet(
					new HashSet<>(8));

	@Override
	protected void initialise()
					throws ServerException {
		// Need to begin by opening the broadcasting ports.

		// Need to open the connections for others to join to.
	}

	@Override
	protected boolean isRunning()
					throws ServerException {
		return true;
	}

	@Override
	protected void processException(AuroraException ex) {
		// Broadcast the exception to all admin clients so they know what the issue
		// is.
	}

	@Override
	protected void shutdown() {
		// Close all of the sockets and be sure to send termination packets
		// accordingly.
	}

	@Override
	protected void update()
					throws ServerException {
		// Need to figure out and perform the appropriate handling of packets.
		// The threads here either perform requests to the world thread or
		// provide information to the the world thread.
	}
}
