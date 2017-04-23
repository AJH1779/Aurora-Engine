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
package com.auroraengine.math;

import com.auroraengine.debug.AuroraLogs;
import static com.auroraengine.math.HDQVec.CONTEXT;
import java.math.BigDecimal;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import java.util.logging.Logger;

/**
 * <code>HDAng</code>s are modifiable objects that denote a rotation in
 * Cartesian space using BigDecimal precision. It is important to duplicate the
 * object whenever making modifications that are not to be reflected in the
 * original.
 *
 * WARNING: This does not currently have the required accuracy to be useful.
 *
 * @author LittleRover
 */
public final class HDQAng {
	private static final Logger LOG = AuroraLogs.getLogger(HDQAng.class.getName());

	/**
	 * Creates a new zero rotation object.
	 */
	public HDQAng() {
	}

	/**
	 * Creates a new rotation object on the clockwise on the XY plane.
	 *
	 * @param ang The rotation angle
	 */
	public HDQAng(BigDecimal ang) {
		set(ang);
	}

	/**
	 * Creates a new rotation object around the specified vector by the provided
	 * angle, which should be normalised.
	 *
	 * @param x   The x vector component
	 * @param y   The y vector component
	 * @param z   The z vector component
	 * @param ang The rotation angle in radians
	 */
	public HDQAng(BigDecimal ang, BigDecimal x, BigDecimal y, BigDecimal z) {
		set(ang, x, y, z);
	}

	/**
	 * Creates a new rotation object around the specified vector by the provided
	 * angle, which should be normalised.
	 *
	 * @param vec The vector
	 * @param ang The rotation angle in radians
	 */
	public HDQAng(BigDecimal ang, HDQVec vec) {
		set(ang, vec);
	}

	/**
	 * Creates a new rotation object which is a copy of the provided object.
	 *
	 * @param ang The rotation object.
	 */
	public HDQAng(HDQAng ang) {
		set(ang);
	}
	private final BigDecimal[] dat = new BigDecimal[3];
	private final HDQVec vec = new HDQVec(ZERO, ZERO, ONE);

	/**
	 * Returns the X component of the rotation vector.
	 *
	 * @return The X component
	 */
	public BigDecimal X() {
		return vec.X();
	}

	/**
	 * Returns the Y component of the rotation vector.
	 *
	 * @return The Y component
	 */
	public BigDecimal Y() {
		return vec.Y();
	}

	/**
	 * Returns the Z component of the rotation vector.
	 *
	 * @return The Z component
	 */
	public BigDecimal Z() {
		return vec.Z();
	}

	/**
	 * Returns the rotation angle.
	 *
	 * @return
	 */
	public BigDecimal ang() {
		return dat[0];
	}

	/**
	 * Returns the cosine of the rotation angle.
	 *
	 * @return
	 */
	public BigDecimal cos() {
		return dat[2];
	}

	/**
	 * Sets the X, Y, and Z components of the rotation vector to the provided
	 * values, then returns this.
	 *
	 * @param x The new X component
	 * @param y The new Y component
	 * @param z The new Z component
	 *
	 * @return This
	 */
	public HDQAng set(BigDecimal x, BigDecimal y, BigDecimal z) {
		this.vec.set(x, y, z);
		return this;
	}

	/**
	 * Sets the X, Y, and Z components of the rotation vector to those of the
	 * provided vector, then returns this.
	 *
	 * @param vec The vector to copy
	 *
	 * @return This
	 */
	public HDQAng set(HDQVec vec) {
		this.vec.set(vec);
		return this;
	}

	/**
	 * Sets the rotation angle to the specified amount, then returns this.
	 *
	 * @param ang The new rotation angle
	 *
	 * @return This
	 */
	public HDQAng set(BigDecimal ang) {
		dat[0] = ang;
		// TODO: Include sin and cos manually using an approximation method.
		dat[1] = new BigDecimal(Math.sin(ang.doubleValue()), CONTEXT);
		dat[2] = new BigDecimal(Math.cos(ang.doubleValue()), CONTEXT);
		return this;
	}

	/**
	 *
	 * Sets the X, Y, and Z components of the rotation vector to the provided
	 * values and the rotation angle to the specified amount, then returns this.
	 *
	 * @param x   The new X component
	 * @param y   The new Y component
	 * @param z   The new Z component
	 * @param ang The new rotation angle
	 *
	 * @return This
	 */
	public HDQAng set(BigDecimal ang, BigDecimal x, BigDecimal y, BigDecimal z) {
		set(x, y, z);
		return set(ang);
	}

	/**
	 * Sets the X, Y, and Z components of the rotation vector to those of the
	 * provided vector and the rotation angle to the specified amount, then
	 * returns this.
	 *
	 * @param ang The new rotation angle
	 * @param vec The vector to copy
	 *
	 * @return This
	 */
	public HDQAng set(BigDecimal ang, HDQVec vec) {
		set(vec);
		return set(ang);
	}

	/**
	 * Sets this object to have the same vector and angle as the provided angle,
	 * then returns this.
	 *
	 * @param ang The angle to copy
	 *
	 * @return This
	 */
	public HDQAng set(HDQAng ang) {
		dat[0] = ang.dat[0];
		dat[1] = ang.dat[1];
		dat[2] = ang.dat[2];
		return set(ang.vec);
	}

	/**
	 * Returns the sine of the rotation angle.
	 *
	 * @return
	 */
	public BigDecimal sin() {
		return dat[1];
	}

	/**
	 * Returns the rotation vector. Modifying this vector modifies the rotation
	 * vector in the angle object.
	 *
	 * @return The rotation vector
	 */
	public HDQVec vec() {
		return vec;
	}

}
