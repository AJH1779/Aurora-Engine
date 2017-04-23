/*
 * Copyright (C) 2017 LittleRover
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.auroraengine.entity;

import com.auroraengine.debug.AuroraLogs;
import com.auroraengine.world.LocalPosition;
import java.util.logging.Logger;

/**
 * Denotes an object within local position space that may or may not be
 * renderable. At the very least it belongs to a reference frame of some kind.
 *
 * @author LittleRover
 */
public abstract class Entity {
	private static final Logger LOG = AuroraLogs.getLogger(Entity.class.getName());

	/**
	 * Creates a new entity located at the specified position.
	 *
	 * @param pos The position of the entity.
	 */
	public Entity(LocalPosition pos) {
		this.pos = new LocalPosition(pos);
		this.prev_pos = new LocalPosition(pos);
	}
	private final LocalPosition pos;
	private final LocalPosition prev_pos;

	/**
	 * Should be called at the end or start of every frame to ensure the positions
	 * are properly synchronised.
	 *
	 * TODO: Relevant?
	 */
	public void finishUpdating() {
		this.prev_pos.set(pos);
	}

	/**
	 * Returns an estimate for the position of this entity after the specified
	 * time has expired.
	 *
	 * TODO: Is relevant?
	 *
	 * @param delt The progressed time.
	 *
	 * @return The estimated position after the specified time.
	 */
	public LocalPosition getEstimatedPosition(double delt) {
		LocalPosition est = new LocalPosition(prev_pos);

		return est;
	}

	/**
	 * Returns the position of this entity at the last frame.
	 *
	 * TODO: Is relevant?
	 *
	 * TODO: Costly as creates new object.
	 *
	 * @return
	 */
	public LocalPosition getPreviousPosition() {
		return new LocalPosition(prev_pos);
	}

	/**
	 * Called every frame, allowing for this to be updated internally.
	 *
	 * @param delt The change in time.
	 */
	public abstract void update(double delt);

}
