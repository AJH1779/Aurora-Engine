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

import static java.lang.Math.abs;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A grid object
 *
 * @author LittleRover
 * @param <T>
 */
public class Grid<T> {

	private static final Logger LOG = AuroraLogs.getLogger(Grid.class.getName());

	public static <T> Boolean func_check_null(int[] p, T t) {
		return true;
	}

	public static <T> Boolean func_check_occupied(int[] p, T t) {
		return t != null;
	}

	public static <T> Boolean func_check_unoccupied(int[] p, T t) {
		return t == null;
	}

	public static <T> T func_make_null(int[] p, T t) {
		return null;
	}

	private final int[] position, offset, maximum;
	private final Object[] grid;

	/**
	 * Creates a new single element grid with the specified dimension. This is for
	 * testing only.
	 *
	 * @param dim
	 */
	public Grid(int dim) {
		super();
		position = new int[dim];
		offset = new int[dim];
		maximum = new int[dim];

		grid = new Object[1];
	}

	/**
	 * Creates a grid with the specified central position and maximum bounds. Note
	 * the actual size is 2*max+1 for each dimension so that there is a central
	 * focus.
	 *
	 * @param pos
	 * @param max
	 */
	public Grid(int[] pos, int[] max) {
		if (pos == null) {
			throw new NullPointerException("Position is Null!");
		}
		if (max == null) {
			throw new NullPointerException("Maximum is Null!");
		}
		if (pos.length != max.length) {
			throw new IllegalArgumentException("Array Lengths are Unequal!");
		}

		position = new int[pos.length];
		offset = new int[pos.length];
		maximum = new int[pos.length];
		System.arraycopy(pos, 0, position, 0, pos.length);
		System.arraycopy(max, 0, maximum, 0, pos.length);

		int length = 0;
		for (int m : maximum) {
			length += 2 * m + 1;
		}
		grid = new Object[length];
	}

	/**
	 * Returns the internal reference number for the specified grid coordinate.
	 *
	 * @param p The position coordinates
	 *
	 * @return The internal reference
	 */
	private int getRef(int[] p) {
		// Verify it is in bounds and a legal argument
		if (p.length != position.length) {
			LOG.log(Level.WARNING, "Invalid position submitted!");
			return -2;
		}
		for (int i = 0; i < position.length; i++) {
			if (abs(position[i] - p[i]) > maximum[i]) {
				return -1; // Outside of range, or get it from the list.
			}
		}
		int e = 0;
		for (int i = 0; i < position.length; i++) {
			// TODO: Verify
			e = e * (2 * maximum[i] + 1) + (maximum[i] + position[i] - p[i] +
																			offset[i]) % (2 * maximum[i] + 1);
		}
		return e;
	}

	/**
	 * Returns the grid object at the given coordinates.
	 *
	 * @param p
	 *
	 * @return
	 */
	public T get(int[] p) {
		int ref = getRef(p);
		if (ref < 0) {
			return null;
		}
		if (ref >= grid.length) {
			LOG.log(Level.WARNING,
							"Attempted to take an element from beyond the grid range! There is a problem here!");
			return null;
		}
		@SuppressWarnings("unchecked")
		T t = (T) grid[ref];
		return t;
	}

	/**
	 * Sets the target of the grid based on the check and make functions that are
	 * provided.
	 *
	 * @param p     The coordinates.
	 * @param check The check function
	 * @param make  The make function
	 *
	 * @return true if the element was set
	 */
	public boolean set(int[] p, BiFunction<int[], T, Boolean> check,
										 BiFunction<int[], T, T> make) {
		int ref = getRef(p);
		if (ref < 0) {
			return false;
		}
		if (ref >= grid.length) {
			LOG.log(Level.WARNING,
							"Attempted to take an element from beyond the grid range! There is a problem here!");
			return false;
		}
		@SuppressWarnings("unchecked")
		T t = (T) grid[ref];
		boolean b = check.apply(p, t);
		if (b) {
			grid[ref] = make.apply(p, t);
		}
		return b;
	}

	public void clear() {
		Arrays.fill(grid, null);
	}

	public boolean translate(int[] dp) {
		if (dp.length != position.length) {
			LOG.log(Level.WARNING, "Invalid position submitted!");
			return false;
		}
		boolean done = false;
		for (int i = 0; i < position.length; i++) {
			if (abs(dp[i]) > maximum[i]) {
				clear();
				for (int j = 0; j < position.length; j++) {
					position[j] += dp[j];
					offset[j] = 0;
				}
				done = true;
				break;
			}
		}
		if (!done) {
			for (int i = 0; i < position.length; i++) {

			}
			for (int i = 0; i < position.length; i++) {
				position[i] += dp[i];
				offset[i] = (offset[i] + dp[i]) % (2 * maximum[i] + 1);
			}
		}
		return done;
	}

	/**
	 * Returns the dimensions of the grid.
	 *
	 * @return
	 */
	public int getDimension() {
		return position.length;
	}
}
