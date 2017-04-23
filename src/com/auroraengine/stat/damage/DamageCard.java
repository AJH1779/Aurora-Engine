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

import com.auroraengine.debug.AuroraLogs;
import java.util.logging.Logger;

/**
 *
 * @author LittleRover
 */
public class DamageCard {
	private static final Logger LOG = AuroraLogs.getLogger(DamageCard.class
					.getName());

	public DamageCard(ElementType type) {

	}
	private int chp_dmg;
	private int hp_dmg;
	private int sp_dmg;

	public int getCHPDamage() {
		return chp_dmg;
	}

	public int getHPDamage() {
		return hp_dmg;
	}

	public int getSPDamage() {
		return sp_dmg;
	}

	public void setCHPDamage(int dmg) {
		chp_dmg = dmg;
	}

	public void setHPDamage(int dmg) {
		hp_dmg = dmg;
	}

	public void setSPDamage(int dmg) {
		sp_dmg = dmg;
	}
}
