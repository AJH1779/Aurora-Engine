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
package com.auroraengine.creature.body;

import com.auroraengine.creature.body.genetics.Genome;
import com.auroraengine.debug.AuroraLogs;
import java.util.logging.Logger;

/**
 * One of the three parts of the creature. It commands STRENGTH, ENDURANCE, and
 * AGILITY. Generally the body has a bunch of other stuff going on with it but
 * for the sake of a game, it is made much more simple using the concept of
 * bonuses and buffs.
 *
 * @author LittleRover
 */
public class Body {
	private static final Logger LOG = AuroraLogs.getLogger(Body.class.getName());

	// TODO: Implement the better body from another project.
	private Body() {
		// TODO: Creature relation?
	}
	private int agility;
	private int endurance;
	private Genome genome;
	private int strength;

}
