/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plan.auroraengine.world;

import com.auroraengine.debug.AuroraLogs;
import com.auroraengine.math.LDRef;
import java.util.logging.Logger;

/**
 *
 * @author Arthur
 */
public class LocalPosition {

	private static final Logger LOG = AuroraLogs.getLogger(LocalPosition.class);

	public LocalPosition(LocalRegion reg, LDRef ref) {
		this.reg = reg;
		this.ref = ref.toLD();
		reg.transform(this);
	}

	public LocalPosition(LocalPosition pos) {
		this.reg = pos.reg;
		this.ref = pos.ref.toLD();
	}
	private LocalRegion reg;
	private final LDRef ref;

	public LDRef getPosition() {
		return ref;
	}

	public LocalRegion getRegion() {
		return reg;
	}

	public void setRegion(LocalRegion reg) {
		this.reg = reg;
	}
}
