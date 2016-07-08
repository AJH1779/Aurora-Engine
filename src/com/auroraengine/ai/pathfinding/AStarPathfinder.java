/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.ai.pathfinding;

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
 * @author Arthur
 * @param <T> The taker of the path. E.g. Creature
 * @param <K> The cost being evaluated. E.g. Distance or Overall Creature Cost
 *
 * @version 0.0.1 Development
 */
public class AStarPathfinder<T, K extends IPathCost> implements IPathFinder<T, K> {
	private static final Logger LOG = Logger.getLogger(AStarPathfinder.class.getName());
    @Override
    public List<IPathNode<T, K>> getPath(T taker, IPathNode<T, K> start, IPathNode<T, K> end) {
        // The Frontier - the possible paths which branch from considered paths,
		// ordered by "path length".
        PriorityQueue<MoveCost> frontier = new PriorityQueue<>();
        frontier.add(new MoveCost(start, null));
        // Maps a node in the space to the lowest cost path to get there.
        HashMap<IPathNode<T, K>, MoveCost> map = new HashMap<>(1 << Short.SIZE);
        map.put(start, new MoveCost(null, null));

        // While there are still paths to consider.
        while(!frontier.isEmpty()) {
            // Get the lowest cost path
            MoveCost current = frontier.poll();
            if(current.node == end) {
                break;
            }
            // Get the cost of moving to the destination normally
            K cost = map.get(current.node).cost;
			// for each of the neighbours
            current.node.getNeighbours().stream().forEach((next) -> {
				// Get the cost associated with moving with the current path to the next node
                K new_cost = cost == null ?
						current.node.getCostToMoveTo(taker, next) :
						(K) cost.add(current.node.getCostToMoveTo(taker, next));
				// gets whether the map contains the next place or if the cost is less than
                if (!map.containsKey(next) || new_cost.isLessThan(map.get(next).cost)) {
					// adds the path to the next place.
                    map.put(next, new MoveCost(current.node, new_cost));
                    K projected_cost = (K) new_cost.add(next.getProjectedCostTo(taker, end));
                    frontier.add(new MoveCost(next, projected_cost));
                }
            });
        }
        ArrayList<IPathNode<T, K>> path = new ArrayList<>();
        MoveCost temp = map.get(end);
        path.add(end);
        while(temp.node != null) {
            path.add(temp.node);
            temp = map.get(temp.node);
        }
        Collections.reverse(path);
        return path;
    }

    private class MoveCost implements Comparable {
        MoveCost(IPathNode<T, K> node, K cost) {
            this.node = node;
            this.cost = cost;
        }
        final IPathNode<T, K> node;
        final K cost;

        @Override
		@SuppressWarnings("unchecked")
        public int compareTo(Object o) {
            return cost.minus(((MoveCost) o).cost).signum();
        }
    }
}
