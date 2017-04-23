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
package com.auroraengine.map.grid;

import com.auroraengine.debug.AuroraLogs;
import java.util.function.BiFunction;
import java.util.logging.Logger;

/**
 * A generic grid element object contained within a Grid object which allows for
 * the grid elements themselves to be linked relative to each other.
 */
public class GridElement {
	private static final Logger LOG = AuroraLogs.getLogger(GridElement.class
					.getName());

	/**
	 * Creates a grid element linked to the specified grid object.
	 *
	 * @param grid
	 */
	public GridElement(Grid<GridElement> grid) {
		if (grid == null) {
			throw new NullPointerException("Grid must not be null!");
		}
		this.grid = grid;
		position = new int[grid.getDimension()];
	}
	private final Grid<GridElement> grid;
	private boolean isnullposition = true;
	private final int[] position;

	/**
	 * Returns the dimensional space occupied by this grid element.
	 *
	 * @return The grid dimension
	 */
	public int getDimension() {
		return position.length;
	}

	/**
	 * Returns the grid this element is a part of.
	 *
	 * @return
	 */
	public Grid<GridElement> getGrid() {
		return grid;
	}

	/**
	 * Returns the element neighbouring this one by the specified offset.
	 *
	 * @param dp The offset
	 *
	 * @return The neighbour
	 */
	public GridElement getNeighbour(int[] dp) {
		return isnullposition ? null : grid.get(getPosition(dp));
	}

	/**
	 * Returns the position of this grid element within the parent grid.
	 *
	 * @return
	 */
	public int[] getPosition() {
		if (isnullposition) {
			return null;
		}
		int[] pos = new int[position.length];
		System.arraycopy(position, 0, pos, 0, pos.length);
		return pos;
	}

	/**
	 * Returns the position of this grid element within the parent grid.
	 *
	 * @param dp
	 *
	 * @return
	 */
	public int[] getPosition(int[] dp) {
		if (dp.length != position.length) {
			throw new IllegalArgumentException(
							"Input array is not of the correct dimension!");
		}
		if (isnullposition) {
			return null;
		}
		int[] pos = new int[position.length];
		for (int i = 0; i < pos.length; i++) {
			pos[i] = position[i] + dp[i];
		}
		return pos;
	}

	/**
	 * Sets the position of this object within the grid to null, removing it from
	 * the grid.
	 */
	public void setNullPosition() {
		this.isnullposition = true;
		grid.set(position, Grid::func_check_null, Grid::func_make_null);
	}

	/**
	 * Attempts to move this grid element to the new location using the provided
	 * check.
	 *
	 * @param p
	 * @param check
	 *
	 * @return
	 */
	public boolean setPosition(int[] p,
														 BiFunction<int[], GridElement, Boolean> check) {
		if (grid.set(p, check, (pos, prev) -> {
					 prev.isnullposition = true;
					 return this;
				 })) {
			isnullposition = false;
			System.arraycopy(p, 0, position, 0, p.length);
			return true;
		} else {
			return false;
		}
	}

}
