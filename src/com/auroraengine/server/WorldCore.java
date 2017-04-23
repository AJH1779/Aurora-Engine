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
package com.auroraengine.server;

import com.auroraengine.debug.AuroraException;
import com.auroraengine.debug.AuroraLogs;
import com.auroraengine.threading.SynchroCore;
import java.util.logging.Logger;

/**
 *
 * @author LittleRover
 */
public class WorldCore extends SynchroCore {

	private static final Logger LOG = AuroraLogs.getLogger(WorldCore.class
					.getName());

	public WorldCore(String name, SynchroCore dependent) {
		super(name, dependent);
	}

	@Override
	protected void initialise()
					throws AuroraException {

	}

	@Override
	protected boolean isRunning()
					throws AuroraException {
		return true;
	}

	@Override
	protected void processException(AuroraException ex) {

	}

	@Override
	protected void shutdown() {

	}

	@Override
	protected void update()
					throws AuroraException {

	}
}
