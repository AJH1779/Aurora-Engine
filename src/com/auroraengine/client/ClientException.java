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
package com.auroraengine.client;

import com.auroraengine.data.Client;
import com.auroraengine.debug.AuroraException;
import com.auroraengine.debug.AuroraLogs;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An exception called by a client-side thread. Note that if this is called by a
 * server, something has gone wrong.
 *
 * TODO: Make this a better system for exception handling, probably by making it
 * tell to the logger in a more pleasurable manner.
 *
 * @author LittleRover
 */
@Client
@SuppressWarnings("LeakingThisInConstructor")
public class ClientException extends AuroraException {
	private static final Logger LOG = AuroraLogs.getLogger(ClientException.class
					.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new client exception carrying the specified message.
	 *
	 * @param msg The message.
	 */
	public ClientException(String msg) {
		super(msg);
		LOG.log(Level.WARNING, "Client Exception Thrown: ", this);
	}

	/**
	 * Creates a new client exception caused by the specified throwable.
	 *
	 * @param cause The cause of this exception.
	 */
	public ClientException(Throwable cause) {
		super(cause);
		LOG.log(Level.WARNING, "Client Exception Thrown: ", this);
	}

	/**
	 * Creates a new client exception carrying the specified message caused by the
	 * specified throwable.
	 *
	 * @param msg   The message.
	 * @param cause The cause of this exception.
	 */
	public ClientException(String msg, Throwable cause) {
		super(msg, cause);
		LOG.log(Level.WARNING, "Client Exception Thrown: ", this);
	}
}
