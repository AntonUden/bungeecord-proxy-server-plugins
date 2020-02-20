package xyz.Zeeraa.bungeecordserverlobby.Listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class KickJoiningPlayers implements Listener {
	@EventHandler
	public void player(PlayerJoinEvent e) {
		e.setJoinMessage(null);
		e.getPlayer().kickPlayer(ChatColor.RED + "Internal server error. Please try again later");
	}
}