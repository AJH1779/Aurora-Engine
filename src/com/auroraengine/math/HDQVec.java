/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.math;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import static java.math.BigDecimal.*;
import java.math.MathContext;
import java.math.RoundingMode;

import java.util.Arrays;

/**
 * <code>HDQVec</code>s are modifiable objects that denote a translation in
 * Cartesian space using BigDecimal precision. It is important to duplicate the
 * object whenever making modifications that are not to be reflected in the
 * original.
 *
 * @author Arthur
 */
public final class HDQVec {

	public static final BigDecimal TWO = new BigDecimal(2);
	public static final MathContext CONTEXT = MathContext.DECIMAL128;

	// The Static Method classes
	/**
	 * Returns the square root.
	 *
	 * @param d
	 * @return
	 */
	public static BigDecimal sqrt(double d) {
		return sqrt(new BigDecimal(d, CONTEXT));
	}

	/**
	 * Returns the square root.
	 *
	 * @param b
	 * @return
	 */
	public static BigDecimal sqrt(BigDecimal b) {
		BigDecimal x0 = ZERO;
		BigDecimal x1 = new BigDecimal(Math.sqrt(b.doubleValue()), CONTEXT);
		while (!x0.equals(x1)) {
			x0 = x1;
			x1 = b.divide(x0, CONTEXT);
			x1 = x1.add(x0, CONTEXT);
			x1 = x1.divide(TWO, CONTEXT);
		}
		return x1;
	}

	/**
	 * Returns the average of the vectors, that is the sum of all vectors divided
	 * by the number of vectors provided.
	 *
	 * @param vecs The vectors to average.
	 * @return The average vector.
	 */
	public static HDQVec getAverage(HDQVec... vecs) {
		HDQVec avg = new HDQVec();
		for (HDQVec vec : vecs) {
			avg.translate(vec);
		}
		return avg.invscale(new BigDecimal(vecs.length, CONTEXT));
	}

	/**
	 * Returns the vector which denotes B - A, which is the translation vector for
	 * moving from the point A to point B.
	 *
	 * @param A The starting point
	 * @param B The destination point
	 * @return The translation vector
	 */
	public static HDQVec getDistVec(HDQVec A, HDQVec B) {
		return new HDQVec(B).negTranslate(A);
	}

	/**
	 * Returns the distance between A and B. This is equivalent to getting the
	 * length of the vector produced from <code>getDistVec()</code>.
	 *
	 * @param A The starting point
	 * @param B The destination point
	 * @return The separation
	 */
	public static BigDecimal getDist(HDQVec A, HDQVec B) {
		return sqrt(getSqrDist(A, B));
	}

	/**
	 * Returns the square distance between A and B. This is equivalent to getting
	 * the square length of the vector produced from <code>getDistVec()</code>.
	 *
	 * @param A The starting point
	 * @param B The destination point
	 * @return The separation squared
	 */
	public static BigDecimal getSqrDist(HDQVec A, HDQVec B) {
		return getDistVec(A, B).getSqrLen();
	}

	// The Local Method classes
	/**
	 * Creates a new vector of zero length.
	 */
	public HDQVec() {

	}

	/**
	 * Creates a new vector with the provided X and Y components and zero for the
	 * Z component.
	 *
	 * @param x The X component
	 * @param y The Y component
	 */
	public HDQVec(double x, double y) {
		set(new BigDecimal(x, CONTEXT), new BigDecimal(y, CONTEXT), ZERO);
	}

	/**
	 * Creates a new vector with the provided X and Y components and zero for the
	 * Z component.
	 *
	 * @param x The X component
	 * @param y The Y component
	 */
	public HDQVec(BigDecimal x, BigDecimal y) {
		set(x, y, ZERO);
	}

	/**
	 * Creates a new vector with the provided X, Y, and Z components
	 *
	 * @param x The X component
	 * @param y The Y component
	 * @param z The Z component
	 */
	public HDQVec(double x, double y, double z) {
		set(x, y, z);
	}

	/**
	 * Creates a new vector with the provided X, Y, and Z components
	 *
	 * @param x The X component
	 * @param y The Y component
	 * @param z The Z component
	 */
	public HDQVec(BigDecimal x, BigDecimal y, BigDecimal z) {
		set(x, y, z);
	}

	/**
	 * Creates a new vector which has the same X, Y, and Z components as the
	 * provided vector.
	 *
	 * @param vec The vector to copy.
	 */
	public HDQVec(HDQVec vec) {
		set(vec);
	}
	private final BigDecimal[] data = new BigDecimal[3];

	/**
	 * Returns the X component of this vector.
	 *
	 * @return The X component
	 */
	public BigDecimal X() {
		return data[0];
	}

	/**
	 * Returns the Y component of this vector.
	 *
	 * @return The Y component
	 */
	public BigDecimal Y() {
		return data[1];
	}

	/**
	 * Returns the Z component of this vector.
	 *
	 * @return The Z component
	 */
	public BigDecimal Z() {
		return data[2];
	}

	/**
	 * Returns the array which stores the X, Y, and Z components of this vector.
	 * Modifying this modifies the components in this vector.
	 *
	 * @return
	 */
	public BigDecimal[] array() {
		return data;
	}

	/**
	 * Places the X, Y, and Z components into the provided buffer in that order,
	 * then returns the provided buffer.
	 *
	 * @param bb The Buffer
	 * @return The Provided Buffer
	 */
	public ByteBuffer write(ByteBuffer bb) {
		bb.putDouble(data[0].doubleValue());
		bb.putDouble(data[1].doubleValue());
		bb.putDouble(data[2].doubleValue());
		return bb;
	}

	/**
	 * Sets the X component to the provided value, then returns this vector.
	 *
	 * @param x The new X component
	 * @return This
	 */
	public HDQVec X(double x) {
		return X(new BigDecimal(x, CONTEXT));
	}

	/**
	 * Sets the X component to the provided value, then returns this vector.
	 *
	 * @param x The new X component
	 * @return This
	 */
	public HDQVec X(BigDecimal x) {
		data[0] = x;
		return this;
	}

	/**
	 * Sets the Y component to the provided value, then returns this vector.
	 *
	 * @param y The new Y component
	 * @return This
	 */
	public HDQVec Y(double y) {
		return Y(new BigDecimal(y, CONTEXT));
	}

	/**
	 * Sets the Y component to the provided value, then returns this vector.
	 *
	 * @param y The new Y component
	 * @return This
	 */
	public HDQVec Y(BigDecimal y) {
		data[1] = y;
		return this;
	}

	/**
	 * Sets the Z component to the provided value, then returns this vector.
	 *
	 * @param z The new Z component
	 * @return This
	 */
	public HDQVec Z(double z) {
		return Z(new BigDecimal(z, CONTEXT));
	}

	/**
	 * Sets the Z component to the provided value, then returns this vector.
	 *
	 * @param z The new Z component
	 * @return This
	 */
	public HDQVec Z(BigDecimal z) {
		data[2] = z;
		return this;
	}

	/**
	 * Sets the X, Y, and Z components to the provided values, then returns this
	 * vector.
	 *
	 * @param x The new X component
	 * @param y The new Y component
	 * @param z The new Z component
	 * @return This
	 */
	public HDQVec set(double x, double y, double z) {
		return set(new BigDecimal(x, CONTEXT), new BigDecimal(y, CONTEXT), new BigDecimal(z, CONTEXT));
	}

	/**
	 * Sets the X, Y, and Z components to the provided values, then returns this
	 * vector.
	 *
	 * @param x The new X component
	 * @param y The new Y component
	 * @param z The new Z component
	 * @return This
	 */
	public HDQVec set(BigDecimal x, BigDecimal y, BigDecimal z) {
		data[0] = x;
		data[1] = y;
		data[2] = z;
		return this;
	}

	/**
	 * Sets the X, Y, and Z components to those of the provided vector, then
	 * returns this vector.
	 *
	 * @param v The vector to copy
	 * @return This
	 */
	public HDQVec set(HDQVec v) {
		data[0] = v.data[0];
		data[1] = v.data[1];
		data[2] = v.data[2];
		return this;
	}

	/**
	 * Returns the length of this vector. If used for comparison, try using the
	 * faster <code>sqrLength()</code> method.
	 *
	 * @return The length
	 */
	public BigDecimal getLen() {
		return sqrt(dot(this));
	}

	/**
	 * Returns the square length of this vector. This is faster than finding the
	 * length.
	 *
	 * @return The square length
	 */
	public BigDecimal getSqrLen() {
		return dot(this);
	}

	/**
	 * Adds the provided X, Y, and Z values to the corresponding components in
	 * this vector, then returns this. This has the effect of translating this by
	 * the specified amount in each component.
	 *
	 * @param x The X component to add
	 * @param y The Y component to add
	 * @param z The Z component to add
	 * @return This
	 */
	public HDQVec translate(BigDecimal x, BigDecimal y, BigDecimal z) {
		data[0] = data[0].add(x, CONTEXT);
		data[1] = data[1].add(y, CONTEXT);
		data[2] = data[2].add(z, CONTEXT);
		return this;
	}

	/**
	 * Adds the provided vector to this vector, then returns this. This has the
	 * effect of translating this by the specified vector.
	 *
	 * @param v The translation vector
	 * @return This
	 */
	public HDQVec translate(HDQVec v) {
		data[0] = data[0].add(v.data[0], CONTEXT);
		data[1] = data[1].add(v.data[1], CONTEXT);
		data[2] = data[2].add(v.data[2], CONTEXT);
		return this;
	}

	/**
	 * Subtracts the provided vector to this vector, then returns this. This has
	 * the effect of translating this by the negative of the specified vector.
	 *
	 * @param v The translation vector
	 * @return This
	 */
	public HDQVec negTranslate(HDQVec v) {
		data[0] = data[0].subtract(v.data[0], CONTEXT);
		data[1] = data[1].subtract(v.data[1], CONTEXT);
		data[2] = data[2].subtract(v.data[2], CONTEXT);
		return this;
	}

	/**
	 * Scales all components in this vector by the specified scale factor, then
	 * returns this.
	 *
	 * @param s The scale factor
	 * @return This
	 */
	public HDQVec scale(BigDecimal s) {
		data[0] = data[0].multiply(s, CONTEXT);
		data[1] = data[1].multiply(s, CONTEXT);
		data[2] = data[2].multiply(s, CONTEXT);
		return this;
	}

	/**
	 * Scales all components in this vector by the inverse of the specified scale
	 * factor, then returns this.
	 *
	 * @param s The inverse of the scale factor
	 * @return This
	 */
	public HDQVec invscale(BigDecimal s) {
		return scale(ONE.divide(s, CONTEXT));
	}

	/**
	 * Returns the dot product of this vector and the specified vector.
	 *
	 * @param v The second vector
	 * @return The dot product
	 */
	public BigDecimal dot(HDQVec v) {
		return data[0].multiply(data[0], CONTEXT).add(data[1].multiply(data[1], CONTEXT), CONTEXT).add(data[2].multiply(data[2], CONTEXT), CONTEXT);
	}

	/**
	 * Returns the cross product of this vector and the specified vector as a new
	 * vector. The order of arguments is as written, so <code>A.cross(B)</code> is
	 * equivalent to A × B.
	 *
	 * @param v The second vector
	 * @return The cross product vector
	 */
	public HDQVec cross(HDQVec v) {
		return new HDQVec(
						data[1].multiply(v.data[2], CONTEXT).subtract(data[2].multiply(v.data[1], CONTEXT), CONTEXT),
						data[2].multiply(v.data[0], CONTEXT).subtract(data[0].multiply(v.data[2], CONTEXT), CONTEXT),
						data[0].multiply(v.data[1], CONTEXT).subtract(data[1].multiply(v.data[0], CONTEXT), CONTEXT)
		);
	}

	/**
	 * Normalises this vector (sets the length to 1), then returns this.
	 *
	 * @return This
	 */
	public HDQVec normalise() {
		return invscale(getLen());
	}

	/**
	 * Scales this vector by -1, then returns this.
	 *
	 * @return This
	 */
	public HDQVec negate() {
		data[0] = data[0].negate();
		data[1] = data[1].negate();
		data[2] = data[2].negate();
		return this;
	}

	/**
	 * Reflects this vector through the plane defined by the provided normal
	 * vector, then returns this. If the provided vector is not normalised, the
	 * result will be scaled by the same amount as the provided vector length.
	 *
	 * @param n The plane normal vector.
	 * @return This
	 */
	public HDQVec reflect(HDQVec n) {
		return set(
						ONE.subtract(TWO.multiply(n.data[0], CONTEXT).multiply(n.data[0], CONTEXT), CONTEXT).multiply(data[0], CONTEXT)
						.subtract(TWO.multiply(n.data[0], CONTEXT).multiply(n.data[1], CONTEXT).multiply(data[1], CONTEXT), CONTEXT)
						.subtract(TWO.multiply(n.data[0], CONTEXT).multiply(n.data[2], CONTEXT).multiply(data[2], CONTEXT), CONTEXT),
						ONE.subtract(TWO.multiply(n.data[1], CONTEXT).multiply(n.data[1], CONTEXT), CONTEXT).multiply(data[1], CONTEXT)
						.subtract(TWO.multiply(n.data[1], CONTEXT).multiply(n.data[0], CONTEXT).multiply(data[0], CONTEXT), CONTEXT)
						.subtract(TWO.multiply(n.data[1], CONTEXT).multiply(n.data[2], CONTEXT).multiply(data[2], CONTEXT), CONTEXT),
						ONE.subtract(TWO.multiply(n.data[2], CONTEXT).multiply(n.data[2], CONTEXT), CONTEXT).multiply(data[2], CONTEXT)
						.subtract(TWO.multiply(n.data[2], CONTEXT).multiply(n.data[0], CONTEXT).multiply(data[0], CONTEXT), CONTEXT)
						.subtract(TWO.multiply(n.data[2], CONTEXT).multiply(n.data[1], CONTEXT).multiply(data[1], CONTEXT), CONTEXT)
		);
	}

	/**
	 * Sets each component to the remainder of the division by the corresponding
	 * component in the provided vector, then returns this.
	 *
	 * @param n The wrapping vector
	 * @return This
	 */
	public HDQVec remainder(HDQVec n) {
		return set(
						data[0].remainder(n.data[0], CONTEXT),
						data[1].remainder(n.data[1], CONTEXT),
						data[2].remainder(n.data[2], CONTEXT)
		);
	}

	/**
	 * Returns a copy of this vector as a <code>LDVec</code>.
	 *
	 * @return A copy of This
	 */
	public LDVec toLD() {
		return new LDVec(data[0].floatValue(), data[1].floatValue(), data[2].floatValue());
	}

	/**
	 * Returns a copy of this vector as a <code>HDVec</code>
	 *
	 * @return A copy of This
	 */
	public HDVec toHD() {
		return new HDVec(data[0].doubleValue(), data[1].doubleValue(), data[2].doubleValue());
	}

	/**
	 * Returns a copy of this vector.
	 *
	 * @return A copy of This
	 */
	public HDQVec toHDQ() {
		return new HDQVec(this);
	}

	/**
	 * Creates a copy of this, equivalent to calling toLD()
	 * @return
	 */
	@Override
	public HDQVec clone() {
		return new HDQVec(this);
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
	 * Returns true if the provided object is a <code>HDQVec</code> with the same
	 * components as this. Use a comparison of square separation with some
	 * tolerance to determine when two vectors are similar
	 *
	 * @param obj The object to check
	 * @return True if the provided object is the same as this.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final HDQVec other = (HDQVec) obj;
		if (!Arrays.deepEquals(this.data, other.data)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 83 * hash + Arrays.deepHashCode(this.data);
		return hash;
	}
}
