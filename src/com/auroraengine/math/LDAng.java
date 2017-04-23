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

/**
 * <code>LDAng</code>s are modifiable objects that denote a rotation in
 * Cartesian space using float precision. It is important to duplicate the
 * object whenever making modifications that are not to be reflected in the
 * original.
 *
 * @author LittleRover
 */
public final class LDAng {
	/**
	 * Creates a new zero rotation object.
	 */
	public LDAng() {
	}

	/**
	 * Creates a new rotation object on the clockwise on the XY plane.
	 *
	 * @param ang The rotation angle
	 */
	public LDAng(float ang) {
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
	public LDAng(float ang, float x, float y, float z) {
		set(ang, x, y, z);
	}

	/**
	 * Creates a new rotation object around the specified vector by the provided
	 * angle, which should be normalised.
	 *
	 * @param vec The vector
	 * @param ang The rotation angle in radians
	 */
	public LDAng(float ang, LDVec vec) {
		set(vec);
		set(ang);
	}

	/**
	 * Creates a new rotation object which is a copy of the provided object.
	 *
	 * @param ang The rotation object.
	 */
	public LDAng(LDAng ang) {
		set(ang);
	}
	final float[] dat = new float[3];
	final LDVec vec = new LDVec(0.0f, 0.0f, 1.0f, 0.0f);

	/**
	 * Returns the X component of the rotation vector.
	 *
	 * @return The X component
	 */
	public float X() {
		return vec.X();
	}

	/**
	 * Returns the Y component of the rotation vector.
	 *
	 * @return The Y component
	 */
	public float Y() {
		return vec.Y();
	}

	/**
	 * Returns the Z component of the rotation vector.
	 *
	 * @return The Z component
	 */
	public float Z() {
		return vec.Z();
	}

	/**
	 * Returns the rotation angle.
	 *
	 * @return
	 */
	public float ang() {
		return dat[0];
	}

	/**
	 * Returns the cosine of the rotation angle.
	 *
	 * @return
	 */
	public float cos() {
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
	public LDAng set(float x, float y, float z) {
		this.vec.set(x, y, z).normalise();
		return this;
	}

	/**
	 * Sets the X, Y, and Z components of the rotation vector to those of the
	 * provided vector and the W component to 0, then returns this.
	 *
	 * @param vec The vector to copy.
	 *
	 * @return This
	 */
	public LDAng set(LDVec vec) {
		this.vec.setXYZ(vec).normalise();
		return this;
	}

	/**
	 * Sets the rotation angle to the specified amount, then returns this.
	 *
	 * @param ang The new rotation angle
	 *
	 * @return This
	 */
	public LDAng set(float ang) {
		dat[0] = ang;
		dat[1] = (float) Math.sin(ang);
		dat[2] = (float) Math.cos(ang);
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
	public LDAng set(float ang, float x, float y, float z) {
		set(x, y, z);
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
	public LDAng set(LDAng ang) {
		dat[0] = ang.dat[0];
		dat[1] = ang.dat[1];
		dat[2] = ang.dat[2];
		this.vec.setXYZ(ang.vec);
		return this;
	}

	/**
	 * Returns the sine of the rotation angle.
	 *
	 * @return
	 */
	public float sin() {
		return dat[1];
	}
}
