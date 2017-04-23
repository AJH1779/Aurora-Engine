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

import com.auroraengine.data.Server;
import com.auroraengine.debug.AuroraException;
import com.auroraengine.debug.AuroraLogs;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An exception to be called by server threads only. If it is called by any
 * other threads then something has gone wrong.
 *
 * TODO: Make this a better system for exception handling, probably by making it
 * tell to the logger in a more pleasurable manner.
 *
 * @author LittleRover
 */
@Server
@SuppressWarnings({"LeakingThisInConstructor", "serial"})
public class ServerException extends AuroraException {

	private static final Logger LOG = AuroraLogs.getLogger(ServerException.class
					.getName());

	/**
	 * Creates a new server exception carrying the specified message.
	 *
	 * @param msg The message.
	 */
	public ServerException(String msg) {
		super(msg);
		LOG.log(Level.WARNING, "Server Exception Thrown: ", this);
	}

	/**
	 * Creates a new server exception caused by the specified throwable.
	 *
	 * @param cause The cause of this exception.
	 */
	public ServerException(Throwable cause) {
		super(cause);
		LOG.log(Level.WARNING, "Server Exception Thrown: ", this);
	}

	/**
	 * Creates a new server exception carrying the specified message caused by the
	 * specified throwable.
	 *
	 * @param msg   The message.
	 * @param cause The cause of this exception.
	 */
	public ServerException(String msg, Throwable cause) {
		super(msg, cause);
		LOG.log(Level.WARNING, "Server Exception Thrown: ", this);
	}
}
