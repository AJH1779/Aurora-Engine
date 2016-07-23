/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.server;

import com.auroraengine.data.Server;
import com.auroraengine.debug.AuroraException;
import com.auroraengine.debug.AuroraLogs;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TODO: Make this a better system for exception handling, probably by making it
 * tell to the logger in a more pleasurable manner.
 *
 * @author Arthur
 */
@Server
@SuppressWarnings({"LeakingThisInConstructor", "serial"})
public class ServerException extends AuroraException {

	private static final Logger LOG = AuroraLogs.getLogger(ServerException.class);

	public ServerException() {
		super();
		LOG.log(Level.WARNING, "Server Exception Thrown: ", this);
	}

	public ServerException(String msg) {
		super(msg);
		LOG.log(Level.WARNING, "Server Exception Thrown: ", this);
	}

	public ServerException(String msg, Throwable cause) {
		super(msg, cause);
		LOG.log(Level.WARNING, "Server Exception Thrown: ", this);
	}

	public ServerException(String msg, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(msg, cause, enableSuppression, writableStackTrace);
		LOG.log(Level.WARNING, "Server Exception Thrown: ", this);
	}

	public ServerException(Throwable cause) {
		super(cause);
		LOG.log(Level.WARNING, "Server Exception Thrown: ", this);
	}
}
