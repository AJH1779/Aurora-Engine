/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.world;

import java.util.logging.Logger;

/**
 *
 * @author LittleRover
 */
public final class LocalPosition {
	private static final Logger LOG = Logger.getLogger(LocalPosition.class
					.getName());

	/**
	 * Creates a new local position using the specified position as the basis.
	 *
	 * @param pos The position to copy.
	 */
	public LocalPosition(LocalPosition pos) {
		set(pos);
	}

	/**
	 * Sets this position to the specified position.
	 *
	 * @param p_pos The position to set to.
	 */
	public void set(LocalPosition p_pos) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
