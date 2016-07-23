/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.stat;

/**
 *
 * @author Arthur
 */
public abstract class Ability {

	public Ability(MajorStatType type) {
		if(type == null) {
			throw new NullPointerException("Type Must Not Be Null!");
		}
		this.type = type;
	}

	private final MajorStatType type;

	public MajorStatType getType() {
		return type;
	}

	public boolean isCompatible(MajorStatType t) {
		return t == null || t == this.type;
	}
}
