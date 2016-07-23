/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.stat.damage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Arthur
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
			new StatusEffect[]{StatusEffect.WARM, StatusEffect.HOT, StatusEffect.COOKING, StatusEffect.BURNING, StatusEffect.INCINERATING},
			new CriticalHit[]{}),
	ICE(
			new StatusEffect[]{StatusEffect.CHILLED, StatusEffect.COLD, StatusEffect.FROSTY, StatusEffect.FROZEN, StatusEffect.CRYOGENIC},
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
			new StatusEffect[]{StatusEffect.ACID_TINGLE, StatusEffect.ACID_BURN, StatusEffect.ACID_SIZZLE},
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
