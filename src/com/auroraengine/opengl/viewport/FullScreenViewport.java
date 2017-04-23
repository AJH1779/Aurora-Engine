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
package com.auroraengine.opengl.viewport;

import com.auroraengine.debug.AuroraLogs;
import com.auroraengine.opengl.GLWindow;
import java.util.logging.Logger;

public class FullScreenViewport extends Viewport {
	private static final Logger LOG = AuroraLogs.getLogger(
					FullScreenViewport.class.getName());

	public FullScreenViewport(GLWindow window) {
		this.window = window;
	}
	private final GLWindow window;

	@Override
	public int getHeight() {
		return window.getHeight();
	}

	@Override
	public int getWidth() {
		return window.getWidth();
	}

	@Override
	public int getX() {
		return 0;
	}

	@Override
	public int getY() {
		return 0;
	}

}
