/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.network;

/**
 *
 * @author Arthur
 */
public abstract class Packet {


	// public final long tick;
	// public final short id;
	
	/**
	 * Returns the size of the packet minus the header.
	 * @return 
	 */
	public abstract int getSize();
}
