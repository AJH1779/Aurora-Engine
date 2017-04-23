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

import com.auroraengine.debug.AuroraLogs;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.logging.Logger;

// TODO: This class needs testing after the alteration that was made.
// This should be done before implementing this in any real manner.
/**
 * Utilises the A* algorithm for finding the path between two nodes.
 *
 * @author LittleRover
 * @param <T> The taker of the path. E.g. Creature
 * @param <K> The cost being evaluated. E.g. Distance or Overall Creature Cost
 *
 * @version 0.0.1 Development
 */
public class AStarPathfinder<T, K extends IPathCost> implements
				IPathFinder<T, K> {
	private static final Logger LOG = AuroraLogs.getLogger(AStarPathfinder.class
					.getName());

	@Override
	public List<IPathNode<T, K>> getPath(T taker, IPathNode<T, K> start,
																			 IPathNode<T, K> end) {
		// The Frontier - the possible paths which branch from considered paths,
		// ordered by "path length".
		PriorityQueue<MoveCost> frontier = new PriorityQueue<>();
		frontier.add(new MoveCost(start, null));
		// Maps a node in the space to the lowest cost path to get there.
		HashMap<IPathNode<T, K>, MoveCost> map = new HashMap<>(1 << Short.SIZE);
		map.put(start, new MoveCost(null, null));

		// While there are still paths to consider.
		while (!frontier.isEmpty()) {
			// Get the lowest cost path
			MoveCost current = frontier.poll();
			if (current.node == end) {
				break;
			}
			// Get the cost of moving to the destination normally
			K cost = map.get(current.node).cost;
			// for each of the neighbours
			current.node.getNeighbours().stream().forEach((next) -> {
				// Get the cost associated with moving with the current path to the next node
				@SuppressWarnings("unchecked")
				K new_cost = cost == null ?
										 current.node.getMoveCost(taker, next) :
										 (K) cost.add(current.node.getMoveCost(taker, next));
				// gets whether the map contains the next place or if the cost is less than
				if (!map.containsKey(next) || new_cost.isLessThan(map.get(next).cost)) {
					// adds the path to the next place.
					map.put(next, new MoveCost(current.node, new_cost));
					@SuppressWarnings("unchecked")
					K projected_cost = (K) new_cost.add(next
									.getMoveCost(taker, end));
					frontier.add(new MoveCost(next, projected_cost));
				}
			});
		}
		ArrayList<IPathNode<T, K>> path = new ArrayList<>();
		MoveCost temp = map.get(end);
		path.add(end);
		while (temp.node != null) {
			path.add(temp.node);
			temp = map.get(temp.node);
		}
		Collections.reverse(path);
		return path;
	}

	private class MoveCost implements Comparable<MoveCost> {
		MoveCost(IPathNode<T, K> node, K cost) {
			this.node = node;
			this.cost = cost;
		}
		final K cost;
		final IPathNode<T, K> node;

		@Override
		@SuppressWarnings("unchecked")
		public int compareTo(MoveCost o) {
			return cost.minus(o.cost).signum();
		}
	}
}
