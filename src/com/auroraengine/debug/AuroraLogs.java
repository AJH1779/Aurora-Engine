/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.debug;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * A type of logger used by this program whcih provides all of the information
 * in a more compact fashion. To be expanded.
 * @author Aurora
 */
public final class AuroraLogs {
	public static final AuroraLoggerFormat AURORA_FORMATTER =
		 new AuroraLoggerFormat();


	/**
	 * Returns the logger for the specified class file with the AuroraEngine
	 * formatting style.
	 * @param cls The class to make it for
	 * @return The logger for the class
	 */
	public static Logger getLogger(Class cls) {
		Logger log = Logger.getLogger(cls.getName());
		log.setUseParentHandlers(false);
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(AURORA_FORMATTER);
		log.addHandler(handler);
		return log;
	}
	private AuroraLogs() {}
	private static class AuroraLoggerFormat extends Formatter {
		private static final SimpleDateFormat DATE_FORMAT =
				new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

		@Override
		public String format(LogRecord record) {
			StringBuilder b = new StringBuilder(1000);
			b.append("[").append(DATE_FORMAT.format(new Date(record.getMillis()))).append("] ").append(record.getSourceClassName()).append("#").append(record.getSourceMethodName()).append(" - ").append(record.getLevel()).append(": ").append(formatMessage(record)).append("\n");
			return b.toString();
		}
	}
}
