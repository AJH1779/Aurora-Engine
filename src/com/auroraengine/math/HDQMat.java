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

import java.math.BigDecimal;
import static java.math.BigDecimal.*;
import static com.auroraengine.math.HDQVec.CONTEXT;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;

import java.util.Arrays;
// TODO: Update to be of the same structure as LD and HD variants.

/**
 *
 * @author LittleRover
 */
public final class HDQMat {
	// The Static Methods

	private static final BigDecimal[] IDENTITY = new BigDecimal[]{ONE, ZERO, ZERO,
																																ZERO, ZERO, ONE,
																																ZERO, ZERO, ZERO,
																																ZERO, ONE, ZERO,
																																ZERO, ZERO, ZERO,
																																ONE};
	private static final BigDecimal[] ZERO_MAT = new BigDecimal[16];

	/**
	 * Performs the matrix multiplication of ref1 on ref2, returning the result as
	 * a new reference matrix.
	 *
	 * @param ref1 The inner reference frame
	 * @param ref2 The second reference frame
	 *
	 * @return The final reference frame
	 */
	public static HDQMat mult(HDQMat ref1, HDQMat ref2) {
		return mult(ref1, ref2, null);
	}

	/**
	 * Performs the matrix multiplication of ref1 on ref2, placing the result in
	 * the provided target reference frame.
	 *
	 * @param ref1   The inner reference frame
	 * @param ref2   The second reference frame
	 * @param target The target reference frame
	 *
	 * @return The final reference frame
	 */
	public static HDQMat mult(HDQMat ref1, HDQMat ref2, HDQMat target) {
		BigDecimal[] d = new BigDecimal[16];
		d[0] = (ref1.dat[0].multiply(ref2.dat[0])).add(ref1.dat[4].multiply(
						ref2.dat[1])).add(ref1.dat[8].multiply(ref2.dat[2]));
		d[1] = (ref1.dat[1].multiply(ref2.dat[0])).add(ref1.dat[5].multiply(
						ref2.dat[1])).add(ref1.dat[9].multiply(ref2.dat[2]));
		d[2] = (ref1.dat[2].multiply(ref2.dat[0])).add(ref1.dat[6].multiply(
						ref2.dat[1])).add(ref1.dat[10].multiply(ref2.dat[2]));
		d[4] = (ref1.dat[0].multiply(ref2.dat[4])).add(ref1.dat[4].multiply(
						ref2.dat[5])).add(ref1.dat[8].multiply(ref2.dat[6]));
		d[5] = (ref1.dat[1].multiply(ref2.dat[4])).add(ref1.dat[5].multiply(
						ref2.dat[5])).add(ref1.dat[9].multiply(ref2.dat[6]));
		d[6] = (ref1.dat[2].multiply(ref2.dat[4])).add(ref1.dat[6].multiply(
						ref2.dat[5])).add(ref1.dat[10].multiply(ref2.dat[6]));
		d[8] = (ref1.dat[0].multiply(ref2.dat[8])).add(ref1.dat[4].multiply(
						ref2.dat[9])).add(ref1.dat[8].multiply(ref2.dat[10]));
		d[9] = (ref1.dat[1].multiply(ref2.dat[8])).add(ref1.dat[5].multiply(
						ref2.dat[9])).add(ref1.dat[9].multiply(ref2.dat[10]));
		d[10] = (ref1.dat[2].multiply(ref2.dat[8])).add(ref1.dat[6].multiply(
						ref2.dat[9])).add(ref1.dat[10].multiply(ref2.dat[10]));
		d[12] = (ref1.dat[0].multiply(ref2.dat[12])).add(ref1.dat[4].multiply(
						ref2.dat[13])).add(ref1.dat[8].multiply(ref2.dat[14])).add(
						ref1.dat[12]);
		d[13] = (ref1.dat[1].multiply(ref2.dat[12])).add(ref1.dat[5].multiply(
						ref2.dat[13])).add(ref1.dat[9].multiply(ref2.dat[14])).add(
						ref1.dat[13]);
		d[14] = (ref1.dat[2].multiply(ref2.dat[12])).add(ref1.dat[6].multiply(
						ref2.dat[13])).add(ref1.dat[10].multiply(ref2.dat[14])).add(
						ref1.dat[14]);
		d[15] = ONE;
		return target != null ? target.set(d) : new HDQMat(d);
	}

	/**
	 * Creates a new reference frame which performs no transformation.
	 */
	public HDQMat() {
		identity();
	}

	/**
	 * Creates a new reference frame translated by the given vector.
	 *
	 * @param vec The translation vector
	 */
	public HDQMat(HDQVec vec) {
		set(vec);
	}

	/**
	 * Creates a new reference frame rotated by the given angle
	 *
	 * @param ang The rotation angle
	 */
	public HDQMat(HDQAng ang) {
		set(ang);
	}

	/**
	 * Creates a new reference frame, such that the three provided vectors form
	 * the X, Y, Z basis for the space. The AB direction is the z-axis, the cross
	 * product of AB and AC form the y-axis, and the cross product of the y-axis
	 * and z-axis form the x-axis.
	 *
	 * @param vecA Position Vector A
	 * @param vecB Position Vector B
	 * @param vecC Position Vector C
	 */
	public HDQMat(HDQVec vecA, HDQVec vecB, HDQVec vecC) {
		set(vecA, vecB, vecC);
	}

	/**
	 * Creates a new reference frame depending on the number of vectors provided.
	 * If one is provided, the new reference frame is translated by the given
	 * vector. If three are provided, the vectors form the X, Y, Z basis for the
	 * space. Otherwise, an <code>IllegalArgumentException</code> is thrown.
	 *
	 * @param vecs The vectors
	 */
	public HDQMat(HDQVec[] vecs) {
		set(vecs);
	}

	/**
	 * Creates a new reference frame with the provided column-major 4x4 matrix
	 * given as a 16 BigDecimal array. The matrix given should be a special
	 * orthogonal affine matrix.
	 *
	 * @param matrix The matrix.
	 */
	public HDQMat(BigDecimal[] matrix) {
		set(matrix);
	}

	/**
	 * Creates a new reference frame which is a duplicate of the provided frame.
	 *
	 * @param ref The frame to copy
	 */
	public HDQMat(HDQMat ref) {
		set(ref);
	}
	private final BigDecimal[] dat = new BigDecimal[16];
	private final DoubleBuffer buffer = ByteBuffer.allocateDirect(16 *
																																Double.BYTES)
					.order(ByteOrder.nativeOrder()).asDoubleBuffer();
	private final DoubleBuffer read_only = buffer.asReadOnlyBuffer();
	private volatile boolean modified = true;

	/**
	 * Clears the reference frame and sets it to a translation by the given
	 * vector, then returns this.
	 *
	 * @param vec The translation vector
	 *
	 * @return This
	 */
	public HDQMat set(HDQVec vec) {
		identity();
		dat[12] = vec.X();
		dat[13] = vec.Y();
		dat[14] = vec.Z();
		return this;
	}

	/**
	 * Clears the reference frame and sets it to a rotation by the given angle,
	 * then returns this.
	 *
	 * @param ang The rotation angle
	 *
	 * @return This
	 */
	public HDQMat set(HDQAng ang) {
		zero();
		dat[0] = ang.cos().add(ang.X().multiply(ang.X()).multiply(ONE.subtract(ang
						.cos())));
		dat[1] = ang.Y().multiply(ang.X()).multiply(ONE.subtract(ang.cos())).add(ang
						.X().multiply(ang.sin()));
		dat[2] = ang.X().multiply(ang.Z()).multiply(ONE.subtract(ang.cos()))
						.subtract(ang.Y().multiply(ang.sin()));

		dat[4] = ang.X().multiply(ang.Y()).multiply(ONE.subtract(ang.cos()))
						.subtract(ang.Z().multiply(ang.sin()));
		dat[5] = ang.cos().add(ang.Y().multiply(ang.Y()).multiply(ONE.subtract(ang
						.cos())));
		dat[6] = ang.X().multiply(ang.Z()).multiply(ONE.subtract(ang.cos())).add(ang
						.X().multiply(ang.sin()));

		dat[8] = ang.X().multiply(ang.Z()).multiply(ONE.subtract(ang.cos())).add(ang
						.Y().multiply(ang.sin()));
		dat[9] = ang.Y().multiply(ang.Z()).multiply(ONE.subtract(ang.cos()))
						.subtract(ang.X().multiply(ang.sin()));
		dat[10] = ang.cos().add(ang.Z().multiply(ang.Z()).multiply(ONE.subtract(ang
						.cos())));

		dat[15] = ONE;
		modified = true;
		return this;
	}

	/**
	 * Clears the reference frame and sets it to the basis provided by the three
	 * vectors. The AB direction is the z-axis, the cross product of AB and AC
	 * form the y-axis, and the cross product of the y-axis and z-axis form the
	 * x-axis.
	 *
	 * @param vecA Position Vector A
	 * @param vecB Position Vector B
	 * @param vecC Position Vector C
	 *
	 * @return This
	 */
	public HDQMat set(HDQVec vecA, HDQVec vecB, HDQVec vecC) {
		HDQVec z = new HDQVec(vecB).negTranslate(vecA);
		HDQVec y = z.cross(new HDQVec(vecC).negTranslate(vecA));
		HDQVec x = y.cross(z).normalise();
		y.normalise();
		z.normalise();

		set(new BigDecimal[]{
			x.X(), x.Y(), x.Z(), ZERO,
			y.X(), y.Y(), y.Z(), ZERO,
			z.X(), z.Y(), z.Z(), ZERO,
			ZERO, ZERO, ZERO, ONE
		});
		modified = true;
		return this;
	}

	/**
	 * Clears the reference frame and sets it depending on the number of vectors
	 * provided, then returns this. If one is provided, the new reference frame is
	 * translated by the given vector. If three are provided, the vectors form the
	 * X, Y, Z basis for the space. Otherwise, an
	 * <code>IllegalArgumentException</code> is thrown.
	 *
	 * @param vecs The vectors
	 *
	 * @return This
	 */
	public HDQMat set(HDQVec[] vecs) {
		switch (vecs.length) {
			case 1:
				return set(vecs[0]);
			case 3:
				return set(vecs[0], vecs[1], vecs[2]);
			default:
				throw new IllegalArgumentException(
								"Array must be of length 1 or 3! Found " + vecs.length + ".");
		}
	}

	/**
	 * Sets the reference frame to be described by the provided column-major 4x4
	 * matrix given as a 16 BigDecimal array. The matrix given should be a special
	 * orthogonal affine matrix.
	 *
	 * @param matrix The matrix
	 *
	 * @return This
	 */
	public HDQMat set(BigDecimal[] matrix) {
		if (matrix.length == dat.length) {
			System.arraycopy(matrix, 0, dat, 0, 16);
			modified = true;
		} else {
			throw new IllegalArgumentException("Array must be of length 16! Found " +
																				 matrix.length + ".");
		}
		return this;
	}

	/**
	 * Sets the reference frame to be a copy of the provided reference frame.
	 *
	 * @param ref The frame to copy
	 *
	 * @return This
	 */
	public HDQMat set(HDQMat ref) {
		return set(ref.dat);
	}

	/**
	 * Sets this to the identity reference frame, then returns this.
	 *
	 * @return This
	 */
	public HDQMat identity() {
		return set(IDENTITY);
	}

	/**
	 * Sets this to the zero reference frame, then returns this.
	 *
	 * @return This
	 */
	private HDQMat zero() {
		return set(ZERO_MAT);
	}

	/**
	 * Sets this to its inverse. Note this expolits the fact this matrix is a
	 * special orthogonal affine matrix, and so does not transform arbitrary
	 * matrix arguments.
	 *
	 * @return This
	 */
	public HDQMat invert() {
		BigDecimal[] d = new BigDecimal[16];
		d[0] = dat[0];
		d[1] = dat[4];
		d[2] = dat[8];
		d[4] = dat[1];
		d[5] = dat[5];
		d[6] = dat[9];
		d[8] = dat[2];
		d[9] = dat[6];
		d[10] = dat[10];
		d[12] = (dat[0].multiply(dat[12]).add(dat[1].multiply(dat[13])).add(dat[2]
						.multiply(dat[14]))).negate();
		d[13] = (dat[4].multiply(dat[12]).add(dat[5].multiply(dat[13])).add(dat[6]
						.multiply(dat[14]))).negate();
		d[14] = (dat[8].multiply(dat[12]).add(dat[9].multiply(dat[13])).add(dat[10]
						.multiply(dat[14]))).negate();
		d[15] = ONE;
		return set(d);
	}

	/**
	 * Translates the reference frame locally, that is after the transformation
	 * described by this frame, then returns this. It is analogous to moving an
	 * object within this reference frame.
	 *
	 * @param vec The translation vector
	 *
	 * @return This
	 */
	public HDQMat translateLocally(HDQVec vec) {
		dat[12] = dat[12].add(vec.X());
		dat[13] = dat[13].add(vec.Y());
		dat[14] = dat[14].add(vec.Z());
		modified = true;
		return this;
	}

	/**
	 * Translates the reference frame globally, that is before the transformation
	 * described by this frame, then returns this. It is analogous to moving an
	 * object outside of this reference frame.
	 *
	 * @param vec The translation vector
	 *
	 * @return This
	 */
	public HDQMat translateGlobally(HDQVec vec) {
		dat[12] = dat[12].add(dat[0].multiply(vec.X()))
						.add(dat[4].multiply(vec.Y())).add(dat[8].multiply(vec.Z()));
		dat[13] = dat[14].add(dat[1].multiply(vec.X()))
						.add(dat[5].multiply(vec.Y())).add(dat[9].multiply(vec.Z()));
		dat[14] = dat[13].add(dat[2].multiply(vec.X()))
						.add(dat[6].multiply(vec.Y())).add(dat[10].multiply(vec.Z()));
		modified = true;
		return this;
	}

	/**
	 * Rotates the reference frame locally, that is after the transformation
	 * described by this frame, then returns this. It is analogous to moving an
	 * object within this reference frame.
	 *
	 * @param ang The rotation angle
	 *
	 * @return This
	 */
	public HDQMat rotateLocally(HDQAng ang) {
		BigDecimal[] r = new BigDecimal[9], d = new BigDecimal[16];
		r[0] = ang.cos().add(ang.X().multiply(ang.X().multiply((ONE.subtract(ang
						.cos())))));
		r[1] = ang.Y().multiply(ang.X()).multiply(ONE.subtract(ang.cos())).add(ang
						.Z().multiply(ang.sin()));
		r[2] = ang.X().multiply(ang.Z()).multiply(ONE.subtract(ang.cos())).subtract(
						ang.Y().multiply(ang.sin()));
		r[3] = ang.X().multiply(ang.Y()).multiply(ONE.subtract(ang.cos())).subtract(
						ang.Z().multiply(ang.sin()));
		r[4] = ang.cos().add(ang.Y().multiply(ang.Y().multiply((ONE.subtract(ang
						.cos())))));
		r[5] = ang.Y().multiply(ang.Z()).multiply(ONE.subtract(ang.cos())).add(ang
						.X().multiply(ang.sin()));
		r[6] = ang.X().multiply(ang.Z()).multiply(ONE.subtract(ang.cos())).add(ang
						.Y().multiply(ang.sin()));
		r[7] = ang.Y().multiply(ang.Z()).multiply(ONE.subtract(ang.cos())).subtract(
						ang.X().multiply(ang.sin()));
		r[8] = ang.cos().add(ang.Z().multiply(ang.Z().multiply((ONE.subtract(ang
						.cos())))));

		d[0] = r[0].multiply(dat[0]).add(r[3].multiply(dat[1])).add(r[6].multiply(
						dat[2]));
		d[1] = r[1].multiply(dat[0]).add(r[4].multiply(dat[1])).add(r[7].multiply(
						dat[2]));
		d[2] = r[2].multiply(dat[0]).add(r[5].multiply(dat[1])).add(r[8].multiply(
						dat[2]));
		d[4] = r[0].multiply(dat[4]).add(r[3].multiply(dat[5])).add(r[6].multiply(
						dat[6]));
		d[5] = r[1].multiply(dat[4]).add(r[4].multiply(dat[5])).add(r[7].multiply(
						dat[6]));
		d[6] = r[2].multiply(dat[4]).add(r[5].multiply(dat[5])).add(r[8].multiply(
						dat[6]));
		d[8] = r[0].multiply(dat[8]).add(r[3].multiply(dat[9])).add(r[6].multiply(
						dat[10]));
		d[9] = r[1].multiply(dat[8]).add(r[4].multiply(dat[9])).add(r[7].multiply(
						dat[10]));
		d[10] = r[2].multiply(dat[8]).add(r[5].multiply(dat[9])).add(r[8].multiply(
						dat[10]));
		d[12] = r[0].multiply(dat[12]).add(r[3].multiply(dat[13])).add(r[6]
						.multiply(dat[14]));
		d[13] = r[1].multiply(dat[12]).add(r[4].multiply(dat[13])).add(r[7]
						.multiply(dat[14]));
		d[14] = r[2].multiply(dat[12]).add(r[5].multiply(dat[13])).add(r[8]
						.multiply(dat[14]));
		d[15] = ONE;

		return set(d);
	}

	/**
	 * Rotates the reference frame globally, that is before the transformation
	 * described by this frame, then returns this. It is analogous to moving an
	 * object outside of this reference frame.
	 *
	 * @param ang The Rotation Object
	 *
	 * @return This
	 */
	public HDQMat rotateGlobally(HDQAng ang) {
		BigDecimal[] r = new BigDecimal[9], d = new BigDecimal[16];
		r[0] = ang.cos().add(ang.X().multiply(ang.X().multiply((ONE.subtract(ang
						.cos())))));
		r[1] = ang.Y().multiply(ang.X()).multiply(ONE.subtract(ang.cos())).add(ang
						.Z().multiply(ang.sin()));
		r[2] = ang.X().multiply(ang.Z()).multiply(ONE.subtract(ang.cos())).subtract(
						ang.Y().multiply(ang.sin()));
		r[3] = ang.X().multiply(ang.Y()).multiply(ONE.subtract(ang.cos())).subtract(
						ang.Z().multiply(ang.sin()));
		r[4] = ang.cos().add(ang.Y().multiply(ang.Y().multiply((ONE.subtract(ang
						.cos())))));
		r[5] = ang.Y().multiply(ang.Z()).multiply(ONE.subtract(ang.cos())).add(ang
						.X().multiply(ang.sin()));
		r[6] = ang.X().multiply(ang.Z()).multiply(ONE.subtract(ang.cos())).add(ang
						.Y().multiply(ang.sin()));
		r[7] = ang.Y().multiply(ang.Z()).multiply(ONE.subtract(ang.cos())).subtract(
						ang.X().multiply(ang.sin()));
		r[8] = ang.cos().add(ang.Z().multiply(ang.Z().multiply((ONE.subtract(ang
						.cos())))));

		d[0] = (dat[0].multiply(r[0])).add(dat[4].multiply(r[1])).add(dat[8]
						.multiply(r[2])).add(dat[12]);
		d[1] = (dat[1].multiply(r[0])).add(dat[5].multiply(r[1])).add(dat[9]
						.multiply(r[2])).add(dat[13]);
		d[2] = (dat[2].multiply(r[0])).add(dat[6].multiply(r[1])).add(dat[10]
						.multiply(r[2])).add(dat[14]);
		d[4] = (dat[0].multiply(r[3])).add(dat[4].multiply(r[4])).add(dat[8]
						.multiply(r[5])).add(dat[12]);
		d[5] = (dat[1].multiply(r[3])).add(dat[5].multiply(r[4])).add(dat[9]
						.multiply(r[5])).add(dat[13]);
		d[6] = (dat[2].multiply(r[3])).add(dat[6].multiply(r[4])).add(dat[10]
						.multiply(r[5])).add(dat[14]);
		d[8] = (dat[0].multiply(r[6])).add(dat[4].multiply(r[7])).add(dat[8]
						.multiply(r[8])).add(dat[12]);
		d[9] = (dat[1].multiply(r[6])).add(dat[5].multiply(r[7])).add(dat[9]
						.multiply(r[8])).add(dat[13]);
		d[10] = (dat[2].multiply(r[6])).add(dat[6].multiply(r[7])).add(dat[10]
						.multiply(r[8])).add(dat[14]);
		d[12] = (dat[0].multiply(r[6])).add(dat[4].multiply(r[7])).add(dat[8]
						.multiply(r[8])).add(dat[12]);
		d[13] = (dat[1].multiply(r[6])).add(dat[5].multiply(r[7])).add(dat[9]
						.multiply(r[8])).add(dat[13]);
		d[14] = (dat[2].multiply(r[6])).add(dat[6].multiply(r[7])).add(dat[10]
						.multiply(r[8])).add(dat[14]);
		d[15] = ONE;

		return set(d);
	}

	/**
	 *
	 * Transforms this reference frame locally with the provided reference frame
	 * transformation, that is after the transformation described by this frame,
	 * then returns this. It is analogous to moving an object within this
	 * reference frame.
	 *
	 * @param ref The transformation frame
	 *
	 * @return This
	 */
	public HDQMat multiplyLocally(HDQMat ref) {
		return mult(ref, this, this);
	}

	/**
	 * Transforms this reference frame globally with the provided reference frame
	 * transformation, that is before the transformation described by this frame,
	 * then returns this. It is analogous to moving an object outside of this
	 * reference frame.
	 *
	 * @param ref The transformation frame
	 *
	 * @return This
	 */
	public HDQMat multiplyGlobally(HDQMat ref) {
		return mult(this, ref, this);
	}

	/**
	 * Returns the position vector transformed to inside this reference frame.
	 * This is equivalent to calling <code>transform(vec, true)<\code>.
	 *
	 * @param vec The global position vector
	 *
	 * @return The local position vector
	 */
	public HDQVec transform(HDQVec vec) {
		return transform(vec, true);
	}

	/**
	 * Returns the vector transformed to inside this reference frame. The position
	 * flag should be true if the vector is a position vector.
	 *
	 * @param vec      The global vector
	 * @param position True if provided vector is a position vector
	 *
	 * @return The local vector
	 */
	public HDQVec transform(HDQVec vec, boolean position) {
		return new HDQVec(
						(dat[0].multiply(vec.X(), CONTEXT)).add(dat[4].multiply(vec.Y(),
																																		CONTEXT))
										.add(dat[8].multiply(vec.Z(), CONTEXT)).add(position ?
																																dat[12] : ZERO,
																																CONTEXT),
						(dat[1].multiply(vec.X(), CONTEXT)).add(dat[5].multiply(vec.Y(),
																																		CONTEXT))
										.add(dat[9].multiply(vec.Z(), CONTEXT)).add(position ?
																																dat[13] : ZERO,
																																CONTEXT),
						(dat[2].multiply(vec.X(), CONTEXT)).add(dat[6].multiply(vec.Y(),
																																		CONTEXT))
										.add(dat[10].multiply(vec.Z(), CONTEXT)).add(position ?
																																 dat[14] : ZERO,
																																 CONTEXT)
		);
	}

	/**
	 * Returns the angle transformed to inside this reference frame.
	 *
	 * @param ang The global angle
	 *
	 * @return The local angle
	 */
	public HDQAng transform(HDQAng ang) {
		return ang.set(transform(ang.vec(), false));
	}

	/**
	 * Returns the global origin in the reference frame.
	 *
	 * @return The local position of the global origin
	 */
	public HDQVec getOrigin() {
		return new HDQVec(dat[12], dat[13], dat[14]);
	}

	/**
	 * Returns a copy of this reference frame in a <code>LDRef</code> object.
	 *
	 * @return A copy of this.
	 */
	public LDMat toLD() {
		float[] d = new float[16];
		for (int i = 0; i < dat.length; i++) {
			d[i] = dat[i].floatValue();
		}
		return new LDMat(d);
	}

	/**
	 * Returns a copy of this reference frame in a <code>HDRef</code> object.
	 *
	 * @return
	 */
	public HDMat toHD() {
		double[] d = new double[16];
		for (int i = 0; i < dat.length; i++) {
			d[i] = dat[i].doubleValue();
		}
		return new HDMat(d);
	}

	/**
	 * Returns a copy of this reference frame.
	 *
	 * @return A <code>HDQRef</code> copy of this.
	 */
	public HDQMat toHDQ() {
		return new HDQMat(this);
	}

	/**
	 * Returns a read-only version of the matrix in a BigDecimal-buffer.
	 *
	 * @return The matrix as a read-only buffer
	 */
	public DoubleBuffer buffer() {
		if (modified) {
			for (BigDecimal dat1 : dat) {
				buffer.put(dat1.doubleValue());
			}
			buffer.flip();
			modified = false;
		}
		return this.read_only;
	}

	/**
	 * Writes the 16 BigDecimal matrix in major-column format into the byte
	 * buffer, then returns the provided buffer.
	 *
	 * @param bb The Buffer to write to
	 *
	 * @return The Buffer written to
	 */
	public ByteBuffer write(ByteBuffer bb) {
		for (BigDecimal dat1 : dat) {
			bb.putDouble(dat1.doubleValue());
		}
		return bb;
	}

	/**
	 * Returns the transformation matrix of this reference frame in the format
	 * "{{0,4,8,12}\\n{1,5,9,13}\\n{2,6,10,14}\\n{3,7,11,15}}".
	 *
	 * @return The transformation matrix in text.
	 */
	@Override
	public String toString() {
		return "{{" + dat[0] + "," + dat[4] + "," + dat[8] + "," + dat[12] + "}\n" +
					 " {" + dat[1] + "," + dat[5] + "," + dat[9] + "," + dat[13] + "}\n" +
					 " {" + dat[2] + "," + dat[6] + "," + dat[10] + "," + dat[14] +
					 "}\n" +
					 " {" + dat[3] + "," + dat[7] + "," + dat[11] + "," + dat[15] + "}}";
	}

	/**
	 * Returns true only if the provided object is a <code>HDQRef</code> object
	 * with the same transformation matrix as this.
	 *
	 * @param obj The object check
	 *
	 * @return If it is equivalent
	 */
	@Override
	public boolean equals(Object obj) {
		return obj instanceof HDQMat && Arrays.equals(dat, ((HDQMat) obj).dat);
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 97 * hash + Arrays.hashCode(this.dat);
		return hash;
	}
}
