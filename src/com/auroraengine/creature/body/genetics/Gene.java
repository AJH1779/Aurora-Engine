package com.auroraengine.creature.body.genetics;

/**
 * These are activated at particular times and may have some effect. As a result
 * they are an interface and such.
 * @author Arthur
 */
public interface Gene {
	/**
	 * Returns a random gene that this one may mutate into. May return itself or
	 * null if the gene should be destroyed.
	 * @return
	 */
	public Gene mutate();
}
