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

import com.auroraengine.client.Session;
import com.auroraengine.data.ProgramProperties;
import com.auroraengine.debug.AuroraLogs;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author LittleRover
 */
public class LWJGLWindow implements GLWindow {
	private static final Logger LOG = AuroraLogs.getLogger(LWJGLWindow.class
					.getName());

	/**
	 * Creates a window using the native LWJGL window package, using the specified
	 * properties and session settings.
	 *
	 * @param session
	 * @param properties
	 */
	public LWJGLWindow(Session session, ProgramProperties properties) {
		ops = new GLOptions(session, properties);
	}
	private final GLOptions ops;
	private GLOptions next_options;
	private volatile boolean iscreated = false;
	private int width, height, sync;

	@Override
	public void load() {
	}

	@Override
	public void unload() {
	}

	@Override
	public void create()
					throws GLException {
		LOG.info("Creating new LWJGL Window.");
		try {
			updateDisplay();
			Display.create();
		} catch (LWJGLException ex) {
			throw new GLException(ex);
		}

		LOG.info("LWJGL Window successfully created.");
		iscreated = true;

		// Then there is the generic GL Initialisation
		GLWindow.updateGL();
		LOG.warning("Still using the old GL system.");
	}

	private void updateDisplay()
					throws LWJGLException, GLException {
		LOG.info("Updating the Display using the Current Options.");
		if (ops.getBoolean("set_display_config")) {
			Display.setDisplayConfiguration(ops.getFloat("gamma"),
																			ops.getFloat("brightness"), ops.getFloat(
																			"contrast"));
		}
		Display.setTitle(ops.getString("title"));
		Display.setResizable(ops.getBoolean("resizeable"));
		Display.setVSyncEnabled(ops.getBoolean("vsync"));

		try {
			if (ops.getBoolean("fullscreen")) {
				width = ops.getInteger("fullscreen_width");
				height = ops.getInteger("fullscreen_height");
				sync = ops.getInteger("fullscreen_sync");
				if (width == -1 && height == -1 && sync == -1) {
					Display.setFullscreen(true);
				} else {
					DisplayMode[] dms = Display.getAvailableDisplayModes();
					if (width == -1) {
						width = Display.getDesktopDisplayMode().getWidth();
					}
					if (height == -1) {
						height = Display.getDesktopDisplayMode().getHeight();
					}
					if (sync == -1) {
						sync = Display.getDesktopDisplayMode().getFrequency();
					}
					for (DisplayMode dm : dms) {
						if (dm.getWidth() == width && dm.getHeight() == height &&
								dm.getFrequency() == sync) {
							Display.setDisplayModeAndFullscreen(dm);
							break;
						}
					}
					if (!Display.isFullscreen()) {
						Display.setFullscreen(true);
					}
				}
			}
		} catch (LWJGLException ex) {
			ops.set("fullscreen", false);
		}
		if (!ops.getBoolean("fullscreen")) {
			Display.setFullscreen(false);
			Display.setDisplayMode(new DisplayMode(ops.getInteger("windowed_width"),
																						 ops.getInteger("windowed_height")));
		}
		LOG.info("Finished Updating Window");
	}

	@Override
	public boolean isCloseRequested()
					throws GLException {
		return Display.isCloseRequested();
	}

	@Override
	public void update()
					throws GLException {
		Display.update();
		if (Display.wasResized()) {
			width = Display.getWidth();
			height = Display.getHeight();
		}
		if (next_options != null) {
			// Perform a screen update
			ops.set(next_options);
			next_options = null;
			try {
				updateDisplay();
			} catch (LWJGLException ex) {
				throw new GLException(ex);
			}
		}
		// Remove this and move to the camera?
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
	}

	@Override
	public GLOptions getGLOptions() {
		return new GLOptions(ops);
	}

	@Override
	public void setGLOptions(GLOptions new_ops) {
		next_options = new_ops;
	}

	@Override
	public void destroy() {
		iscreated = false;
		Display.destroy();
		LOG.info("Destroyed LWJGL Window");
	}

	@Override
	public boolean isLoaded() {
		return true;
	}

	@Override
	public boolean isCreated() {
		return iscreated;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}
}
