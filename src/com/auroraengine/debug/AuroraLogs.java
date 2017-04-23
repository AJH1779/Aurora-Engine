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
package com.auroraengine.debug;

import com.auroraengine.utils.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * A type of logger used by this program whcih provides all of the information
 * in a more compact fashion. To be expanded.
 *
 * @author LittleRover
 */
public final class AuroraLogs {
	private static final AuroraLoggerFormat AURORA_FORMATTER
					= new AuroraLoggerFormat();
	private static final Logger LOG = Logger.getLogger(AuroraLogs.class.getName());

	/**
	 * Returns the logger with the specified name with the AuroraEngine formatting
	 * style.
	 *
	 * @param name The name of the logger
	 *
	 * @return The named logger.
	 */
	@NotNull
	public static Logger getLogger(@NotNull String name) {
		Logger log = Logger.getLogger(name);
		log.setUseParentHandlers(false);
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(AURORA_FORMATTER);
		log.addHandler(handler);
		LOG.log(Level.FINER, "Created new logger \"{0}\"", name);
		return log;
	}

	private AuroraLogs() {
	}

	private static class AuroraLoggerFormat extends Formatter {
		private static final SimpleDateFormat DATE_FORMAT
						= new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

		@Override
		public String format(LogRecord record) {
			StringBuilder b = new StringBuilder(1000);
			b.append("[").append(DATE_FORMAT.format(new Date(record.getMillis())))
							.append("] ").append(record.getSourceClassName()).append("#")
							.append(record.getSourceMethodName()).append(" - ").append(record
							.getLevel()).append(": ").append(formatMessage(record)).append(
							"\n");
			return b.toString();
		}
	}
}
