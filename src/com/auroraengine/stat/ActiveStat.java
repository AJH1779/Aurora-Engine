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
import java.util.logging.Logger;

/**
 * A stat type for things like health, mana, and stamina.
 *
 * @author LittleRover
 */
public class ActiveStat {
	private static final Logger LOG = AuroraLogs.getLogger(ActiveStat.class
					.getName());

	public ActiveStat(int val) {
		this(val, 0, 0, val, val);
	}

	public ActiveStat(int val, int max) {
		this(val, 0, 0, max, max);
	}

	public ActiveStat(int val, int max, int nat_max) {
		this(val, 0, 0, max, nat_max);
	}

	public ActiveStat(int val, int min, int nat_min, int max, int nat_max) {
		this.current_max = Math.max(min, max);
		this.current_min = Math.min(min, max);
		this.current_val = val;
		this.natural_max = Math.max(nat_min, nat_max);
		this.natural_min = Math.min(nat_min, nat_max);
	}
	private int current_max;
	private int current_min;
	private int current_val;
	private int natural_max;
	private int natural_min;

	public int getCurrent() {
		return current_val;
	}

	public float getFraction() {
		return (current_val - current_min) / (float) (current_max -
																									current_min);
	}

	public float getMaxFrac() {
		return (current_max - natural_min) / (float) (natural_max -
																									natural_min);
	}

	public int getMaximum() {
		return current_max;
	}

	public float getMinFrac() {
		return (current_min - natural_min) / (float) (natural_max -
																									natural_min);
	}

	public int getMinimum() {
		return current_min;
	}

	public float getNaturalFrac() {
		return (current_val - natural_min) / (float) (natural_max -
																									natural_min);
	}

	public int getNaturalMax() {
		return natural_max;
	}

	public int getNaturalMin() {
		return natural_min;
	}

	public void setCurrent(int val) {
		current_val = Math.max(current_min, Math.min(current_max, val));
	}

	public void setMaximum(int val) {
		current_max = val;
		if (current_min > current_max) {
			current_max = current_min;
		}
		setCurrent(current_val);
	}

	public void setMinimum(int val) {
		current_min = val;
		if (current_min > current_max) {
			current_max = current_min;
		}
		setCurrent(current_val);
	}

	public void setNaturalMax(int val) {
		this.natural_max = val;
	}

	public void setNaturalMin(int val) {
		this.natural_min = val;
	}

}
