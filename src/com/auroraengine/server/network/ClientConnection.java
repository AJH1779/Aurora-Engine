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
package com.auroraengine.server.network;

/**
 * Denotes a connection from the server to an individual client.
 *
 * TODO: Not yet implemented fully.
 *
 * @author LittleRover
 */
public class ClientConnection {

	public ClientConnection(Player player) {
		this.player = player;
	}

	// Health of the connection
	private long last_message_time;
	// Will require an object referring to the in-game player thing.
	private final Player player;
	private final long[] recent_pings = new long[10];

	// AFK Check
	private long time_last_active;

	/**
	 * Returns the approximate latency in milliseconds.
	 *
	 * @return
	 */
	public long getLatency() {
		long l = 0L;
		for (long li : recent_pings) {
			l += li;
		}
		return l / 1000000L;
	}

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

}
