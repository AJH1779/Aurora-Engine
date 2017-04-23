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

import java.util.logging.Logger;

public class DivisibleGridElement extends GridElement {
	private static final Logger LOG = Logger.getLogger(DivisibleGridElement.class
					.getName());

	public DivisibleGridElement(Grid<GridElement> grid) {
		super(grid);
		parent = null;
		children = new DivisibleGridElement[1 << grid.getDimension()];
		scale = 0;
	}

	public DivisibleGridElement(DivisibleGridElement parent, int[] pos) {
		super(parent.getGrid());
		this.parent = parent;
		this.children = new DivisibleGridElement[1 << getGrid().getDimension()];
		this.scale = parent.scale + 1;
		this.setPosition(pos, Grid::func_check_unoccupied);
	}
	private final DivisibleGridElement[] children;
	private DivisibleGridElement parent;
	private final int scale;

	public DivisibleGridElement getParent() {
		return parent;
	}

	public int getScale() {
		return scale;
	}
}
