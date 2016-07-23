/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.stat.damage;

import com.auroraengine.stat.LivingStats;

/**
 *
 * @author Arthur
 */
public abstract class StatusEffect {
	public static final StatusEffect BLEED = new StatusEffect() {};
	public static final StatusEffect STUN = new StatusEffect() {};
	public static final StatusEffect PAIN = new StatusEffect() {};
	public static final StatusEffect WARM = new StatusEffect() {};
	public static final StatusEffect HOT = new StatusEffect() {};
	public static final StatusEffect COOKING = new StatusEffect() {};
	public static final StatusEffect BURNING = new StatusEffect() {};
	public static final StatusEffect INCINERATING = new StatusEffect() {};
	public static final StatusEffect CHILLED = new StatusEffect() {};
	public static final StatusEffect COLD = new StatusEffect() {};
	public static final StatusEffect FROSTY = new StatusEffect() {};
	public static final StatusEffect FROZEN = new StatusEffect() {};
	public static final StatusEffect CRYOGENIC = new StatusEffect() {};
	public static final StatusEffect REGENERATING = new StatusEffect() {};
	public static final StatusEffect HASTENED = new StatusEffect() {};
	public static final StatusEffect MENDING = new StatusEffect() {};
	public static final StatusEffect CORRUPTING = new StatusEffect() {};
	public static final StatusEffect PURIFYING = new StatusEffect() {};
	public static final StatusEffect SHOCKED = new StatusEffect() {};
	public static final StatusEffect PARALYSED = new StatusEffect() {};
	public static final StatusEffect ACID_TINGLE = new StatusEffect() {};
	public static final StatusEffect ACID_BURN = new StatusEffect() {};
	public static final StatusEffect ACID_SIZZLE = new StatusEffect() {};
	public static final StatusEffect MIND_CONTROLLED = new StatusEffect() {};

	/**
	 * Called on every tick of the status effect. This is called before checking
	 * whether the effect has been resisted if it can be resisted. The packet
	 * delivered is for things like poison primarily, and will typically return
	 * null;
	 * @return
	 */
	public DamagePacket onTick() {
		return null;
	}
}
