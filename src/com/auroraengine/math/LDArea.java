package com.auroraengine.math;

import java.util.logging.Logger;

public class LDArea implements Cloneable {

	private static final Logger LOG = Logger.getLogger(LDArea.class.getName());

	private final LDVec vecs[] = new LDVec[3],
			edges[] = new LDVec[2], cross;

	public LDArea() {
		this(new LDVec[] {
			new LDVec(0.0f, 0.0f, 0.0f),
			new LDVec(1.0f, 0.0f, 0.0f),
			new LDVec(0.0f, 1.0f, 0.0f)
		});
	}

	public LDArea(LDVec[] vecs) {
		if (vecs == null) {
			throw new NullPointerException("Null Vector Array!");
		}
		if (vecs.length != 3) {
			throw new IllegalArgumentException("Array Must be of Length 3!");
		}
		for (int i = 0; i < vecs.length; i++) {
			this.vecs[i] = vecs[i].toLD();
		}
		edges[0] = vecs[1].toLD().negTranslate(vecs[0]);
		edges[1] = vecs[2].toLD().negTranslate(vecs[0]);
		cross = edges[0].cross(edges[1]);
	}

	public LDArea(LDArea area) {
		if (area == null) {
			throw new NullPointerException("Null Area Provided!");
		}
		for (int i = 0; i < vecs.length; i++) {
			vecs[i] = new LDVec(area.vecs[i]);
		}
		for (int i = 0; i < edges.length; i++) {
			edges[i] = new LDVec(area.edges[i]);
		}
		cross = new LDVec(area.cross);
	}

	boolean isBeyond(LDVec v) {
		return v.dot(cross) < 0;
	}

	/**
	 * Flips the effective direction of the area.
	 */
	public void flip() {
		edges[0].negate();
		edges[1].negate();
		LDVec edge = edges[0];
		edges[0] = edges[1];
		edges[1] = edge;
		cross.negate();
	}

	@Override
	public LDArea clone() throws CloneNotSupportedException {
		return new LDArea(this);
	}
}
