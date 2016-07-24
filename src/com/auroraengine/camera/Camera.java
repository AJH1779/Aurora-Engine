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
import org.lwjgl.opengl.GL11;
import plan.auroraengine.world.LocalPosition;
import plan.auroraengine.world.LocalVelocity;

/**
 *
 * @author ajh17
 */
public class Camera extends Entity {

	public Camera(LocalPosition pos) {
		super(pos);
	}

	public Camera(LocalPosition pos, LocalVelocity vel) {
		super(pos, vel);
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
		
		GL11.glMultMatrix(this.getPosition().getPosition().toLD().invert().buffer());
		
		this.getRegion().render(this, view, frustrum);
	}

	private Frustrum calculateFrustrum(Viewport view) {
		// TODO: Make frustrum
		throw new UnsupportedOperationException("Not Yet Implementeds");
	}
}
