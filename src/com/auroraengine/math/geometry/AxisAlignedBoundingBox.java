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

import com.auroraengine.debug.AuroraLogs;
import com.auroraengine.math.LDVec;
import java.util.logging.Logger;

/**
 *
 * @author LittleRover
 */
public class AxisAlignedBoundingBox extends Volume {
	private static final Logger LOG = AuroraLogs.getLogger(
					AxisAlignedBoundingBox.class.getName());

	public AxisAlignedBoundingBox(LDVec center, float dx, float dy, float dz) {
		super(center);
		this.dx = dx;
		this.dy = dy;
		this.dz = dz;
	}

	public AxisAlignedBoundingBox(LDVec a, LDVec b) {
		super(LDVec.getAverage(a, b));
		dx = Math.abs(a.X() - b.X());
		dy = Math.abs(a.Y() - b.Y());
		dz = Math.abs(a.Z() - b.Z());
	}
	private float dx;
	private float dy;
	private float dz;

	@Override
	public Side getSideOf(Plane p_plane) {
		float distance = p_plane.getDistanceTo(center);
		// TODO: Verify
		float radius = (Math.abs(dx * p_plane.normal.X()) +
										Math.abs(dy * p_plane.normal.Y()) +
										Math.abs(dz * p_plane.normal.Z())) * 0.5f;

		if (distance < -radius) {
			return Side.OUTSIDE;
		} else if (distance > radius) {
			return Side.INSIDE;
		} else {
			return Side.ACROSS;
		}
	}

	@Override
	public float getVolume() {
		return dx * dy * dz;
	}
}
