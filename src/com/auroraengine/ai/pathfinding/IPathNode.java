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
 * An object denoting a node which is used for constructing paths.
 *
 * @author LittleRover
 * @param <T> The class of takers of the path consisting of these nodes.
 * @param <K> The implementation used of IPathCost.
 */
public interface IPathNode<T, K extends IPathCost> {
	/**
	 * Returns a list of the nodes that can be reached directly from this node.
	 *
	 * TODO: Smarter implementation of nodes.
	 *
	 * @return
	 */
	public List<IPathNode<T, K>> getNeighbours();

	/**
	 * Returns the cost of moving from this to the specified node. If the provided
	 * node is a neighbour, then the exact cost is likely to be returned. If the
	 * provided node is not a neighbour, then an estimated cost is returned.
	 *
	 * @param p_walker      The walker
	 * @param p_destination The destination
	 *
	 * @return The estimated or actual cost to reach the destination
	 */
	public K getMoveCost(T p_walker, IPathNode<T, K> p_destination);
}
