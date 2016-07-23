/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.opengl.gui;

import com.auroraengine.opengl.viewport.Viewport;


public class GUI extends GUIObject {
	public GUI(Viewport view) {
		this.viewport = view;
	}
	private final Viewport viewport;
}
