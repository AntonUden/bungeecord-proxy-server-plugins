package xyz.Zeeraa.bungeecordproxymanager.BanManager;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
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
import net.md_5.bungee.event.EventPriority;
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
	
	@EventHandler(priority = EventPriority.NORMAL)
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
	
	public static boolean banPlayer(UUID uuid, String playerName, String message, String comment) {
		return banPlayer(uuid, playerName, message, comment, null);
	}

	public static boolean banPlayer(UUID uuid, String playerName, String message, String comment, Date expiresAt) {
		String expiresAtStr = null;
		if(expiresAt != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			expiresAtStr = df.format(expiresAt);
		}
		
		try {
			String sql = "INSERT INTO `banned_players` (`active`, `uuid`, `username`, `message`, `expires`, `banned_at`, `comment`) VALUES ('1', ?, ?, ?, ?, CURRENT_TIMESTAMP, ?)";

			PreparedStatement ps = DBConnection.connection.prepareStatement(sql);

			ps.setString(1, uuid.toString());
			ps.setString(2, playerName);
			ps.setString(3, message);
			ps.setString(4, expiresAtStr);
			ps.setString(5, comment);

			int r = ps.executeUpdate();
			
			System.out.println("banPlayer: mysql returned " + r);
			
			ps.close();
			
			return true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}