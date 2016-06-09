package com.auroraengine.math;

import java.util.logging.Logger;

public class LDVolume {
	private static final Logger LOG = Logger.getLogger(LDVolume.class.getName());
	private final LDArea[] sides = new LDArea[6];
	public LDVolume() {
		super();
	}

	public boolean isContained(LDVec v) {
		for(LDArea s : sides) {
			if(!s.isBeyond(v)) {
				return false;
			}
		}
		return true;
	}

}