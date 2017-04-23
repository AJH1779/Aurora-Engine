/*
 * Copyright (C) 2017 LittleRover
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.auroraengine.creature.body.genetics;

import java.util.logging.Logger;

/**
 * The Genome consists of Chromosomes and has one Genome per Creature
 * essentially. TODO: This has been done before, find the better version and
 * mesh it in.
 *
 * @author LittleRover
 */
public class Genome {
	private static final Logger LOG = Logger.getLogger(Genome.class.getName());
	private Chromosome[][] chromos;

	// TODO: Replace the genome with the separate genomes system.
	/*
	Instead of a single genome, will use a species genome which is flexible and a
	personal genome.
	 */
}
