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
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 *
 *
 * TODO: Look up scaling of reference frames and implement.
 *
 * @author LittleRover
 */
public final class LDMat implements Cloneable {

	private static final float[] IDENTITY = new float[]{1f, 0f, 0f, 0f, 0f, 1f, 0f,
																											0f, 0f, 0f, 1f, 0f, 0f, 0f,
																											0f, 1f};
	private static final Logger LOG = AuroraLogs.getLogger(LDMat.class.getName());

	/**
	 * Performs the matrix multiplication of ref1 on ref2, returning the result as
	 * a new reference matrix.
	 *
	 * @param p_ref1 The inner reference frame
	 * @param p_ref2 The second reference frame
	 *
	 * @return The final reference frame
	 */
	public static LDMat mult(LDMat p_ref1, LDMat p_ref2) {
		return mult(p_ref1, p_ref2, null);
	}

	/**
	 * Performs the matrix multiplication of ref1 on ref2, placing the result in
	 * the provided target reference frame.
	 *
	 * @param p_ref1   The inner reference frame
	 * @param p_ref2   The second reference frame
	 * @param p_target The target reference frame
	 *
	 * @return The final reference frame
	 */
	public static LDMat mult(LDMat p_ref1, LDMat p_ref2, LDMat p_target) {
		float[] d = new float[16];
		for (int j = 0; j < 4; j++) {
			for (int i = 0; i < 4; i++) {
				for (int a = 0; a < 4; a++) {
					d[j * 4 + i] += p_ref1.data[i + a * 4] * p_ref2.data[j * 4 + a];
				}
			}
		}
		return p_target != null ? p_target.set(d, p_ref1.affine && p_ref2.affine) :
					 new LDMat(d);
	}

	LDMat(float[] matrix, boolean set_affine) {
		set(matrix, set_affine);
	}

	// The Local Methods
	/**
	 * Creates a new reference frame which performs no transformation.
	 */
	public LDMat() {
		identity();
	}

	/**
	 * Creates a new reference frame translated by the given vector.
	 *
	 * @param vec The translation vector
	 */
	public LDMat(LDVec vec) {
		set(vec);
	}

	/**
	 * Creates a new reference frame rotated by the given angle
	 *
	 * @param ang The rotation angle
	 */
	public LDMat(LDAng ang) {
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
	public LDMat(LDVec vecA, LDVec vecB, LDVec vecC) {
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
	public LDMat(LDVec[] vecs) {
		set(vecs);
	}

	/**
	 * Creates a new reference frame with the provided column-major 4x4 matrix
	 * given as a 16 float array. The matrix given should be a special orthogonal
	 * affine matrix.
	 *
	 * @param matrix The matrix.
	 */
	public LDMat(float[] matrix) {
		set(matrix, false);
	}

	/**
	 * Creates a new reference frame which is a duplicate of the provided frame.
	 *
	 * @param ref The frame to copy
	 */
	public LDMat(LDMat ref) {
		set(ref);
	}
	private boolean affine;
	private final FloatBuffer buffer = ByteBuffer.allocateDirect(16 * Float.BYTES)
					.order(ByteOrder.nativeOrder()).asFloatBuffer();
	private final float[] data = new float[16];
	private boolean modified = true;
	private final FloatBuffer read_only = buffer.asReadOnlyBuffer();

	LDMat set(float[] matrix, boolean set_affine) {
		if (matrix.length == data.length) {
			System.arraycopy(matrix, 0, data, 0, 16);
			modified = true;
			affine = set_affine;
		} else {
			throw new IllegalArgumentException("Array must be of length 16! Found " +
																				 matrix.length + ".");
		}
		return this;
	}

	/**
	 * Returns a read-only version of the matrix in a float-buffer.
	 *
	 * @return The matrix as a read-only buffer
	 */
	public FloatBuffer buffer() {
		if (modified) {
			buffer.put(data);
			buffer.flip();
			modified = false;
		}
		return this.read_only;
	}

	@Override
	public LDMat clone() {
		// TODO: This implementation of clone violates the contract of clone.
		return new LDMat(this);
	}

	/**
	 * Returns true only if the provided object is a <code>LDRef</code> object
	 * with the same transformation matrix as this.
	 *
	 * @param obj The object check
	 *
	 * @return If it is equivalent
	 */
	@Override
	public boolean equals(Object obj) {
		return obj instanceof LDMat && Arrays.equals(data, ((LDMat) obj).data);
	}

	/**
	 * Returns the global origin in the reference frame.
	 *
	 * @return The local position of the global origin
	 */
	public LDVec getOrigin() {
		return new LDVec(data[12], data[13], data[14], data[15]);
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 79 * hash + Arrays.hashCode(this.data);
		return hash;
	}

	/**
	 * Sets this to the identity reference frame, then returns this.
	 *
	 * @return This
	 */
	public LDMat identity() {
		return set(IDENTITY, true);
	}

	/**
	 * Sets this to its inverse. Note this expolits the fact this matrix is a
	 * special orthogonal affine matrix, and so does not transform arbitrary
	 * matrix arguments.
	 *
	 * @return This
	 */
	public LDMat invert() {
		float[] inv = new float[16];
		if (affine) {
			inv[0] = data[0];
			inv[1] = data[4];
			inv[2] = data[8];
			inv[4] = data[1];
			inv[5] = data[5];
			inv[6] = data[9];
			inv[8] = data[2];
			inv[9] = data[6];
			inv[10] = data[10];
			inv[12] = -(data[0] * data[12] + data[1] * data[13] + data[2] * data[14]);
			inv[13] = -(data[4] * data[12] + data[5] * data[13] + data[6] * data[14]);
			inv[14] = -(data[8] * data[12] + data[9] * data[13] + data[10] * data[14]);
			inv[15] = 1.0f;
		} else {
			inv[0] = data[5] * data[10] * data[15] -
							 data[5] * data[11] * data[14] -
							 data[9] * data[6] * data[15] +
							 data[9] * data[7] * data[14] +
							 data[13] * data[6] * data[11] -
							 data[13] * data[7] * data[10];

			inv[4] = -data[4] * data[10] * data[15] +
							 data[4] * data[11] * data[14] +
							 data[8] * data[6] * data[15] -
							 data[8] * data[7] * data[14] -
							 data[12] * data[6] * data[11] +
							 data[12] * data[7] * data[10];

			inv[8] = data[4] * data[9] * data[15] -
							 data[4] * data[11] * data[13] -
							 data[8] * data[5] * data[15] +
							 data[8] * data[7] * data[13] +
							 data[12] * data[5] * data[11] -
							 data[12] * data[7] * data[9];

			inv[12] = -data[4] * data[9] * data[14] +
								data[4] * data[10] * data[13] +
								data[8] * data[5] * data[14] -
								data[8] * data[6] * data[13] -
								data[12] * data[5] * data[10] +
								data[12] * data[6] * data[9];

			inv[1] = -data[1] * data[10] * data[15] +
							 data[1] * data[11] * data[14] +
							 data[9] * data[2] * data[15] -
							 data[9] * data[3] * data[14] -
							 data[13] * data[2] * data[11] +
							 data[13] * data[3] * data[10];

			inv[5] = data[0] * data[10] * data[15] -
							 data[0] * data[11] * data[14] -
							 data[8] * data[2] * data[15] +
							 data[8] * data[3] * data[14] +
							 data[12] * data[2] * data[11] -
							 data[12] * data[3] * data[10];

			inv[9] = -data[0] * data[9] * data[15] +
							 data[0] * data[11] * data[13] +
							 data[8] * data[1] * data[15] -
							 data[8] * data[3] * data[13] -
							 data[12] * data[1] * data[11] +
							 data[12] * data[3] * data[9];

			inv[13] = data[0] * data[9] * data[14] -
								data[0] * data[10] * data[13] -
								data[8] * data[1] * data[14] +
								data[8] * data[2] * data[13] +
								data[12] * data[1] * data[10] -
								data[12] * data[2] * data[9];

			inv[2] = data[1] * data[6] * data[15] -
							 data[1] * data[7] * data[14] -
							 data[5] * data[2] * data[15] +
							 data[5] * data[3] * data[14] +
							 data[13] * data[2] * data[7] -
							 data[13] * data[3] * data[6];

			inv[6] = -data[0] * data[6] * data[15] +
							 data[0] * data[7] * data[14] +
							 data[4] * data[2] * data[15] -
							 data[4] * data[3] * data[14] -
							 data[12] * data[2] * data[7] +
							 data[12] * data[3] * data[6];

			inv[10] = data[0] * data[5] * data[15] -
								data[0] * data[7] * data[13] -
								data[4] * data[1] * data[15] +
								data[4] * data[3] * data[13] +
								data[12] * data[1] * data[7] -
								data[12] * data[3] * data[5];

			inv[14] = -data[0] * data[5] * data[14] +
								data[0] * data[6] * data[13] +
								data[4] * data[1] * data[14] -
								data[4] * data[2] * data[13] -
								data[12] * data[1] * data[6] +
								data[12] * data[2] * data[5];

			inv[3] = -data[1] * data[6] * data[11] +
							 data[1] * data[7] * data[10] +
							 data[5] * data[2] * data[11] -
							 data[5] * data[3] * data[10] -
							 data[9] * data[2] * data[7] +
							 data[9] * data[3] * data[6];

			inv[7] = data[0] * data[6] * data[11] -
							 data[0] * data[7] * data[10] -
							 data[4] * data[2] * data[11] +
							 data[4] * data[3] * data[10] +
							 data[8] * data[2] * data[7] -
							 data[8] * data[3] * data[6];

			inv[11] = -data[0] * data[5] * data[11] +
								data[0] * data[7] * data[9] +
								data[4] * data[1] * data[11] -
								data[4] * data[3] * data[9] -
								data[8] * data[1] * data[7] +
								data[8] * data[3] * data[5];

			inv[15] = data[0] * data[5] * data[10] -
								data[0] * data[6] * data[9] -
								data[4] * data[1] * data[10] +
								data[4] * data[2] * data[9] +
								data[8] * data[1] * data[6] -
								data[8] * data[2] * data[5];

			float det = data[0] * inv[0] + data[1] * inv[4] +
									data[2] * inv[8] + data[3] * inv[12];

			for (int i = 0; i < inv.length; i++) {
				inv[i] /= det;
			}
		}
		return set(inv, affine);
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
	public LDMat multiplyGlobally(LDMat ref) {
		return mult(this, ref, this);
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
	public LDMat multiplyLocally(LDMat ref) {
		return mult(ref, this, this);
	}

	/**
	 * Rotates the reference frame globally, that is before the transformation
	 * described by this frame, then returns this. It is analogous to moving an
	 * object outside of this reference frame.
	 *
	 * @param ang The rotation angle.
	 *
	 * @return This
	 */
	public LDMat rotateGlobally(LDAng ang) {
		float[] r = new float[9], d = new float[16];
		r[0] = ang.cos() + ang.X() * ang.X() * (1 - ang.cos());
		r[1] = ang.Y() * ang.X() * (1 - ang.cos()) + ang.Z() * ang.sin();
		r[2] = ang.X() * ang.Z() * (1 - ang.cos()) - ang.Y() * ang.sin();
		r[3] = ang.X() * ang.Y() * (1 - ang.cos()) - ang.Z() * ang.sin();
		r[4] = ang.cos() + ang.Y() * ang.Y() * (1 - ang.cos());
		r[5] = ang.Y() * ang.Z() * (1 - ang.cos()) + ang.X() * ang.sin();
		r[6] = ang.X() * ang.Z() * (1 - ang.cos()) + ang.Y() * ang.sin();
		r[7] = ang.Y() * ang.Z() * (1 - ang.cos()) - ang.X() * ang.sin();
		r[8] = ang.cos() + ang.Z() * ang.Z() * (1 - ang.cos());

		d[0] = data[0] * r[0] + data[4] * r[1] + data[8] * r[2];
		d[1] = data[1] * r[0] + data[5] * r[1] + data[9] * r[2];
		d[2] = data[2] * r[0] + data[6] * r[1] + data[10] * r[2];
		d[4] = data[0] * r[3] + data[4] * r[4] + data[8] * r[5];
		d[5] = data[1] * r[3] + data[5] * r[4] + data[9] * r[5];
		d[6] = data[2] * r[3] + data[6] * r[4] + data[10] * r[5];
		d[8] = data[0] * r[6] + data[4] * r[7] + data[8] * r[8];
		d[9] = data[1] * r[6] + data[5] * r[7] + data[9] * r[8];
		d[10] = data[2] * r[6] + data[6] * r[7] + data[10] * r[8];
		d[12] = data[12];
		d[13] = data[13];
		d[14] = data[14];
		d[15] = data[15];
		if (!affine) {
			d[3] = data[3] * r[0] + data[7] * r[1] + data[11] * r[2] + data[15];
			d[7] = data[3] * r[3] + data[7] * r[4] + data[11] * r[5] + data[15];
			d[11] = data[3] * r[6] + data[7] * r[7] + data[11] * r[8] + data[15];
		}
		return set(d, affine);
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
	public LDMat rotateLocally(LDAng ang) {
		float[] r = new float[9], d = new float[16];
		r[0] = ang.cos() + ang.X() * ang.X() * (1 - ang.cos());
		r[1] = ang.Y() * ang.X() * (1 - ang.cos()) + ang.Z() * ang.sin();
		r[2] = ang.X() * ang.Z() * (1 - ang.cos()) - ang.Y() * ang.sin();
		r[3] = ang.X() * ang.Y() * (1 - ang.cos()) - ang.Z() * ang.sin();
		r[4] = ang.cos() + ang.Y() * ang.Y() * (1 - ang.cos());
		r[5] = ang.Y() * ang.Z() * (1 - ang.cos()) + ang.X() * ang.sin();
		r[6] = ang.X() * ang.Z() * (1 - ang.cos()) + ang.Y() * ang.sin();
		r[7] = ang.Y() * ang.Z() * (1 - ang.cos()) - ang.X() * ang.sin();
		r[8] = ang.cos() + ang.Z() * ang.Z() * (1 - ang.cos());

		d[0] = r[0] * data[0] + r[3] * data[1] + r[6] * data[2];
		d[1] = r[1] * data[0] + r[4] * data[1] + r[7] * data[2];
		d[2] = r[2] * data[0] + r[5] * data[1] + r[8] * data[2];
		d[3] = data[3];
		d[4] = r[0] * data[4] + r[3] * data[5] + r[6] * data[6];
		d[5] = r[1] * data[4] + r[4] * data[5] + r[7] * data[6];
		d[6] = r[2] * data[4] + r[5] * data[5] + r[8] * data[6];
		d[7] = data[7];
		d[8] = r[0] * data[8] + r[3] * data[9] + r[6] * data[10];
		d[9] = r[1] * data[8] + r[4] * data[9] + r[7] * data[10];
		d[10] = r[2] * data[8] + r[5] * data[9] + r[8] * data[10];
		d[11] = data[11];
		d[12] = r[0] * data[12] + r[3] * data[13] + r[6] * data[14];
		d[13] = r[1] * data[12] + r[4] * data[13] + r[7] * data[14];
		d[14] = r[2] * data[12] + r[5] * data[13] + r[8] * data[14];
		d[15] = data[15];

		return set(d, affine);
	}

	/**
	 * Clears the reference frame and sets it to a translation by the given
	 * vector, then returns this.
	 *
	 * @param vec The translation vector
	 *
	 * @return This
	 */
	public LDMat set(LDVec vec) {
		identity();
		data[12] = vec.data[0];
		data[13] = vec.data[1];
		data[14] = vec.data[2];
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
	public LDMat set(LDAng ang) {
		identity();
		data[0] = ang.cos() + ang.X() * ang.X() * (1 - ang.cos());
		data[1] = ang.Y() * ang.X() * (1 - ang.cos()) + ang.Z() * ang.sin();
		data[2] = ang.X() * ang.Z() * (1 - ang.cos()) - ang.Y() * ang.sin();

		data[4] = ang.X() * ang.Y() * (1 - ang.cos()) - ang.Z() * ang.sin();
		data[5] = ang.cos() + ang.Y() * ang.Y() * (1 - ang.cos());
		data[6] = ang.Y() * ang.Z() * (1 - ang.cos()) + ang.X() * ang.sin();

		data[8] = ang.X() * ang.Z() * (1 - ang.cos()) + ang.Y() * ang.sin();
		data[9] = ang.Y() * ang.Z() * (1 - ang.cos()) - ang.X() * ang.sin();
		data[10] = ang.cos() + ang.Z() * ang.Z() * (1 - ang.cos());

		data[15] = 1.0f;
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
	public LDMat set(LDVec vecA, LDVec vecB, LDVec vecC) {
		LDVec z = new LDVec(vecB).negTranslate(vecA);
		LDVec y = z.cross(new LDVec(vecC).negTranslate(vecA));
		LDVec x = y.cross(z).normalise();
		y.normalise();
		z.normalise();

		set(new float[]{
			x.X(), x.Y(), x.Z(), 0.0f,
			y.X(), y.Y(), y.Z(), 0.0f,
			z.X(), z.Y(), z.Z(), 0.0f,
			0.0f, 0.0f, 0.0f, 1.0f
		}, true);
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
	public LDMat set(LDVec[] vecs) {
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
	 * matrix given as a 16 float array. The matrix given should be a special
	 * orthogonal affine matrix.
	 *
	 * @param matrix The matrix
	 *
	 * @return This
	 */
	public LDMat set(float[] matrix) {
		return set(matrix, false);
	}

	/**
	 * Sets the reference frame to be a copy of the provided reference frame.
	 *
	 * @param ref The frame to copy
	 *
	 * @return This
	 */
	public LDMat set(LDMat ref) {
		return set(ref.data, ref.affine);
	}

	/**
	 * Returns a copy of this reference frame in a <code>HDRef</code> object.
	 *
	 * @return A <code>HDRef</code> copy of this.
	 */
	public HDMat toHD() {
		double[] d = new double[16];
		for (int i = 0; i < data.length; i++) {
			d[i] = data[i];
		}
		return new HDMat(d, affine);
	}

	/**
	 * Returns a copy of this reference frame in a <code>HDQRef</code> object.
	 *
	 * @return A <code>HDQRef</code> copy of this.
	 */
	public HDQMat toHDQ() {
		// TODO: HDQMat
		throw new UnsupportedOperationException("NYI");
	}

	/**
	 * Returns a copy of this reference frame.
	 *
	 * @return A copy of this.
	 */
	public LDMat toLD() {
		return new LDMat(this);
	}

	/**
	 * Returns the transformation matrix of this reference frame in the format
	 * "{{0,4,8,12}\\n{1,5,9,13}\\n{2,6,10,14}\\n{3,7,11,15}}".
	 *
	 * @return The transformation matrix in text.
	 */
	@Override
	public String toString() {
		return "{{" + data[0] + "," + data[4] + "," + data[8] + "," + data[12] +
					 "}\n" +
					 " {" + data[1] + "," + data[5] + "," + data[9] + "," + data[13] +
					 "}\n" +
					 " {" + data[2] + "," + data[6] + "," + data[10] + "," + data[14] +
					 "}\n" +
					 " {" + data[3] + "," + data[7] + "," + data[11] + "," + data[15] +
					 "}}";
	}

	/**
	 * Returns the vector transformed to inside this reference frame. The position
	 * flag should be true if the vector is a position vector.
	 *
	 * @param vec The global vector
	 *
	 * @return The local vector
	 */
	public LDVec transform(LDVec vec) {
		return new LDVec(
						data[0] * vec.data[0] + data[4] * vec.data[1] +
						data[8] * vec.data[2] + data[12] * vec.data[3],
						data[1] * vec.data[0] + data[5] * vec.data[1] +
						data[9] * vec.data[2] + data[13] * vec.data[3],
						data[2] * vec.data[0] + data[6] * vec.data[1] +
						data[10] * vec.data[2] + data[14] * vec.data[3],
						data[3] * vec.data[0] + data[7] * vec.data[1] +
						data[11] * vec.data[2] + data[15] * vec.data[3]);
	}

	/**
	 * Returns the angle transformed to inside this reference frame.
	 *
	 * @param ang The global angle
	 *
	 * @return The local angle
	 */
	public LDAng transform(LDAng ang) {
		return ang.set(transform(ang.vec));
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
	public LDMat translateGlobally(LDVec vec) {
		if (affine) {
			data[12] += data[0] * vec.data[0] +
									data[4] * vec.data[1] +
									data[8] * vec.data[2];
			data[13] += data[1] * vec.data[0] +
									data[5] * vec.data[1] +
									data[9] * vec.data[2];
			data[14] += data[2] * vec.data[0] +
									data[6] * vec.data[1] +
									data[10] * vec.data[2];
		} else {
			LDMat.mult(this, new LDMat(vec), this);
		}
		modified = true;
		return this;
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
	public LDMat translateLocally(LDVec vec) {
		if (affine) {
			data[12] += vec.data[0];
			data[13] += vec.data[1];
			data[14] += vec.data[2];
			modified = true;
		} else {
			LDMat.mult(new LDMat(vec), this, this);
		}
		return this;
	}

	/**
	 * Writes the 16 float matrix in major-column format into the byte buffer,
	 * then returns the provided buffer.
	 *
	 * @param bb The Buffer to write to
	 *
	 * @return The Buffer written to
	 */
	public ByteBuffer write(ByteBuffer bb) {
		// This is not the way it is likely to actually be used.
		for (float d : data) {
			bb.putFloat(d);
		}
		return bb;
	}

	/**
	 * Writes the 16 float matrix in major-column format into the byte buffer,
	 * then returns the provided buffer.
	 *
	 * @param fb The float buffer to write to.
	 *
	 * @return The float buffer written to.
	 */
	public FloatBuffer write(FloatBuffer fb) {
		fb.put(data);
		return fb;
	}

}
