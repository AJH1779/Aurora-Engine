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
import java.util.HashMap;
import java.util.logging.Logger;

/**
 *
 * @author LittleRover
 */
public class LivingStats {

	private static final Logger LOG = AuroraLogs.getLogger(LivingStats.class
					.getName());

	public LivingStats() {
		for (MajorStatType type : MajorStatType.values()) {
			major_stats.put(type, new MajorStat(type));
		}
	}
	private int chp = 2;
	private int chp_max = 2;
	private int hp = 4;
	private int hp_max = 4;
	private int hp_max_max = 4;
	private int hp_per_chp = 2;
	private final HashMap<MajorStatType, MajorStat> major_stats = new HashMap<>(
					MajorStatType.values().length);
	private int mp = 2;
	private int mp_max = 2;
	private int sp = 4;
	private int sp_max = 4;
	private int sp_max_max = 4;
	private int sp_per_chp = 2;

	public int getCHP() {
		return chp;
	}

	public int getCHPMax() {
		return chp_max;
	}

	public int getHP() {
		return hp;
	}

	public int getHPMax() {
		return hp_max;
	}

	public int getHPMaxMax() {
		return hp_max_max;
	}

	public int getHPperCHP() {
		return hp_per_chp;
	}

	public int getMP() {
		return mp;
	}

	public int getMPMax() {
		return mp_max;
	}

	public MajorStat getMajorStat(MajorStatType t) {
		return major_stats.get(t);
	}

	public int getSP() {
		return sp;
	}

	public int getSPMax() {
		return sp_max;
	}

	public int getSPMaxMax() {
		return sp_max_max;
	}

	public int getSPperCHP() {
		return sp_per_chp;
	}

	public boolean hasAbility(Ability a) {
		return major_stats.get(a.getType()).containsAbility(a);
	}

}
