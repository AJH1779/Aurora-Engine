package com.auroraengine.creature.body;

import com.auroraengine.creature.body.genetics.Genome;

/**
 * One of the three parts of the creature. It commands STRENGTH, ENDURANCE, and
 * AGILITY. Generally the body has a bunch of other stuff going on with it but
 * for the sake of a game, it is made much more simple using the concept of
 * bonuses and buffs.
 * @author Arthur
 */
public class Body {
	private Genome genome;

	// TODO - change this to something that allows ability slots more properly
	// Don't forget levelling
	// How do these things get improved? Picking them or by using them?
	private int strength, endurance, agility;
}
