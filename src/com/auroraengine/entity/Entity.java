package com.auroraengine.entity;

import com.auroraengine.debug.AuroraLogs;
import com.auroraengine.world.LocalPosition;
import java.util.logging.Logger;

/**
 *
 * @author Arthur
 */
public abstract class Entity {
	private static final Logger LOG = AuroraLogs.getLogger(Entity.class);

	// Needs to hold or extend some kind of position object.

	public Entity(LocalPosition pos) {
		this.pos = new LocalPosition(pos);
		this.prev_pos = new LocalPosition(pos);
	}
	private final LocalPosition pos, prev_pos;

	public abstract void update(double delt);

	public LocalPosition getEstimatedPosition(double delt) {
		LocalPosition est = new LocalPosition(prev_pos);

		

		return est;
	}

	public LocalPosition getPreviousPosition() {
		return new LocalPosition(prev_pos);
	}
}
