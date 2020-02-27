package xyz.zeeraa.BungeecordServerCommons.Log;

import net.md_5.bungee.api.ProxyServer;

public class BLog {
	public static void info(String message) {
		ProxyServer.getInstance().getLogger().info(message);
	}
	
	public static void warning(String message) {
		ProxyServer.getInstance().getLogger().warning(message);
	}
}