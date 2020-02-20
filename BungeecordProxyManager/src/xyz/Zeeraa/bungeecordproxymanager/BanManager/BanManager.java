package xyz.Zeeraa.bungeecordproxymanager.BanManager;

import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.event.EventHandler;
import xyz.zeeraa.BungeecordServerCommons.Database.DBConnection;
import xyz.zeeraa.BungeecordServerCommons.Database.Objects.BanInfo;

public class BanManager implements Listener {
	private ScheduledTask updateTask;
	
	private static final String BAN_CHECK_FAIL_MESSAGE = ChatColor.RED + "An internal error occurred in the proxy server.\nIf this error persists please contact an admin";

	public BanManager(Plugin plugin) {
		updateTask = plugin.getProxy().getScheduler().schedule(plugin, new Runnable() {
			@Override
			public void run() {
				DBConnection.updateBanExpiration();
				checkPlayers();
			}
		}, 5, 5, TimeUnit.SECONDS);
	}

	public void stop() {
		if (updateTask != null) {
			updateTask.cancel();
			updateTask = null;
		}
	}

	public void checkPlayerBan(ProxiedPlayer player) {
		try {
			BanInfo banInfo = DBConnection.getLongestActiveBan(player.getUniqueId());
			
			if (banInfo != null) {
				player.disconnect(new TextComponent(banInfo.getFullMessage()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			player.disconnect(new TextComponent(BAN_CHECK_FAIL_MESSAGE + ChatColor.AQUA + "\n\nException: " + e.getClass().getName()));
		}
	}

	public void checkPlayers() {
		for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
			checkPlayerBan(player);
		}
	}
	
	@EventHandler
	public void onLogin(LoginEvent e) {
		try {
			BanInfo banInfo = DBConnection.getLongestActiveBan(e.getConnection().getUniqueId());
			
			if (banInfo != null) {
				e.setCancelled(true);
				e.setCancelReason(new TextComponent(banInfo.getFullMessage()));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			e.setCancelled(true);
			e.setCancelReason(new TextComponent(BAN_CHECK_FAIL_MESSAGE + ChatColor.AQUA + "\n\nException: " + ex.getClass().getName()));
		}
	}

}