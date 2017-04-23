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
package com.auroraengine.opengl;

import com.auroraengine.client.ClientException;
import com.auroraengine.data.Client;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.opengl.Util;

/**
 * Exceptions called exclusively on the OpenGL thread. If this is called on a
 * thread that does not own the GL context, then something has gone wrong.
 *
 * @author LittleRover
 */
@Client
@SuppressWarnings("LeakingThisInConstructor")
public class GLException extends ClientException {
	private static final Logger LOG = Logger
					.getLogger(GLException.class.getName());
	private static final long serialVersionUID = 1L;

	public static void checkGL(String msg)
					throws GLException {
		try {
			Util.checkGLError();
		} catch (OpenGLException err) {
			throw new GLException(msg, err);
		}
	}

	/**
	 * Creates a new OpenGL exception carrying the specified message.
	 *
	 * @param msg The message.
	 */
	public GLException(String msg) {
		super(msg);
		LOG.log(Level.WARNING, "GL Exception Thrown: ", this);
	}

	/**
	 * Creates a new OpenGL exception caused by the specified throwable.
	 *
	 * @param cause The cause of this exception.
	 */
	public GLException(Throwable cause) {
		super(cause);
		LOG.log(Level.WARNING, "GL Exception Thrown: ", this);
	}

	/**
	 * Creates a new OpenGL exception carrying the specified message caused by the
	 * specified throwable.
	 *
	 * @param msg   The message.
	 * @param cause The cause of this exception.
	 */
	public GLException(String msg, Throwable cause) {
		super(msg, cause);
		LOG.log(Level.WARNING, "GL Exception Thrown: ", this);
	}
}
