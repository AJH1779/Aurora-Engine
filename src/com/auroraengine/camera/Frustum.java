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
package com.auroraengine.camera;

import com.auroraengine.debug.AuroraException;
import com.auroraengine.math.LDMat;
import com.auroraengine.math.geometry.Plane;
import com.auroraengine.math.geometry.Side;
import com.auroraengine.math.geometry.Volume;
import java.util.logging.Logger;

/**
 * An object representing the visible region of a view. May be used for cameras
 * and for AI checks of what is visible.
 *
 * @author LittleRover
 */
public final class Frustum {
	private static final int BOTTOM = 2;
	private static final int FAR = 5;
	private static final int LEFT = 0;
	private static final Logger LOG = Logger.getLogger(Frustum.class.getName());
	private static final int NEAR = 4;
	private static final int RIGHT = 1;
	private static final int TOP = 3;

	/**
	 * Creates an orthographic frustrum with the planes set by the specified
	 * planes.
	 *
	 * @param near
	 * @param far
	 * @param left
	 * @param right
	 * @param top
	 * @param bottom
	 *
	 * @throws com.auroraengine.debug.AuroraException
	 */
	public Frustum(float left, float right, float top,
								 float bottom, float near, float far)
					throws AuroraException {
		setFrustum(left, right, top, bottom, near, far);
	}

	/**
	 * Creates a perspective frustrum with the planes set by the arguments.
	 *
	 * @param fovy
	 * @param aspect
	 * @param near
	 * @param far
	 *
	 * @throws com.auroraengine.debug.AuroraException
	 */
	public Frustum(float fovy, float aspect, float near, float far)
					throws AuroraException {
		setFrustum(aspect, fovy, near, far);
	}

	private final float[][] coefficients = new float[4][2];
	private final float[] frustrum = new float[6];
	private final Plane[] frustrum_planes = {new Plane(), new Plane(), new Plane(),
																					 new Plane(), new Plane(), new Plane()};
	private final LDMat matrix = new LDMat();
	private boolean perspective;

	private void recalcCoefficients() {
		if (perspective) {
			float[] frustrum_sqr = new float[6];
			for (int i = 0; i < frustrum.length; i++) {
				frustrum_sqr[i] = frustrum[i] * frustrum[i];
			}

			float[] inv_lengths = new float[4];
			for (int i = 0; i < 4;
					 i++) {
				inv_lengths[i] = 1.0f /
												 ((float) Math.sqrt(frustrum_sqr[NEAR] +
																						frustrum_sqr[i]));
			}

			for (int i = 0; i < 4; i++) {
				coefficients[i][0] = (i == 0 || i == 3 ? -1 : 1) * frustrum[NEAR] *
														 inv_lengths[i];
				coefficients[i][1] = (i == 0 || i == 2 ? -1 : 1) * frustrum[i] *
														 inv_lengths[i];
			}

			matrix.set(new float[]{
				(2 * frustrum[NEAR]) / (frustrum[RIGHT] - frustrum[LEFT]),
				0.0f,
				0.0f,
				0.0f,
				//
				0.0f,
				(2 * frustrum[NEAR]) / (frustrum[TOP] - frustrum[BOTTOM]),
				0.0f,
				0.0f,
				//
				(frustrum[RIGHT] + frustrum[LEFT]) / (frustrum[RIGHT] - frustrum[LEFT]),
				(frustrum[TOP] + frustrum[BOTTOM]) / (frustrum[TOP] - frustrum[BOTTOM]),
				-(frustrum[FAR] + frustrum[NEAR]) / (frustrum[FAR] - frustrum[NEAR]),
				-(2 * frustrum[FAR] * frustrum[NEAR]) / (frustrum[FAR] - frustrum[NEAR]),
				//
				0.0f,
				0.0f,
				-1.0f,
				0.0f
			});
		} else {
			for (int i = 0; i < 4; i++) {
				coefficients[i][0] = (i == 0 || i == 2 ? 1 : -1);
				coefficients[i][1] = 0;
			}
			matrix.set(new float[]{
				2.0f / (frustrum[RIGHT] - frustrum[LEFT]),
				0.0f,
				0.0f,
				0.0f,
				//
				0.0f,
				2.0f / (frustrum[TOP] - frustrum[BOTTOM]),
				0.0f,
				0.0f,
				//
				(frustrum[RIGHT] + frustrum[LEFT]) / (frustrum[RIGHT] - frustrum[LEFT]),
				(frustrum[TOP] + frustrum[BOTTOM]) / (frustrum[TOP] - frustrum[BOTTOM]),
				-2.0f / (frustrum[FAR] - frustrum[NEAR]),
				-(2 * frustrum[FAR] * frustrum[NEAR]) / (frustrum[FAR] - frustrum[NEAR]),
				//
				0.0f,
				0.0f,
				-1.0f,
				1.0f
			});
		}
	}

	/**
	 * Returns a Side enum depending on whether the specified volume is fully
	 * inside, fully outside, or intersecting the frustrum.
	 *
	 * @param bound The volume to check.
	 *
	 * @return where the volume is relative to the frustrum.
	 */
	public Side contains(Volume bound) {
		// TODO: Order checks by where the volume was last rejected, optimisation.
		boolean across = false;
		for (Plane plane : frustrum_planes) {
			Side side = bound.getSideOf(plane);
			switch (side) {
				case INSIDE: {
					break;
				}
				case OUTSIDE: {
					return side;
				}
				case ACROSS: {
					across = true;
					break;
				}
			}
		}
		return across ? Side.ACROSS : Side.INSIDE;
	}

	/**
	 * Creates a new orthographic viewport using the provided factors.
	 *
	 * TODO: Returns an exception if the input is invalid.
	 *
	 * TODO: Revisit this Javadoc
	 *
	 * @param p_left   The left plane
	 * @param p_right  The right plane
	 * @param p_top    The top plane
	 * @param p_bottom The bottom plane
	 * @param p_near   The near plane
	 * @param p_far    The far plane
	 *
	 * @throws AuroraException Never Thrown
	 */
	public void setFrustum(float p_left, float p_right, float p_top,
												 float p_bottom, float p_near, float p_far)
					throws AuroraException {
		System.arraycopy(
						new float[]{p_left, p_right, p_top, p_bottom, p_near, p_far},
						0, frustrum, 0, frustrum.length);
	}

	/**
	 * Sets the properties of this frustrum to a perspective view described by the
	 * provided factors.
	 *
	 * @param p_fovy   The vertical field of view angle in degrees
	 * @param p_aspect The aspect ratio
	 * @param p_near   The near plane distance from camera
	 * @param p_far    The far plane distance from camera
	 *
	 * @throws AuroraException If the aspect ratio is NaN or +/- Infinity.
	 */
	public void setFrustum(float p_fovy, float p_aspect, float p_near,
												 float p_far)
					throws AuroraException {
		if (Float.isNaN(p_aspect) || Float.isInfinite(p_aspect)) {
			throw new AuroraException(
							"The aspect ratio of the frustrum is invalid: " +
							p_aspect);
		}
		perspective = true;
		float height = ((float) Math.tan(p_fovy * 0.5f)) * p_near;
		float width = height * p_aspect;
		System.arraycopy(new float[]{-width, width, -height, height, p_near, p_far},
										 0, frustrum, 0, frustrum.length);
		recalcCoefficients();
	}

}
