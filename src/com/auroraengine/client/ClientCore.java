/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.client;

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
 * another thread fails, this thread will attempt to recover as gracefully as
 * it can, or closing after performing the best error data collection and
 * potentially covering any lost data so a restore can be made.
 * @author Arthur John Hills
 * @version 0.0.1 Development
 */
public class ClientCore extends SynchroCore {
    private static final Logger LOG = AuroraLogs.getLogger(ClientCore.class);

    private final ProgramProperties properties;
    private final Session session;
    private final GLCore glcore;
	/**
	 * Creates a client core with the specified program properties and the
	 * provided user session.
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

	/**
	 * Returns the program properties.
	 * @return The program properties.
	 */
    public final ProgramProperties getProperties() { return properties; }
	/**
	 * Returns the user session.
	 * @return The user session.
	 */
    public final Session getSession() { return session; }

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
        return glcore.getThreading();
    }
    @Override
    protected void update() throws ClientException {
		// TODO: Include Thread health checking
		if(!glcore.getRunning()) {
			// Try to recover it somehow or call it as a failure.
		}
    }
    @Override
    protected void shutdown() {
        LOG.info("Shutting Down");
        waitForStop(glcore);
        LOG.info("Shut Down");
        System.exit(0);
    }

    @Override
    protected void processException(AuroraException ex) {
        LOG.log(Level.SEVERE, "Fatal Exception: {0}", ex);
    }
}
