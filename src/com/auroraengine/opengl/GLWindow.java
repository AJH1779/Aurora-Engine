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

import org.lwjgl.opengl.GL11;

/**
 *
 * @author LittleRover
 */
public interface GLWindow extends GLObject {
	/**
	 * Creates the window. Should be called first before other methods and should
	 * not be called unless destroy() has been called since.
	 *
	 * @throws GLException
	 */
	@Override
	public void create()
					throws GLException;

	/**
	 * Returns true if the window has been requested to close, such as through the
	 * hotkey Alt-F4, pressing the kill window button, or by the system OS.
	 *
	 * @return True if the window has been asked to close.
	 *
	 * @throws GLException
	 */
	public boolean isCloseRequested()
					throws GLException;

	/**
	 * Must be called for every frame, updating the position and shape of the
	 * window and context.
	 *
	 * @throws GLException
	 */
	@Override
	public void update()
					throws GLException;

	/**
	 * Returns a copy of the current settings used for the display.
	 *
	 * @return
	 */
	public GLOptions getGLOptions();

	/**
	 * Sets new settings for the display, implemented on the next call to
	 * update().
	 *
	 * @param new_ops
	 */
	public void setGLOptions(GLOptions new_ops);

	/**
	 * Destroys the display. Should be called after create() and must be called
	 * before quitting the program.
	 */
	@Override
	public void destroy();

	// TODO: Tie this to the cameras rather than having it as a separate thing.
	/**
	 * Currently serves as a simple test for implementing some GL settings.
	 *
	 * @throws GLException
	 */
	public static void updateGL()
					throws GLException {
		GL11.glAlphaFunc(GL11.GL_GEQUAL, 0.5f);
		GL11.glDepthFunc(GL11.GL_LEQUAL);

		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	public int getWidth();

	public int getHeight();
}
