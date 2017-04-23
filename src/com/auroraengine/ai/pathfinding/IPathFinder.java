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

import java.util.List;

/**
 * Denotes a generic pathfinder.
 *
 * TODO: Improve the pathfinding interface for more complex path modifications.
 *
 * @author LittleRover
 * @param <T> The class of the taker of the path
 * @param <K> The implementation of IPathCost.
 */
public interface IPathFinder<T, K extends IPathCost> {
	/**
	 * Finds a path between two points that may be taken by the provided taker. If
	 * no path is possible, then null should be returned.
	 *
	 * @param taker The taker of the path
	 * @param start The start node of the path
	 * @param end   The end node of the path
	 *
	 * @return The path as a list, or null if no valid path exists.
	 */
	public List<IPathNode<T, K>> getPath(T taker, IPathNode<T, K> start,
																			 IPathNode<T, K> end);
}
