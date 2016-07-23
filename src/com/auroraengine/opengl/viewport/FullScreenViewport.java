/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.opengl.viewport;

import com.auroraengine.opengl.GLException;
import com.auroraengine.opengl.GLWindow;


public class FullScreenViewport extends Viewport {
	
	public FullScreenViewport(GLWindow window) {
		this.window = window;
	}
	private final GLWindow window;
	
	public int getX() {
		return 0;
	}
	public int getY() {
		return 0;
	}
	public int getWidth() {
		return window.getWidth();
	}
	public int getHeight() {
		return window.getHeight();
	}
}
