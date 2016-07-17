/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A line consisting of nodes.
 * @author ajh17
 */
public class LocalLine {
	
	private final ArrayList<LocalNode> nodes;
	private final List<LocalNode> immutable;
	
	public LocalLine(LocalNode... nodes) {
		this(Arrays.asList(nodes));
	}
	public LocalLine(List<LocalNode> nodes) {
		this.nodes = new ArrayList<>(nodes);
		this.immutable = Collections.unmodifiableList(this.nodes);
	}
	
	/**
	 * Returns an unmodifiable form of the nodes which make up this list.
	 * @return 
	 */
	public List<LocalNode> getNodes() {
		return immutable;
	}
}
