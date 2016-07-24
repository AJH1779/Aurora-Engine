/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plan.auroraengine.world;

import com.auroraengine.math.LDRef;

/**
 *
 * @author Arthur
 */
public class LocalVelocity {

	public LocalVelocity() {
		this.ref = new LDRef();
	}

	public LocalVelocity(LDRef ref) {
		this.ref = ref.toLD();
	}

	public LocalVelocity(LocalVelocity vel) {
		this.ref = vel.ref.toLD();
	}

	private final LDRef ref;
}
