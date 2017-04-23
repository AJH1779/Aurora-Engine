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
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * <code>LDVec</code>s are modifiable objects that denote a translation in
 * Cartesian space using float precision. It is important to duplicate the
 * object whenever making modifications that are not to be reflected in the
 * original.
 *
 * @author LittleRover
 */
public final class LDVec implements Cloneable {

	private static final float EPSILON = 1E-7f;
	private static final Logger LOG = AuroraLogs.getLogger(LDVec.class.getName());

	/**
	 * Returns the average of the vectors, that is the sum of all vectors divided
	 * by the number of vectors provided.
	 *
	 * @param vecs The vectors to average.
	 *
	 * @return The average vector.
	 */
	public static LDVec getAverage(LDVec... vecs) {
		LDVec avg = new LDVec();
		for (LDVec vec : vecs) {
			avg.translate(vec);
		}
		return avg.invscale(vecs.length);
	}

	/**
	 * Returns the positions of the points on the defined lines which are closest
	 * together by multiples of the direction vector away from the position
	 * vector.
	 *
	 * @param A  Line A position vector.
	 * @param dA Line A direction vector.
	 * @param B  Line B position vector.
	 * @param dB Line B direction vector.
	 *
	 * @return
	 */
	public static float[] getClosest(LDVec A, LDVec dA, LDVec B, LDVec dB) {
		LDVec C = A.clone().negTranslate(B);
		float da2 = dA.getSqrLen();
		float db2 = dB.getSqrLen();
		float dadb = dA.dot(dB);
		float cda = C.dot(dA);
		float cdb = C.dot(dB);

		float eps = EPSILON * Math.min(da2, db2);

		float a = -(db2 * cda + dadb * dadb) / (da2 * db2 + dadb * cdb);
		float b;

		if (a > 1f) {
			a = 1f;
			b = (cdb - dadb) / db2;
		} else if (a < 0f) {
			a = 0f;
			b = cdb / db2;
		} else {
			b = (da2 * cdb + dadb * dadb) / (da2 * db2 + dadb * cda);
			if (b > 1f) {
				b = 1f;
				a = (dadb - cda) / da2;
			} else if (b < 0f) {
				b = 0f;
				a = -cda / da2;
			}
		}

		return new float[]{a, b};
	}

	/**
	 * Returns the position vectors of the points on the defined lines which are
	 * closest together.
	 *
	 * @param A  Line A position vector.
	 * @param dA Line A direction vector.
	 * @param B  Line B position vector.
	 * @param dB Line B direction vector.
	 *
	 * @return Closest points on Line A and Line B to each other.
	 */
	public static LDVec[] getClosestVecs(LDVec A, LDVec dA, LDVec B, LDVec dB) {
		float[] fa = getClosest(A, dA, B, dB);
		return new LDVec[]{dA.clone().scale(fa[0]).translate(A),
											 dB.clone().scale(fa[1]).translate(B)};
	}

	/**
	 * Returns the distance between A and B. This is equivalent to getting the
	 * length of the vector produced from <code>getDistVec()</code>.
	 *
	 * @param A The starting point
	 * @param B The destination point
	 *
	 * @return The separation
	 */
	public static double getDist(LDVec A, LDVec B) {
		return Math.sqrt(getSqrDist(A, B));
	}

	/**
	 * Returns the vector which denotes B - A, which is the translation vector for
	 * moving from the point A to point B.
	 *
	 * @param A The starting point
	 * @param B The destination point
	 *
	 * @return The translation vector
	 */
	public static LDVec getDistVec(LDVec A, LDVec B) {
		return new LDVec(B).negTranslate(A);
	}

	/**
	 * Returns true if the closest point of the two lines defined by the provided
	 * vectors lies within the defined segments spanned by the direction vector.
	 *
	 * @param A  Line A position vector.
	 * @param dA Line A direction vector.
	 * @param B  Line B position vector.
	 * @param dB Line B direction vector.
	 *
	 * @return
	 */
	public static boolean getHasNearest(LDVec A, LDVec dA, LDVec B, LDVec dB) {
		float[] fa = getClosest(A, dA, B, dB);
		return fa[0] > 0f && fa[0] < 1f && fa[1] > 0f && fa[1] < 1f;
	}

	/**
	 * Returns true if the two defined lines may be considered to intersect.
	 *
	 * @param A  Line A position vector.
	 * @param dA Line A direction vector.
	 * @param B  Line B position vector.
	 * @param dB Line B direction vector.
	 *
	 * @return
	 */
	public static boolean getIntersecting(LDVec A, LDVec dA, LDVec B, LDVec dB) {
		return getIntersecting(A, dA, B, dB,
													 EPSILON * Math.max(dA.getSqrLen(), dB.getSqrLen()));
	}

	/**
	 * Returns true if the two defined lines are within the tolerance distance of
	 * each other at their closest approach.
	 *
	 * @param A   Line A position vector.
	 * @param dA  Line A direction vector.
	 * @param B   Line B position vector.
	 * @param dB  Line B direction vector.
	 * @param eps The distance to be considered intersecting.
	 *
	 * @return
	 */
	public static boolean getIntersecting(LDVec A, LDVec dA, LDVec B, LDVec dB,
																				float eps) {
		LDVec[] va = getClosestVecs(A, dA, B, dB);
		return va[0].negTranslate(va[1]).getSqrLen() < eps * eps;
	}

	/**
	 * Returns the square distance between A and B. This is equivalent to getting
	 * the square length of the vector produced from <code>getDistVec()</code>.
	 *
	 * @param A The starting point
	 * @param B The destination point
	 *
	 * @return The separation squared
	 */
	public static float getSqrDist(LDVec A, LDVec B) {
		return getDistVec(A, B).getSqrLen();
	}

	// The Local Method classes
	/**
	 * Creates a new vector of zero length.
	 */
	public LDVec() {
		zero();
	}

	/**
	 * Creates a new vector with the provided X and Y components and zero for the
	 * Z component.
	 *
	 * @param x The X component
	 * @param y The Y component
	 */
	public LDVec(float x, float y) {
		set(x, y, 0.0f, 1.0f);
	}

	/**
	 * Creates a new vector with the provided X, Y, and Z components
	 *
	 * @param x The X component
	 * @param y The Y component
	 * @param z The Z component
	 */
	public LDVec(float x, float y, float z) {
		set(x, y, z, 1.0f);
	}

	public LDVec(float x, float y, float z, float w) {
		set(x, y, z, w);
	}

	/**
	 * Creates a new vector which has the same X, Y, and Z components as the
	 * provided vector.
	 *
	 * @param vec The vector to copy.
	 */
	public LDVec(LDVec vec) {
		set(vec);
	}
	final float[] data = new float[4];

	/**
	 * Returns the W component of this vector.
	 *
	 * @return The W component
	 */
	public float W() {
		return data[3];
	}

	/**
	 * Sets the W component to the provided value, then returns this vector.
	 *
	 * @param w The new W component
	 *
	 * @return This
	 */
	public LDVec W(float w) {
		data[3] = w;
		return this;
	}

	/**
	 * Returns the X component of this vector.
	 *
	 * @return The X component
	 */
	public float X() {
		return data[0];
	}

	/**
	 * Sets the X component to the provided value, then returns this vector.
	 *
	 * @param x The new X component
	 *
	 * @return This
	 */
	public LDVec X(float x) {
		data[0] = x;
		return this;
	}

	/**
	 * Returns the Y component of this vector.
	 *
	 * @return The Y component
	 */
	public float Y() {
		return data[1];
	}

	/**
	 * Sets the Y component to the provided value, then returns this vector.
	 *
	 * @param y The new Y component
	 *
	 * @return This
	 */
	public LDVec Y(float y) {
		data[1] = y;
		return this;
	}

	/**
	 * Returns the Z component of this vector.
	 *
	 * @return The Z component
	 */
	public float Z() {
		return data[2];
	}

	/**
	 * Sets the Z component to the provided value, then returns this vector.
	 *
	 * @param z The new Z component
	 *
	 * @return This
	 */
	public LDVec Z(float z) {
		data[2] = z;
		return this;
	}

	/**
	 * Returns an array containing the data of this vector.
	 *
	 * @return Data in a length 4 float array.
	 */
	public float[] array() {
		return Arrays.copyOf(data, data.length);
	}

	/**
	 * Puts the data into the provided array at the specified offset, returning
	 * the provided array.
	 *
	 * @param fa     The array to put the data into
	 * @param offset The index to begin entering data.
	 *
	 * @return The provided array with the data.
	 */
	public float[] array(float[] fa, int offset) {
		System.arraycopy(data, 0, fa, offset, data.length);
		return fa;
	}

	/**
	 * Creates a copy of this, equivalent to calling toLD()
	 *
	 * @return
	 */
	@Override
	public LDVec clone() {
		// TODO: Clone contract broken.
		return new LDVec(this);
	}

	/**
	 * Returns the cross product of this vector and the specified vector as a new
	 * vector. The order of arguments is as written, so <code>A.cross(B)</code> is
	 * equivalent to A × B.
	 *
	 * @param v The second vector
	 *
	 * @return The cross product vector
	 */
	public LDVec cross(LDVec v) {
		return new LDVec(data[1] * v.data[2] - data[2] * v.data[1],
										 data[2] * v.data[0] - data[0] * v.data[2],
										 data[0] * v.data[1] - data[1] * v.data[0],
										 data[3] * v.data[3]);
	}

	/**
	 * Returns the dot product of this vector and the specified vector.
	 *
	 * @param v The second vector
	 *
	 * @return The dot product
	 */
	public float dot(LDVec v) {
		return data[0] * v.data[0] + data[1] * v.data[1] + data[2] * v.data[2];
	}

	/**
	 * Returns true if the provided object is a <code>LDVec</code> with the same
	 * components as this. Use a comparison of square separation with some
	 * tolerance to determine when two vectors are similar
	 *
	 * @param obj The object to check
	 *
	 * @return True if the provided object is the same as this.
	 */
	@Override
	public boolean equals(Object obj) {
		return obj instanceof LDVec && Arrays.equals(data, ((LDVec) obj).data);
	}

	/**
	 * Returns the length of this vector. If used for comparison, try using the
	 * faster <code>sqrLength()</code> method.
	 *
	 * @return The length
	 */
	public double getLen() {
		return Math.sqrt(dot(this));
	}

	/**
	 * Returns the square length of this vector. This is faster than finding the
	 * length.
	 *
	 * @return The square length
	 */
	public float getSqrLen() {
		return dot(this);
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 29 * hash + Arrays.hashCode(this.data);
		return hash;
	}

	/**
	 * Scales all components in this vector by the inverse of the specified scale
	 * factor, then returns this.
	 *
	 * @param s The inverse of the scale factor
	 *
	 * @return This
	 */
	public LDVec invscale(float s) {
		return scale(1.0f / s);
	}

	/**
	 * Subtracts the provided vector to this vector, then returns this. This has
	 * the effect of translating this by the negative of the specified vector.
	 *
	 * @param v The translation vector
	 *
	 * @return This
	 */
	public LDVec negTranslate(LDVec v) {
		data[0] -= v.data[0] * data[3];
		data[1] -= v.data[1] * data[3];
		data[2] -= v.data[2] * data[3];
		data[3] *= v.data[3];
		return this;
	}

	/**
	 * Scales this vector by -1, then returns this.
	 *
	 * @return This
	 */
	public LDVec negate() {
		return scale(-1.0f);
	}

	/**
	 * Normalises this vector (sets the length to 1), then returns this.
	 *
	 * @return This
	 */
	public LDVec normalise() {
		return invscale((float) getLen());
	}

	/**
	 * Reflects this vector through the plane defined by the provided normal
	 * vector, then returns this. If the provided vector is not normalised, the
	 * result will be scaled by the same amount as the provided vector length.
	 *
	 * @param n The plane normal vector.
	 *
	 * @return This
	 */
	public LDVec reflect(LDVec n) {
		return set(
						(1.0F - 2.0F * n.data[0] * n.data[0]) * data[0] - 2.0F * n.data[0] *
																															n.data[1] *
																															data[1] - 2.0F *
																																				n.data[0] *
																																				n.data[2] *
																																				data[2],
						-2.0F * n.data[0] * n.data[1] * data[0] + (1.0F - 2.0F * n.data[1] *
																															n.data[1]) *
																											data[1] -
						2.0F * n.data[1] * n.data[2] * data[2],
						-2.0F * n.data[0] * n.data[2] * data[0] - 2.0F * n.data[1] *
																											n.data[2] * data[1] +
						(1.0F - 2.0F * n.data[2] * n.data[2]) * data[2]);
	}

	/**
	 * Sets each component to the remainder of the division by the corresponding
	 * component in the provided vector, then returns this.
	 *
	 * @param n The wrapping vector
	 *
	 * @return This
	 */
	public LDVec remainder(LDVec n) {
		return set(data[0] % n.data[0], data[1] % n.data[1], data[2] % n.data[2]);
	}

	/**
	 * Scales all components in this vector by the specified scale factor, then
	 * returns this.
	 *
	 * @param s The scale factor
	 *
	 * @return This
	 */
	public LDVec scale(float s) {
		data[0] *= s;
		data[1] *= s;
		data[2] *= s;
		return this;
	}

	/**
	 * Sets the X, Y, and Z components to the provided values and the W component
	 * to 1, then returns this vector.
	 *
	 * @param x The new X component
	 * @param y The new Y component
	 * @param z The new Z component
	 *
	 * @return This
	 */
	public LDVec set(float x, float y, float z) {
		data[0] = x;
		data[1] = y;
		data[2] = z;
		return this;
	}

	/**
	 * Sets the X, Y, Z, and W components to the provided values, then returns
	 * this vector.
	 *
	 * @param x The new X component
	 * @param y The new Y component
	 * @param z The new Z component
	 * @param w The new W component
	 *
	 * @return This
	 */
	public LDVec set(float x, float y, float z, float w) {
		data[0] = x;
		data[1] = y;
		data[2] = z;
		data[3] = w;
		return this;
	}

	/**
	 * Sets the X, Y, and Z components to those of the provided vector, then
	 * returns this vector.
	 *
	 * @param v The vector to copy
	 *
	 * @return This
	 */
	public LDVec set(LDVec v) {
		data[0] = v.data[0];
		data[1] = v.data[1];
		data[2] = v.data[2];
		data[3] = v.data[3];
		return this;
	}

	public LDVec setXYZ(LDVec v) {
		data[0] = v.data[0];
		data[1] = v.data[1];
		data[2] = v.data[2];
		return this;
	}

	/**
	 * Returns a copy of this vector as a <code>HDVec</code>.
	 *
	 * @return A copy of This
	 */
	public HDVec toHD() {
		return new HDVec(data[0], data[1], data[2]);
	}

	/**
	 * Returns a copy of this vector as a <code>HDQVec</code>.
	 *
	 * @return
	 */
	public HDQVec toHDQ() {
		return new HDQVec(data[0], data[1], data[2]);
	}

	/**
	 * Returns a copy of this vector.
	 *
	 * @return A copy of This
	 */
	public LDVec toLD() {
		return new LDVec(this);
	}

	/**
	 * Returns this vector as a string in the format "(x,y,z)", where x, y, and z
	 * are the corresponding components of this vector.
	 *
	 * @return A string representation of this vector.
	 */
	@Override
	public String toString() {
		return "(" + data[0] + "," + data[1] + "," + data[2] + ")";
	}

	/**
	 * Adds the provided X, Y, and Z values to the corresponding components in
	 * this vector, then returns this. This has the effect of translating this by
	 * the specified amount in each component.
	 *
	 * @param x The X component to add
	 * @param y The Y component to add
	 * @param z The Z component to add
	 *
	 * @return This
	 */
	public LDVec translate(float x, float y, float z) {
		data[0] += x;
		data[1] += y;
		data[2] += z;
		return this;
	}

	/**
	 * Adds the provided vector to this vector, then returns this. This has the
	 * effect of translating this by the specified vector.
	 *
	 * @param v The translation vector
	 *
	 * @return This
	 */
	public LDVec translate(LDVec v) {
		data[0] += v.data[0];
		data[1] += v.data[1];
		data[2] += v.data[2];
		return this;
	}

	public ByteBuffer writeXY(ByteBuffer p_bytebuffer) {
		p_bytebuffer.putFloat(data[0]);
		p_bytebuffer.putFloat(data[1]);
		return p_bytebuffer;
	}

	/**
	 * Places the X, Y, and Z components into the provided buffer in that order,
	 * then returns the provided buffer.
	 *
	 * @param p_bytebuffer The Buffer
	 *
	 * @return The Provided Buffer
	 */
	public ByteBuffer writeXYZ(ByteBuffer p_bytebuffer) {
		p_bytebuffer.putFloat(data[0]);
		p_bytebuffer.putFloat(data[1]);
		p_bytebuffer.putFloat(data[2]);
		return p_bytebuffer;
	}

	/**
	 * Places the X, Y, Z, and W components into the provided buffer in that
	 * order, then returns the provided buffer.
	 *
	 * @param p_bytebuffer The Buffer
	 *
	 * @return The Provided Buffer
	 */
	public ByteBuffer writeXYZW(ByteBuffer p_bytebuffer) {
		p_bytebuffer.putFloat(data[0]);
		p_bytebuffer.putFloat(data[1]);
		p_bytebuffer.putFloat(data[2]);
		p_bytebuffer.putFloat(data[3]);
		return p_bytebuffer;
	}

	/**
	 * Sets this to an unscaled zero vector.
	 *
	 * @return This
	 */
	public LDVec zero() {
		return set(0.0f, 0.0f, 0.0f, 1.0f);
	}
}
