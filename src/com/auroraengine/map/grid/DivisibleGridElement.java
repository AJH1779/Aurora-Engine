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
import java.util.logging.Logger;

/**
 * An element of a grid that can be subdivided into a 2^(DIM) set of
 * sub-elements.
 *
 * @author Arthur
 */
public class DivisibleGridElement extends GridElement {
	private static final Logger LOG = AuroraLogs.getLogger(
					DivisibleGridElement.class.getName());

	/**
	 * Creates a new element contained in the specified grid.
	 *
	 * @param grid The parent grid
	 */
	public DivisibleGridElement(Grid<GridElement> grid) {
		super(grid);
		parent = null;
		children = new DivisibleGridElement[1 << grid.getDimension()];
		scale = 0;
	}

	/**
	 * Creates a new element contained in the specified parent element.
	 *
	 * // TODO: An integer position array?
	 *
	 * TODO: OverridableMethodCallInConstructor
	 *
	 * @param parent The parent element
	 * @param pos
	 */
	@SuppressWarnings("OverridableMethodCallInConstructor")
	public DivisibleGridElement(DivisibleGridElement parent, int[] pos) {
		super(parent.getGrid());
		this.parent = parent;
		this.children = new DivisibleGridElement[1 << getGrid().getDimension()];
		this.scale = parent.scale + 1;
		this.setPosition(pos, Grid::func_check_unoccupied);
	}
	private final DivisibleGridElement[] children;
	// TODO: FieldMayBeFinal
	@SuppressWarnings("FieldMayBeFinal")
	private DivisibleGridElement parent;
	private final int scale;

	/**
	 * Returns the parent element of this element, if it exists.
	 *
	 * @return
	 */
	public DivisibleGridElement getParent() {
		return parent;
	}

	/**
	 * Returns the scale of this element, i.e. the number of times getParent can
	 * be called on the returned value before null is returned.
	 *
	 * @return
	 */
	public int getScale() {
		return scale;
	}
}
