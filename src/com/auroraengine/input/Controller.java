/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.input;

import com.auroraengine.client.ClientException;

/**
 *
 * @author Arthur
 */
public interface Controller {
	public void initialise() throws ClientException;
	public void update() throws ClientException;
	public void shutdown();
	
	public boolean isMouseGrabbed();
	public void setMouseGrabbed(boolean grab);
	
	public int getX(); public int getY();
	public int getAbsX(); public int getAbsY();
	public int getDX(); public int getDY();
	public int getDW(); // Scroll wheel amount
	public char getRecentKeyChar();
	
	public boolean createReference(String ref, int[] mouse, int[] keys);
	public boolean referenceExists(String ref);
	public boolean deleteReference(String ref);
	
	public boolean onClick(String ref);
	public boolean onClickAll(String ref);
	public boolean onPress(String ref);
	public boolean onPressAll(String ref);
	public boolean onRelease(String ref);
	public boolean onReleaseAll(String ref);
}
