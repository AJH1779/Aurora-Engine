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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author LittleRover
 */
public enum ElementType {
	SLASH(
					new StatusEffect[]{StatusEffect.BLEED},
					new CriticalHit[]{}),
	BLUNT(
					new StatusEffect[]{StatusEffect.STUN},
					new CriticalHit[]{}),
	PIERCE(
					new StatusEffect[]{StatusEffect.PAIN},
					new CriticalHit[]{}),
	FIRE(
					new StatusEffect[]{StatusEffect.WARM, StatusEffect.HOT,
														 StatusEffect.COOKING, StatusEffect.BURNING,
														 StatusEffect.INCINERATING},
					new CriticalHit[]{}),
	ICE(
					new StatusEffect[]{StatusEffect.CHILLED, StatusEffect.COLD,
														 StatusEffect.FROSTY, StatusEffect.FROZEN,
														 StatusEffect.CRYOGENIC},
					new CriticalHit[]{}),
	EARTH(
					new StatusEffect[]{},
					new CriticalHit[]{}),
	WATER(
					new StatusEffect[]{StatusEffect.REGENERATING},
					new CriticalHit[]{}),
	WIND(
					new StatusEffect[]{StatusEffect.HASTENED},
					new CriticalHit[]{}),
	LIFE(
					new StatusEffect[]{StatusEffect.MENDING},
					new CriticalHit[]{}),
	DARK(
					new StatusEffect[]{StatusEffect.CORRUPTING},
					new CriticalHit[]{}),
	LIGHT(
					new StatusEffect[]{StatusEffect.PURIFYING},
					new CriticalHit[]{}),
	ELECTRIC(
					new StatusEffect[]{StatusEffect.SHOCKED, StatusEffect.PARALYSED},
					new CriticalHit[]{}),
	TRANSMUTE(
					new StatusEffect[]{},
					new CriticalHit[]{}),
	ABSORPTION(
					new StatusEffect[]{},
					new CriticalHit[]{}),
	ACID(
					new StatusEffect[]{StatusEffect.ACID_TINGLE, StatusEffect.ACID_BURN,
														 StatusEffect.ACID_SIZZLE},
					new CriticalHit[]{}),
	GENETIC(
					new StatusEffect[]{},
					new CriticalHit[]{}),
	PSYCHIC(
					new StatusEffect[]{StatusEffect.MIND_CONTROLLED},
					new CriticalHit[]{});

	ElementType(StatusEffect[] effects, CriticalHit[] crits) {
		status_effects = Collections.unmodifiableList(Arrays.asList(effects));
		critical_hits = Collections.unmodifiableList(Arrays.asList(crits));
	}

	private final List<StatusEffect> status_effects;
	private final List<CriticalHit> critical_hits;

	public final List<StatusEffect> getStatusEffects() {
		return status_effects;
	}
}
