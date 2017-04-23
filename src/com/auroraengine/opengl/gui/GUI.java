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
package com.auroraengine.opengl.gui;

import com.auroraengine.debug.AuroraLogs;
import com.auroraengine.opengl.viewport.Viewport;
import java.util.ArrayList;
import java.util.logging.Logger;

public class GUI extends GUIObject {
	private static final Logger LOG = AuroraLogs.getLogger(GUI.class.getName());

	public GUI(Viewport view) {
		this.viewport = view;
	}
	private final Viewport viewport;

	private final ArrayList<GUIObject> front_to_back = new ArrayList<>(4),
					back_to_front = new ArrayList<>(4);

	@Override
	public void renderOpaque() {
		// Render each opaque thing
		front_to_back.forEach((r) -> renderOpaque());
	}

	@Override
	public void renderTranslucent() {
		// Render each translucent thing
		back_to_front.forEach((r) -> renderTranslucent());
	}
}
