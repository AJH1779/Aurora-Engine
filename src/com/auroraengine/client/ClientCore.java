/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.client;

import com.auroraengine.data.Client;
import com.auroraengine.data.ProgramProperties;
import com.auroraengine.debug.AuroraException;
import com.auroraengine.debug.AuroraLogs;
import com.auroraengine.opengl.GLCore;
import com.auroraengine.threading.SynchroCore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the core of the client program, the main thread from which all the
 * other threads will be controlled and managed. In the event that this thread
 * fails, all other threads should be closed gracefully. In the event that
 * another thread fails, this thread will attempt to recover as gracefully as it
 * can, or closing after performing the best error data collection and
 * potentially covering any lost data so a restore can be made.
 *
 * @author Arthur John Hills
 * @version 0.0.1 Development
 */
@Client
public class ClientCore extends SynchroCore {

	private static final Logger LOG = AuroraLogs.getLogger(ClientCore.class);

	/**
	 * Creates a client core with the specified program properties and the
	 * provided user session.
	 *
	 * @param properties The program properties.
	 * @param session The user session.
	 * @throws AuroraException
	 */
	public ClientCore(ProgramProperties properties, Session session)
			throws AuroraException {
		super("Client Core");
		this.properties = properties;
		this.session = session;
		this.glcore = new GLCore(this);
	}

	private final ProgramProperties properties;
	private final Session session;
	private final GLCore glcore;

	/**
	 * Returns the program properties.
	 *
	 * @return The program properties.
	 */
	public final ProgramProperties getProperties() {
		return properties;
	}

	/**
	 * Returns the user session.
	 *
	 * @return The user session.
	 */
	public final Session getSession() {
		return session;
	}

	@Override
	protected void initialise() throws ClientException {
		LOG.info("Initialising");
		LOG.info("Starting GLCore");
		waitForStart(glcore, 8);
		LOG.info("Started GLCore");
		LOG.info("Initialised");
	}

	@Override
	protected boolean isRunning() throws ClientException {
		// TODO: Adjust this!
		return glcore.getAlive();
	}

	@Override
	protected void update() throws ClientException {
		try {
			Thread.sleep(200);
		} catch (InterruptedException ex) {}
	}

	@Override
	protected void shutdown() {
		LOG.info("Shutting Down");
		waitForStop(glcore);
		LOG.info("Shut Down");
	}

	@Override
	protected void processException(AuroraException ex) {
		LOG.log(Level.SEVERE, "Fatal Exception: {0}", ex);
	}
}
