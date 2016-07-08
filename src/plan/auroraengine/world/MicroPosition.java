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
public class MicroPosition extends LocalPosition {
	private static final Logger LOG = AuroraLogs.getLogger(MicroPosition.class);
	
	public MicroPosition(MicroRegion reg, LDRef ref) {
		super(reg, ref);
	}
}
