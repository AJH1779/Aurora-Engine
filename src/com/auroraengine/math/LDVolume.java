package com.auroraengine.math;

import java.util.logging.Logger;

public class LDVolume implements Cloneable {
	private static final Logger LOG = Logger.getLogger(LDVolume.class.getName());
	private final LDArea[] sides = new LDArea[6];
	public LDVolume() {

	}
	public LDVolume(LDVolume vol) {
		for(int i = 0; i < sides.length; i++) {
			if(vol.sides[i] != null) {
				sides[i] = new LDArea(vol.sides[i]);
			}
		}
	}

	public boolean isContained(LDVec v) {
		for(LDArea s : sides) {
			if(!s.isBeyond(v)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public LDVolume clone() {
		return new LDVolume(this);
	}
}