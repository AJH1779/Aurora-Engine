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
package com.auroraengine.stat.damage;

import com.auroraengine.stat.LivingStats;

/**
 *
 * @author LittleRover
 */
public abstract class StatusEffect {
	public static final StatusEffect BLEED = new StatusEffect() {
	};
	public static final StatusEffect STUN = new StatusEffect() {
	};
	public static final StatusEffect PAIN = new StatusEffect() {
	};
	public static final StatusEffect WARM = new StatusEffect() {
	};
	public static final StatusEffect HOT = new StatusEffect() {
	};
	public static final StatusEffect COOKING = new StatusEffect() {
	};
	public static final StatusEffect BURNING = new StatusEffect() {
	};
	public static final StatusEffect INCINERATING = new StatusEffect() {
	};
	public static final StatusEffect CHILLED = new StatusEffect() {
	};
	public static final StatusEffect COLD = new StatusEffect() {
	};
	public static final StatusEffect FROSTY = new StatusEffect() {
	};
	public static final StatusEffect FROZEN = new StatusEffect() {
	};
	public static final StatusEffect CRYOGENIC = new StatusEffect() {
	};
	public static final StatusEffect REGENERATING = new StatusEffect() {
	};
	public static final StatusEffect HASTENED = new StatusEffect() {
	};
	public static final StatusEffect MENDING = new StatusEffect() {
	};
	public static final StatusEffect CORRUPTING = new StatusEffect() {
	};
	public static final StatusEffect PURIFYING = new StatusEffect() {
	};
	public static final StatusEffect SHOCKED = new StatusEffect() {
	};
	public static final StatusEffect PARALYSED = new StatusEffect() {
	};
	public static final StatusEffect ACID_TINGLE = new StatusEffect() {
	};
	public static final StatusEffect ACID_BURN = new StatusEffect() {
	};
	public static final StatusEffect ACID_SIZZLE = new StatusEffect() {
	};
	public static final StatusEffect MIND_CONTROLLED = new StatusEffect() {
	};

	/**
	 * Called on every tick of the status effect. This is called before checking
	 * whether the effect has been resisted if it can be resisted. The packet
	 * delivered is for things like poison primarily, and will typically return
	 * null;
	 *
	 * @return
	 */
	public DamagePacket onTick() {
		return null;
	}
}
