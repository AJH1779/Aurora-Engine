/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.ai.pathfinding;

/**
 *
 * @author Arthur
 */
public interface IPathCost {
	/**
	 * Adds the provided path cost to this path cost.
	 * @param cost
	 * @return
	 */
    public IPathCost add(IPathCost cost);
	/**
	 * Minus the provided path cost from this path cost.
	 * @param cost
	 * @return
	 */
    public IPathCost minus(IPathCost cost);
	/**
	 * Returns true if the this path cost is less than the provided path cost.
	 * @param cost
	 * @return
	 */
    public boolean isLessThan(IPathCost cost);
	/**
	 * Returns true if this path cost is greater than the provided path cost.
	 * @param cost
	 * @return
	 */
    public boolean isGreaterThan(IPathCost cost);
	/**
	 * Returns true if this path cost is equal to the provided path cost.
	 * @param cost
	 * @return
	 */
    public boolean isEqualTo(IPathCost cost);
	/**
	 * Returns 1 if the path is positive in cost, -1 if negative in cost, and 0
	 * if zero in cost.
	 * @return
	 */
    public int signum();
}
