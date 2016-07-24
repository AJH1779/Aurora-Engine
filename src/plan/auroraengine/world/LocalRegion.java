/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plan.auroraengine.world;

import com.auroraengine.camera.Camera;
import com.auroraengine.camera.Frustrum;
import com.auroraengine.debug.AuroraLogs;
import com.auroraengine.math.LDRef;
import com.auroraengine.opengl.viewport.Viewport;
import java.util.HashSet;
import java.util.logging.Logger;

/**
 * Denotes a 3D volume of space which is on the scale of meters.
 * @author Arthur
 */
public class LocalRegion {
	private static final Logger LOG = AuroraLogs.getLogger(LocalRegion.class);

	private final LocalRegion parent;
	private final LDRef toparent;
	private final HashSet<LocalRegion> children;
	// private final LocalBoundary[] bounds;

	public LocalRegion(MicroRegion parent, LDRef fromparent) {
		this.parent = parent;
		this.toparent = fromparent.invert();
		this.children = new HashSet<>();
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

	public void render(Camera camera, Viewport view, Frustrum frustrum) {
		
		// TODO: Make the drawing work properly.
		// Draw terrain first as it is the most simple geometry typically
		
	}
}
