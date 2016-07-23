/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.server.network;

import com.auroraengine.creature.soul.PlayerSoul;

/**
 *
 * @author Arthur
 */
public class ClientConnection {
	
	public ClientConnection(PlayerSoul player) {
		this.player = player;
	}
	
	// Will require an object referring to the in-game player thing.
	private final PlayerSoul player;
	
	// Health of the connection
	private long last_message_time;
	private final long[] recent_pings = new long[10];
	
	// AFK Check
	private long time_last_active;
	
	/**
	 * Called once each tick of the game server.
	 */
	public void tick() {
		// For the previous tick essentially
		// Get the packets sent
		// Send a packet to confirm living
		// Check for limits on actions
		// Process these actions
	}
	
	/**
	 * Returns the approximate latency in milliseconds.
	 * @return 
	 */
	public long getLatency() {
		long l = 0L;
		for(long li : recent_pings) {
			l += li;
		}
		return l / 1000000L;
	}
	
	
}
