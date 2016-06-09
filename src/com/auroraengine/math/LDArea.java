package com.auroraengine.math;

import java.util.logging.Logger;


public class LDArea {
	private static final Logger LOG = Logger.getLogger(LDArea.class.getName());

	private final LDVec vecs[] = new LDVec[3],
			edges[] = new LDVec[2], cross;
	public LDArea(LDVec[] vecs) {
		if(vecs == null) {
			throw new NullPointerException("Null Vector Array!");
		}
		if(vecs.length != 3) {
			throw new IllegalArgumentException("Array Must be of Length 3!");
		}
		for(int i = 0; i < vecs.length; i++) {
			this.vecs[i] = vecs[i].toLD();
		}
		edges[0] = vecs[1].toLD().negTranslate(vecs[0]);
		edges[1] = vecs[2].toLD().negTranslate(vecs[0]);
		cross = edges[0].cross(edges[1]);
	}

	boolean isBeyond(LDVec v) {
		return v.dot(cross) < 0;
	}
}
