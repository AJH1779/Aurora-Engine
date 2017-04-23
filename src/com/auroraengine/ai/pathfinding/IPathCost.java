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
package com.auroraengine.ai.pathfinding;

/**
 *
 * @author LittleRover
 */
public interface IPathCost {
	/**
	 * Adds the provided path cost to this path cost.
	 *
	 * @param cost
	 *
	 * @return
	 */
	public IPathCost add(IPathCost cost);

	/**
	 * Minus the provided path cost from this path cost.
	 *
	 * @param cost
	 *
	 * @return
	 */
	public IPathCost minus(IPathCost cost);

	/**
	 * Returns true if the this path cost is less than the provided path cost.
	 *
	 * @param cost
	 *
	 * @return
	 */
	public boolean isLessThan(IPathCost cost);

	/**
	 * Returns true if this path cost is greater than the provided path cost.
	 *
	 * @param cost
	 *
	 * @return
	 */
	public boolean isGreaterThan(IPathCost cost);

	/**
	 * Returns true if this path cost is equal to the provided path cost.
	 *
	 * @param cost
	 *
	 * @return
	 */
	public boolean isEqualTo(IPathCost cost);

	/**
	 * Returns 1 if the path is positive in cost, -1 if negative in cost, and 0 if
	 * zero in cost.
	 *
	 * @return
	 */
	public int signum();
}
