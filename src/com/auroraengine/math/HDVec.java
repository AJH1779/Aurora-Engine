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
 * <code>HDVec</code>s are modifiable objects that denote a translation in
 * Cartesian space using double precision. It is important to duplicate the
 * object whenever making modifications that are not to be reflected in the
 * original.
 *
 * @author LittleRover
 */
public final class HDVec implements Cloneable {

	private static final double EPSILON = 1E-7f;
	private static final Logger LOG = AuroraLogs.getLogger(HDVec.class.getName());

	/**
	 * Returns the average of the vectors, that is the sum of all vectors divided
	 * by the number of vectors provided.
	 *
	 * @param vecs The vectors to average.
	 *
	 * @return The average vector.
	 */
	public static HDVec getAverage(HDVec... vecs) {
		HDVec avg = new HDVec();
		for (HDVec vec : vecs) {
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
	public static double[] getClosest(HDVec A, HDVec dA, HDVec B, HDVec dB) {
		HDVec C = A.clone().negTranslate(B);
		double da2 = dA.getSqrLen();
		double db2 = dB.getSqrLen();
		double dadb = dA.dot(dB);
		double cda = C.dot(dA);
		double cdb = C.dot(dB);

		double eps = EPSILON * Math.min(da2, db2);

		double a = -(db2 * cda + dadb * dadb) / (da2 * db2 + dadb * cdb);
		double b;

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

		return new double[]{a, b};
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
	public static HDVec[] getClosestVecs(HDVec A, HDVec dA, HDVec B, HDVec dB) {
		double[] fa = getClosest(A, dA, B, dB);
		return new HDVec[]{dA.clone().scale(fa[0]).translate(A),
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
	public static double getDist(HDVec A, HDVec B) {
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
	public static HDVec getDistVec(HDVec A, HDVec B) {
		return new HDVec(B).negTranslate(A);
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
	public static boolean getHasNearest(HDVec A, HDVec dA, HDVec B, HDVec dB) {
		double[] fa = getClosest(A, dA, B, dB);
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
	public static boolean getIntersecting(HDVec A, HDVec dA, HDVec B, HDVec dB) {
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
	public static boolean getIntersecting(HDVec A, HDVec dA, HDVec B, HDVec dB,
																				double eps) {
		HDVec[] va = getClosestVecs(A, dA, B, dB);
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
	public static double getSqrDist(HDVec A, HDVec B) {
		return getDistVec(A, B).getSqrLen();
	}

	// The Local Method classes
	/**
	 * Creates a new vector of zero length.
	 */
	public HDVec() {
		zero();
	}

	/**
	 * Creates a new vector with the provided X and Y components and zero for the
	 * Z component.
	 *
	 * @param x The X component
	 * @param y The Y component
	 */
	public HDVec(double x, double y) {
		set(x, y, 0.0f, 1.0f);
	}

	/**
	 * Creates a new vector with the provided X, Y, and Z components
	 *
	 * @param x The X component
	 * @param y The Y component
	 * @param z The Z component
	 */
	public HDVec(double x, double y, double z) {
		set(x, y, z, 1.0f);
	}

	public HDVec(double x, double y, double z, double w) {
		set(x, y, z, w);
	}

	/**
	 * Creates a new vector which has the same X, Y, and Z components as the
	 * provided vector.
	 *
	 * @param vec The vector to copy.
	 */
	public HDVec(HDVec vec) {
		set(vec);
	}
	final double[] data = new double[4];

	/**
	 * Returns the W component of this vector.
	 *
	 * @return The W component
	 */
	public double W() {
		return data[3];
	}

	/**
	 * Sets the W component to the provided value, then returns this vector.
	 *
	 * @param w The new W component
	 *
	 * @return This
	 */
	public HDVec W(double w) {
		data[3] = w;
		return this;
	}

	/**
	 * Returns the X component of this vector.
	 *
	 * @return The X component
	 */
	public double X() {
		return data[0];
	}

	/**
	 * Sets the X component to the provided value, then returns this vector.
	 *
	 * @param x The new X component
	 *
	 * @return This
	 */
	public HDVec X(double x) {
		data[0] = x;
		return this;
	}

	/**
	 * Returns the Y component of this vector.
	 *
	 * @return The Y component
	 */
	public double Y() {
		return data[1];
	}

	/**
	 * Sets the Y component to the provided value, then returns this vector.
	 *
	 * @param y The new Y component
	 *
	 * @return This
	 */
	public HDVec Y(double y) {
		data[1] = y;
		return this;
	}

	/**
	 * Returns the Z component of this vector.
	 *
	 * @return The Z component
	 */
	public double Z() {
		return data[2];
	}

	/**
	 * Sets the Z component to the provided value, then returns this vector.
	 *
	 * @param z The new Z component
	 *
	 * @return This
	 */
	public HDVec Z(double z) {
		data[2] = z;
		return this;
	}

	/**
	 * Returns an array containing the data of this vector.
	 *
	 * @return Data in a length 4 double array.
	 */
	public double[] array() {
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
	public double[] array(double[] fa, int offset) {
		System.arraycopy(data, 0, fa, offset, data.length);
		return fa;
	}

	/**
	 * Creates a copy of this, equivalent to calling toHD()
	 *
	 * @return
	 */
	@Override
	public HDVec clone() {
		// TODO: Clone contract broken.
		return new HDVec(this);
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
	public HDVec cross(HDVec v) {
		return new HDVec(data[1] * v.data[2] - data[2] * v.data[1],
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
	public double dot(HDVec v) {
		return data[0] * v.data[0] + data[1] * v.data[1] + data[2] * v.data[2];
	}

	/**
	 * Returns true if the provided object is a <code>HDVec</code> with the same
	 * components as this. Use a comparison of square separation with some
	 * tolerance to determine when two vectors are similar
	 *
	 * @param obj The object to check
	 *
	 * @return True if the provided object is the same as this.
	 */
	@Override
	public boolean equals(Object obj) {
		return obj instanceof HDVec && Arrays.equals(data, ((HDVec) obj).data);
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
	public double getSqrLen() {
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
	public HDVec invscale(double s) {
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
	public HDVec negTranslate(HDVec v) {
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
	public HDVec negate() {
		return scale(-1.0f);
	}

	/**
	 * Normalises this vector (sets the length to 1), then returns this.
	 *
	 * @return This
	 */
	public HDVec normalise() {
		return invscale(getLen());
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
	public HDVec reflect(HDVec n) {
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
	public HDVec remainder(HDVec n) {
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
	public HDVec scale(double s) {
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
	public HDVec set(double x, double y, double z) {
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
	public HDVec set(double x, double y, double z, double w) {
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
	public HDVec set(HDVec v) {
		data[0] = v.data[0];
		data[1] = v.data[1];
		data[2] = v.data[2];
		data[3] = v.data[3];
		return this;
	}

	public HDVec setXYZ(HDVec v) {
		data[0] = v.data[0];
		data[1] = v.data[1];
		data[2] = v.data[2];
		return this;
	}

	/**
	 * Returns a copy of this vector.
	 *
	 * @return A copy of This
	 */
	public HDVec toHD() {
		return new HDVec(this);
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
	 * Returns a copy of this vector as a <code>LDVec</code>.
	 *
	 * @return A copy of This
	 */
	public LDVec toLD() {
		return new LDVec((float) data[0], (float) data[1],
										 (float) data[2], (float) data[3]);
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
	public HDVec translate(double x, double y, double z) {
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
	public HDVec translate(HDVec v) {
		data[0] += v.data[0];
		data[1] += v.data[1];
		data[2] += v.data[2];
		return this;
	}

	/**
	 * Places the X, Y, and Z components into the provided buffer in that order,
	 * then returns the provided buffer.
	 *
	 * @param bb The Buffer
	 *
	 * @return The Provided Buffer
	 */
	public ByteBuffer writeXYZ(ByteBuffer bb) {
		bb.putDouble(data[0]);
		bb.putDouble(data[1]);
		bb.putDouble(data[2]);
		return bb;
	}

	/**
	 * Places the X, Y, Z, and W components into the provided buffer in that
	 * order, then returns the provided buffer.
	 *
	 * @param bb The Buffer
	 *
	 * @return The Provided Buffer
	 */
	public ByteBuffer writeXYZW(ByteBuffer bb) {
		bb.putDouble(data[0]);
		bb.putDouble(data[1]);
		bb.putDouble(data[2]);
		bb.putDouble(data[3]);
		return bb;
	}

	/**
	 * Sets this to an unscaled zero vector.
	 *
	 * @return This
	 */
	public HDVec zero() {
		return set(0.0f, 0.0f, 0.0f, 1.0f);
	}
}
