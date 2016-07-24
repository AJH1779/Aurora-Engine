/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.opengl.gui;

import com.auroraengine.debug.AuroraLogs;
import com.auroraengine.opengl.viewport.Viewport;
import java.util.ArrayList;
import java.util.logging.Logger;

public class GUI extends GUIObject {
	private static final Logger LOG = AuroraLogs.getLogger(GUI.class);

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
