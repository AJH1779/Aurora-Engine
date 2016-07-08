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
 * Denotes a 3D Space on the local level which may be directly interacted with.
 * @author Arthur
 */
public class LocalRegion {
	private static final Logger LOG = AuroraLogs.getLogger(LocalRegion.class);

	private final LocalRegion parent;
	private final LDRef toparent;
	// private final LocalBoundary[] bounds;

	public LocalRegion(MicroRegion parent, LDRef fromparent) {
		this.parent = parent;
		this.toparent = fromparent.invert();
	}

	public LocalPosition transform(LocalPosition ref) {
		/*
		for(LocalBoundary b : bounds) {
			if(b.isBeyond(ref.getPosition().getOrigin())) {
				b.toBeyond(ref.getPosition());
				ref.setRegion(b.getBeyond());
				return ref;
			}
		}*/
		return ref;
	}

	public LocalRegion getParent() {
		return parent;
	}

	public LocalPosition toParent(LocalPosition ref) {
		return new LocalPosition(parent, ref.getPosition().toLD().multiplyGlobally(toparent));
	}
}
