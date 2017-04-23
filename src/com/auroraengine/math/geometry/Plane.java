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
package com.auroraengine.math.geometry;

import com.auroraengine.math.LDVec;

/**
 *
 * @author LittleRover
 */
public class Plane {
	public Plane(LDVec p_normal) {
		this.normal = p_normal;
	}

	public Plane() {
		this(new LDVec());
	}
	protected final LDVec normal;

	public float getDistanceTo(LDVec p_target) {
		return normal.dot(p_target);
	}

	public Side getSideOf(LDVec p_target) {
		float dot = normal.dot(p_target);
		if (dot < 0.0f) {
			return Side.OUTSIDE;
		} else if (dot > 0.0f) {
			return Side.INSIDE;
		} else {
			return Side.ACROSS;
		}
	}
}
