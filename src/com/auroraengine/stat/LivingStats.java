/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.stat;

import com.auroraengine.debug.AuroraLogs;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 *
 * @author Arthur
 */
public class LivingStats {

	private static final Logger LOG = AuroraLogs.getLogger(LivingStats.class);

	public LivingStats() {
		for (MajorStatType type : MajorStatType.values()) {
			major_stats.put(type, new MajorStat(type));
		}
	}

	private int hp = 4, hp_max = 4, hp_max_max = 4;
	private int sp = 4, sp_max = 4, sp_max_max = 4;
	private int chp = 2, chp_max = 2;
	private int hp_per_chp = 2;
	private int sp_per_chp = 2;
	private int mp = 2, mp_max = 2;

	private final HashMap<MajorStatType, MajorStat> major_stats = new HashMap<>(MajorStatType.values().length);

	public int getHP() {
		return hp;
	}

	public int getHPMax() {
		return hp_max;
	}

	public int getHPMaxMax() {
		return hp_max_max;
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

	public int getCHP() {
		return chp;
	}

	public int getCHPMax() {
		return chp_max;
	}

	public int getMP() {
		return mp;
	}

	public int getMPMax() {
		return mp_max;
	}

	public int getHPperCHP() {
		return hp_per_chp;
	}

	public int getSPperCHP() {
		return sp_per_chp;
	}

	public MajorStat getMajorStat(MajorStatType t) {
		return major_stats.get(t);
	}

	public boolean hasAbility(Ability a) {
		return major_stats.get(a.getType()).containsAbility(a);
	}

}
