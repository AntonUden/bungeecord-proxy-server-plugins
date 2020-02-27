package xyz.zeeraa.BungeecordServerCommons.Log;

import org.bukkit.Bukkit;

public class SLog {
	public static void info(String message) {
		Bukkit.getServer().getLogger().info(message);
	}
	
	public static void warning(String message) {
		Bukkit.getServer().getLogger().warning(message);
	}
}