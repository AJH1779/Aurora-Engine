/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plan.auroraengine.world;

import com.auroraengine.debug.AuroraLogs;
import com.auroraengine.math.LDRef;
import com.auroraengine.math.LDVec;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * Denotes the area boundary between two local regions. This assumes that the
 * two regions are simple and contain no concave surfaces.
 *
 * @author Arthur
 */
public class LocalBoundary {
	private static final Logger LOG = AuroraLogs.getLogger(LocalBoundary.class);

	private LDVec[] corners;
	private final LDVec normal = new LDVec();
	private LocalRegion before, beyond;
	private final LocalBoundary inverse;
	private final LDRef tobeyond = new LDRef();

	/**
	 * Creates a new boundary with the specified corners. Note that these should
	 * form an approximately flat plane.
	 * @param corners
	 */
	public LocalBoundary(LDVec[] corners) {
		setCorners(corners);
		inverse = new LocalBoundary(this);
	}

	private LocalBoundary(LocalBoundary bound) {
		corners = new LDVec[bound.corners.length];
		for(int i = 0; i < corners.length; i++) {
			corners[i] = bound.corners[i].toLD();
		}
		Collections.reverse(Arrays.asList(corners));

		this.inverse = bound;
	}

	/**
	 * Sets the region which is "before" the boundary. This is the region that
	 * sees the "front" of the boundary.
	 * @param before
	 * @param beyond
	 */
	public void setRegions(LocalRegion before, LocalRegion beyond) {
		this.before = before;
		inverse.beyond = before;
		this.beyond = beyond;
		inverse.before = beyond;

		// Create the transformation matrices
	}
	/**
	 * Returns the corners of the boundary.
	 * @return
	 */
	public LDVec[] getCorners() {
		LDVec[] vecs = new LDVec[corners.length];
		for(int i = 0; i < vecs.length; i++) {
			vecs[i] = corners[i].toLD();
		}
		return vecs;
	}
	/**
	 * Returns the vector normal to the plane, facing away from the "front" of
	 * the boundary into the "before" boundary.
	 * @return
	 */
	public LDVec getNormalVector() {
		return normal.toLD();
	}
	/**
	 * Sets the corners of the boundary, should be flat.
	 * @param corners
	 */
	public final void setCorners(LDVec[] corners) {
		if(corners.length < 3) {
			throw new IllegalArgumentException("Not enough corners to form a boundary!");
		}
		this.corners = new LDVec[corners.length];
		for (int i = 0; i < corners.length; i++) {
			this.corners[i] = new LDVec(corners[i].toLD());
		}
		// TODO: Verify the integrity of this cross product. The corners should
		// always denote a flat surface to within an error amount.
		normal.set(corners[1].toLD().negTranslate(corners[0])
				.cross(corners[corners.length - 1].toLD().negTranslate(corners[0])));
	}
	/**
	 * Returns the inverse of this boundary.
	 * @return
	 */
	public LocalBoundary getInverse() {
		return inverse;
	}
	/**
	 * Gets the region before the boundary.
	 * @return
	 */
	public LocalRegion getBefore() {
		return before;
	}
	/**
	 * Gets the region beyond the boundary.
	 * @return
	 */
	public LocalRegion getBeyond() {
		return beyond;
	}
	/**
	 * Returns true if the point is before the boundary, false otherwise.
	 * @param p
	 * @return
	 */
	public boolean isBefore(LDVec p) {
		return normal.dot(p.toLD().negTranslate(corners[0])) >= 0f;
	}
	/**
	 * Returns true if the point is after the boundary, false otherwise.
	 * @param p
	 * @return
	 */
	public boolean isBeyond(LDVec p) {
		return !isBefore(p);
	}

	/**
	 * Moves the provided reference frame to beyond the boundary.
	 * @param r
	 * @return
	 */
	public LDRef toBeyond(LDRef r) {
		return r.multiplyGlobally(tobeyond);
	}
}
