package com.auroraengine.opengl.shaders;

import com.auroraengine.client.ClientException;
import com.auroraengine.data.Client;
import com.auroraengine.opengl.GLException;
import com.auroraengine.opengl.GLObject;
import com.auroraengine.opengl.GLVersion;

import java.io.File;

/**
 * An object which describes a shader program.
 * Currently not made.
 *
 * There should be something like this already, go find it.
 */
@Client
@GLVersion(version="GL20")
public class ShaderCode implements GLObject {
	public ShaderCode(File f) {
		// TODO: Make this read a file for the script.
	}
	public ShaderCode(String code) {
		
	}
	
	public boolean setCode(String code) {
		return false;
	}
	
	@Override
	public void load() throws ClientException {
		// Should create this object from the provided information.
	}

	@Override
	public void unload() throws ClientException {
		// TODO Implement this method
	}

	@Override
	public void create() throws GLException {
		// TODO Implement this method
	}

	@Override
	public void update() throws GLException {
		// TODO Implement this method
	}
	
	@Override
	public void destroy() {
		
	}
}
