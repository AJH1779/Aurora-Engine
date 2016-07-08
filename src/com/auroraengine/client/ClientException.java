/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.client;

import com.auroraengine.debug.AuroraException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TODO: Make this a better system for exception handling, probably by making it
 * tell to the logger in a more pleasurable manner.
 * @author Arthur
 */
public class ClientException extends AuroraException {
	@SuppressWarnings("compatibility:5008359297044540141")
	private static final long serialVersionUID = 8677486320751841264L;
	private static final Logger LOG = Logger.getLogger(ClientException.class.getName());

	public ClientException() {
		super();
		LOG.log(Level.WARNING, "Client Exception Thrown: ", this);
	}
	public ClientException(String msg) {
		super(msg);
		LOG.log(Level.WARNING, "Client Exception Thrown: ", this);
	}
	public ClientException(String msg, Throwable cause) {
		super(msg, cause);
		LOG.log(Level.WARNING, "Client Exception Thrown: ", this);
	}
	public ClientException(String msg, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(msg, cause, enableSuppression, writableStackTrace);
		LOG.log(Level.WARNING, "Client Exception Thrown: ", this);
	}
	public ClientException(Throwable cause) {
		super(cause);
		LOG.log(Level.WARNING, "Client Exception Thrown: ", this);
	}
}
