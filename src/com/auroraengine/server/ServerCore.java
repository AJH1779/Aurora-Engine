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
package com.auroraengine.server;

import com.auroraengine.data.ProgramProperties;
import com.auroraengine.debug.AuroraException;
import com.auroraengine.debug.AuroraLogs;
import com.auroraengine.server.network.ServerNetworkCore;
import com.auroraengine.threading.SynchroCore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LittleRover
 */
public class ServerCore extends SynchroCore {

	private static final Logger LOG = AuroraLogs.getLogger(ServerCore.class
					.getName());

	public ServerCore(String name, ProgramProperties properties)
					throws ServerException {
		this(name, properties, null);
	}

	public ServerCore(String name, ProgramProperties properties,
										SynchroCore dependent)
					throws ServerException {
		super(name, dependent);
		this.properties = properties;
		this.network = new ServerNetworkCore(name + " Network Core", this);
		this.world = new WorldCore(name + " World Core", this);
	}

	private final ProgramProperties properties;
	private final ServerNetworkCore network;
	private final WorldCore world;

	/**
	 * Returns the program properties.
	 *
	 * @return The program properties.
	 */
	public final ProgramProperties getProperties() {
		return properties;
	}

	@Override
	protected void initialise()
					throws ServerException {
		LOG.info("Initialising");
		LOG.info("Starting Network Core");
		waitForStart(network, 8);
		LOG.info("Started Network Core");
		LOG.info("Starting World Core");
		waitForStart(world, 8);
		LOG.info("Started World Core");
		LOG.info("Initialised");
	}

	@Override
	protected boolean isRunning()
					throws ServerException {
		return network.getAlive() && world.getAlive();
	}

	@Override
	protected void update()
					throws ServerException {

	}

	@Override
	protected void shutdown() {
		LOG.info("Shutting Down");
		waitForStop(world);
		waitForStop(network);
		LOG.info("Shut Down");
	}

	@Override
	protected void processException(AuroraException ex) {
		LOG.log(Level.SEVERE, "Fatal Exception: {0}", ex);
	}
}
