/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.world;

/**
 *
 * @author ajh17
 * @param <Region>
 * @param <Ref>
 */
public interface Transition<Region, Ref> {
	
	public Region getDestination();
	
	/**
	 * Should return the modifiable version of the transform, i.e. return ref
	 * @return 
	 */
	public Ref getTransform();
	
	public Ref transform(Ref ref);
}
