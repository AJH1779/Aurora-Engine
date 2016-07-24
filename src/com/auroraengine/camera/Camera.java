/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.camera;

import com.auroraengine.entity.Entity;
import com.auroraengine.opengl.viewport.Viewport;
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

	public void render(Viewport view) {

	}
}
