/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author Arthur
 */
public class ServerCore extends SynchroCore {

	private static final Logger LOG = AuroraLogs.getLogger(ServerCore.class);

	public ServerCore(String name, ProgramProperties properties)
			throws ServerException {
		this(name, properties, null);
	}

	public ServerCore(String name, ProgramProperties properties, SynchroCore dependent)
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
	protected void initialise() throws ServerException {
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
	protected boolean isRunning() throws ServerException {
		return network.getAlive() && world.getAlive();
	}

	@Override
	protected void update() throws ServerException {

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
