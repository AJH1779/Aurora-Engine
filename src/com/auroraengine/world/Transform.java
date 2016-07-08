/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.world;

/**
 *
 * @author Arthur
 */
public class Transform<A, B, C> {

	private final A to;
	private final B from;
	private final C fromto;

	public Transform(A to, B from, C transform) {
		this.to = to;
		this.from = from;
		this.fromto = transform;
	}

	public A getTo() {
		return to;
	}
	public B getFrom() {
		return from;
	}
	public C getFromTo() {
		return fromto;
	}
}
