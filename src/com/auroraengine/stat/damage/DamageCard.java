/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.stat.damage;

/**
 *
 * @author Arthur
 */
public class DamageCard {
	public DamageCard(ElementType type) {

	}

	private int hp_dmg;
	private int sp_dmg;
	private int chp_dmg;

	public int getHPDamage() {
		return hp_dmg;
	}
	public void setHPDamage(int dmg) {
		hp_dmg = dmg;
	}
	public int getSPDamage() {
		return sp_dmg;
	}
	public void setSPDamage(int dmg) {
		sp_dmg = dmg;
	}
	public int getCHPDamage() {
		return chp_dmg;
	}
	public void setCHPDamage(int dmg) {
		chp_dmg = dmg;
	}
}
