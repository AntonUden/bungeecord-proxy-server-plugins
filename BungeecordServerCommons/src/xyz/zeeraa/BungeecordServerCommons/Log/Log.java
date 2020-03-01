package xyz.zeeraa.BungeecordServerCommons.Log;

import java.util.logging.Logger;

public class Log {
	private static Logger logger = null;

	/**
	 * Set the {@link Logger} to use
	 * 
	 * @param logger {@link Logger} instance
	 */
	public static void setLogger(Logger logger) {
		Log.logger = logger;
	}

	/**
	 * Get the {@link Logger}
	 * 
	 * @return {@link Logger} instance
	 */
	public static Logger getLogger() {
		return logger;
	}

	/**
	 * Check if {@link Logger} has been set
	 * 
	 * @return <code>true</code> if {@link Logger} has been set
	 */
	public static boolean hasLogger() {
		return logger != null;
	}

	/**
	 * Shows info message
	 * 
	 * @param message Message to log
	 */
	public static void info(String message) {
		logger.info(message);
	}

	/**
	 * Shows warning message BungeeCord
	 * 
	 * @param message Message to log
	 */
	public static void warning(String message) {
		logger.warning(message);
	}
}