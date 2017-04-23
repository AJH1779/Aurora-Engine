package com.auroraengine.creature.body.genetics;

import com.auroraengine.debug.AuroraLogs;
import java.util.logging.Logger;

/**
 * The Chromosome is one of the parts of the genome which holds a set of Genes.
 * These are essentially a form of data structure and nothing more.
 *
 * @author LittleRover
 */
public class Chromosome {
	private static final Logger LOG = AuroraLogs.getLogger(Chromosome.class
					.getName());
	private Gene[] genes;

}
