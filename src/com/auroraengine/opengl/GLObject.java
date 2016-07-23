/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.opengl;

import com.auroraengine.client.ClientException;

/**
 *
 * @author Arthur
 */
public interface GLObject {
	/**
	 * Must be called before create() can be used, unless otherwise specified.
	 * Loads any resources connected to the object, can be called by any thread.
	 * @throws ClientException 
	 */
	public void load() throws ClientException;
	/**
	 * Can be called before destroy() is used, removing the data stored on the
	 * non-GL side. load() must be called before create() may be used again.
	 * @throws ClientException 
	 */
	public void unload() throws ClientException;
	
	/**
	 * Returns true if and only if the object can be created using create().
	 * @return True if the object can be created with create().
	 */
	public boolean isLoaded();
	
	/**
	 * Must be called to make this object usable by the GL thread.
	 * @throws GLException 
	 */
	public void create() throws GLException;
	/**
	 * Returns true if and only if the object can be modified using update() and
	 * destroyed using destroy()
	 * @return
	 */
	public boolean isCreated();
	/**
	 * Must be called by the GL context whenever the object changes.
	 * @throws GLException 
	 */
	public void update() throws GLException;
	/**
	 * Must always be called by a created object on shutdown by the GL thread.
	 */
	public void destroy();
}
