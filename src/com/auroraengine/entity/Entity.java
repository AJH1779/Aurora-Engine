package com.auroraengine.entity;

import com.auroraengine.debug.AuroraLogs;
import java.util.logging.Logger;
import plan.auroraengine.world.LocalPosition;
import plan.auroraengine.world.LocalVelocity;

/**
 *
 * @author Arthur
 */
public class Entity {
	private static final Logger LOG = AuroraLogs.getLogger(Entity.class);

	// Needs to hold or extend some kind of position object.

	public Entity(LocalPosition pos) {
		this.pos = new LocalPosition(pos);
		this.prev_pos = new LocalPosition(pos);
		this.mom = new LocalVelocity();
		this.prev_mom = new LocalVelocity();
	}
	public Entity(LocalPosition pos, LocalVelocity mom) {
		this.pos = new LocalPosition(pos);
		this.prev_pos = new LocalPosition(pos);
		this.mom = new LocalVelocity(mom);
		this.prev_mom = new LocalVelocity(mom);
	}
	private final LocalPosition pos, prev_pos;
	private final LocalVelocity mom, prev_mom;
}
