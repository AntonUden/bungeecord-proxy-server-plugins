package xyz.Zeeraa.bungeecordproxymanager.Listeners;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import xyz.zeeraa.BungeecordServerCommons.Log.BLog;

public class DenyJoinListener implements Listener {
	@EventHandler
	public void onLogin(LoginEvent e) {
		TextComponent message = new TextComponent("Internal server error. Please try again later");
		message.setColor(ChatColor.RED);
		
		BLog.info("Kicking " + e.getConnection().getUniqueId().toString());
		
		e.setCancelReason(message);
		e.setCancelled(true);
	}
}