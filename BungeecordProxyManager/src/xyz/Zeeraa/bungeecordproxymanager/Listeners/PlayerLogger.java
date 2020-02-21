package xyz.Zeeraa.bungeecordproxymanager.Listeners;

import java.net.InetSocketAddress;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import xyz.zeeraa.BungeecordServerCommons.Database.DBConnection;

public class PlayerLogger implements Listener {
	@EventHandler
	public void onPostLogin(PostLoginEvent e) {
		ProxiedPlayer p = e.getPlayer();

		String oldUsername = null;
		try {
			String sql = "SELECT username FROM `players` WHERE uuid = ?";

			PreparedStatement ps = DBConnection.connection.prepareStatement(sql);

			ps.setString(1, p.getUniqueId().toString());

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				oldUsername = rs.getString("username");
			}

			rs.close();
			ps.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (oldUsername != null) {
			if (!oldUsername.equals(p.getName())) {
				System.out.println(oldUsername + " has changed name to " + p.getName());
				try {
					String sql = "INSERT INTO name_history (uuid, old_name, new_name) VALUES(?, ?, ?)";

					PreparedStatement ps = DBConnection.connection.prepareStatement(sql);

					ps.setString(1, p.getUniqueId().toString());
					ps.setString(2, oldUsername);
					ps.setString(3, p.getName());

					ps.executeUpdate();
					ps.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		InetSocketAddress address = e.getPlayer().getPendingConnection().getVirtualHost();
		String hostname = null;
		if (address != null) {
			hostname = address.getHostName().toLowerCase();
		}

		if (oldUsername == null) {
			try {
				String sql = "INSERT INTO players (uuid, username, last_join_timestamp, last_join_ip, last_join_hostname) VALUES(?, ?, CURRENT_TIMESTAMP, ?, ?)";

				PreparedStatement ps = DBConnection.connection.prepareStatement(sql);

				ps.setString(1, p.getUniqueId().toString());
				ps.setString(2, p.getName());
				ps.setString(3, p.getAddress().getAddress().toString());
				ps.setString(4, hostname);

				ps.executeUpdate();
				ps.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			try {
				String sql = "UPDATE players SET username = ?, last_join_timestamp = CURRENT_TIMESTAMP, last_join_ip = ?, last_join_hostname = ? WHERE uuid = ?";

				PreparedStatement ps = DBConnection.connection.prepareStatement(sql);

				ps.setString(1, p.getName());
				ps.setString(2, p.getAddress().getAddress().toString());
				ps.setString(3, hostname);
				ps.setString(4, p.getUniqueId().toString());

				ps.executeUpdate();
				ps.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	@EventHandler
	public void onChat(ChatEvent e) {
		if (e.getSender() instanceof ProxiedPlayer) {
			ProxiedPlayer player = (ProxiedPlayer) e.getSender();

			try {
				String sql = "INSERT INTO `chat_log` (`server`, `uuid`, `username`, `message`) VALUES (?, ?, ?, ?);";
				PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
				
				ps.setString(1, player.getServer().getInfo().getName());
				ps.setString(2, player.getUniqueId().toString());
				ps.setString(3, player.getName());
				ps.setString(4, e.getMessage());

				ps.executeUpdate();
				ps.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}