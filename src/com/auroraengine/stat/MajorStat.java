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
package com.auroraengine.stat;

import com.auroraengine.debug.AuroraLogs;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * This contains the properties of the major stats in the game as given in
 * MajorStatType.
 *
 * @author LittleRover
 */
public class MajorStat {
	private static final int BASE_STAT_VALUE = 2;
	private static final Logger LOG = AuroraLogs.getLogger(MajorStat.class
					.getName());
	private static final int MAX_STAT_VALUE = 30;

	/**
	 * Creates a new stat of the provided type
	 *
	 * @param type
	 */
	public MajorStat(MajorStatType type) {
		if (type == null) {
			throw new NullPointerException("Stat Type is Null!");
		}
		this.type = type;
	}
	private final ArrayList<Ability> abilities = new ArrayList<>(Arrays.asList(
					new Ability[MAX_STAT_VALUE + BASE_STAT_VALUE]));
	private final MajorStatType type;
	private int value = 0;

	/**
	 * Adds the given ability to the first available slot, returning true only if
	 * a space existed for it to be placed in.
	 *
	 * @param a The ability to add
	 *
	 * @return True if added
	 */
	public boolean addAbility(Ability a) {
		if (!a.isCompatible(type)) {
			return false;
		}
		int i = abilities.indexOf(null);
		if (i == -1 || abilities.contains(a)) {
			return false;
		} else {
			abilities.set(i, a);
			return true;
		}
	}

	/**
	 * Returns true if the abilities currently contain the specified ability.
	 *
	 * @param a The ability to check
	 *
	 * @return True if contained
	 */
	public boolean containsAbility(Ability a) {
		return abilities.contains(a);
	}

	/**
	 * Returns a stream of the currently active abilities, which is the value plus
	 * BASE_STAT_VALUE, by default 2.
	 *
	 * @return
	 */
	public Stream<Ability> getAbilities() {
		return abilities.stream();
	}

	/**
	 * Returns the ability in the given slot, or null if there is not one.
	 *
	 * @param i The slot id.
	 *
	 * @return The ability there.
	 */
	public Ability getAbility(int i) {
		return i < 0 || i >= abilities.size() ? null : abilities.get(i);
	}

	/**
	 * Returns a stream of the currently active abilities, which is the value plus
	 * BASE_STAT_VALUE, by default 2.
	 *
	 * @return
	 */
	public Stream<Ability> getActiveAbilities() {
		return abilities.stream().limit(value + BASE_STAT_VALUE);
	}

	/**
	 * Returns the type of the stat.
	 *
	 * @return
	 */
	public MajorStatType getStatType() {
		return type;
	}

	/**
	 * Returns the value of the stat.
	 *
	 * @return The current value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Removes the provided ability from the list, returning true if it was found
	 * and removed.
	 *
	 * @param a The ability to remove
	 *
	 * @return True if removed
	 */
	public boolean removeAbility(Ability a) {
		int i = abilities.indexOf(a);
		if (i != -1) {
			abilities.set(i, a);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Sets the ability in the specified slot to the provided slot, returning the
	 * ability already located there or null if the slot was empty. Returns the
	 * ability being placed if the ability is already included here.
	 *
	 * @param a The ability to add
	 * @param i The place to add it
	 *
	 * @return The ability already there
	 */
	public Ability setAbility(Ability a, int i) {
		return (a.isCompatible(type) && (abilities.contains(a) || i < 0 || i >=
																																			 abilities
																																							 .size())) ?
					 a : abilities.set(i, a);
	}

	/**
	 * Sets the value of the stat. This is bounded between 0 and MAX_STAT_VALUE
	 * which is by default 30. Returns the modified value.
	 *
	 * @param val The value to set as
	 *
	 * @return The new value
	 */
	public int setValue(int val) {
		value = Math.min(MAX_STAT_VALUE, Math.max(0, val));
		return value;
	}
}
