package com.auroraengine.opengl.shaders;

import com.auroraengine.client.ClientException;
import com.auroraengine.data.Client;
import com.auroraengine.opengl.GLException;
import com.auroraengine.opengl.GLObject;
import com.auroraengine.opengl.GLVersion;

import org.lwjgl.opengl.GL20;

/**
 * An object which describes a shader program.
 * Currently not made.
 */
@Client
@GLVersion(version="GL20")
public class ShaderProgram implements GLObject {
	
	public ShaderProgram(ShaderCode vertex, ShaderCode fragment) {
		// TODO: Check that the included codes are actually allowed (?)
		this.vertex = vertex; this.fragment = fragment;
	}
	
	private int ref = 0;
	
	private ShaderCode vertex, fragment;
	
	/**
	 * Returns true if the program is currently available.
	 * @return
	 */
	public boolean isAlive() {
		return ref > 0;
	}
	
	/**
	 * Returns the program reference which allows the program to bind. Should
	 * be quite pointless outside of this method and probably could be made
	 * useless.
	 * @return
	 */
	public int getRef() {
		return ref;
	}
	
	/**
	 * Sets the current program to this program.
	 */
	public boolean bind() {
		if(isAlive()) {
			GL20.glUseProgram(ref); return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Deletes this program.
	 * TODO: Check if the program is actually deleted and stuff?
	 */
	public boolean delete() {
		if(isAlive()) {
			GL20.glDeleteProgram(ref);
			ref = 0;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void load() throws ClientException {
		
	}

	@Override
	public void unload() throws ClientException {
		
	}

	@Override
	public void create() throws GLException {
		
	}

	@Override
	public void update() throws GLException {
		
	}

	@Override
	public void destroy() {
		delete();
	}
}
