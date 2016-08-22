/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.camera;

import com.auroraengine.entity.Entity;
import com.auroraengine.opengl.GLException;
import com.auroraengine.opengl.shaders.ShaderLibrary;
import com.auroraengine.opengl.viewport.Viewport;
import com.auroraengine.world.LocalPosition;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author ajh17
 */
public class Camera extends Entity {

	public Camera(LocalPosition pos) {
		super(pos);
	}
	private ShaderLibrary shader_library;

	public void switchTo(Viewport view) throws GLException {
		// Calls the GL routines and perhaps the shader library associated with
		// this viewport.

	}

	public void render(Viewport view) throws GLException {
		// Intended to draw everything in its viewport.
		// Needs a way of getting things in the correct priority order.
		Frustrum frustrum = calculateFrustrum(view);

		// GL11.glMultMatrix(this.getPreviousPosition().getPosition().toLD().invert().buffer());

		// this.getRegion().render(this, view, frustrum);
	}

	private Frustrum calculateFrustrum(Viewport view) {
		// TODO: Make frustrum
		throw new UnsupportedOperationException("Not Yet Implementeds");
	}

	@Override
	public void update(double delt) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
