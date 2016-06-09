/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.model;

import com.auroraengine.opengl.GLMaterial;
import com.auroraengine.opengl.GLObject;

/**
 *
 * @author Arthur
 */
public abstract class GLModel implements GLObject {
	private GLMaterial material;

	public GLModel(VertexBuffer vb, IndexBuffer ib) {
		vertexbuffer = vb; indexbuffer = ib;
	}
	private final VertexBuffer vertexbuffer;
	private final IndexBuffer indexbuffer;
	
	// Defines vertex patches for the index buffer
	
	public void draw() {
		vertexbuffer.bind();
		indexbuffer.bind();
		// Bind the textures
		
		vertexbuffer.enableClientState();
		
		indexbuffer.draw();
		
		vertexbuffer.disableClientState();
	}
}
