package xyz.zeeraa.BungeecordServerCommons.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import xyz.zeeraa.BungeecordServerCommons.Database.Objects.BanInfo;
import xyz.zeeraa.BungeecordServerCommons.Database.Objects.BlacklistedMod;
import xyz.zeeraa.BungeecordServerCommons.Database.Objects.ServerConfiguration;

public class DBConnection {
	/**
	 * Instance of the {@link Connection} to the database
	 */
	public static Connection connection;

	/**
	 * Connects to the database
	 * 
	 * @param driver   driver to use
	 * @param host     host to connect to
	 * @param user     user name of the MySQL user
	 * @param pass     password of the MySQL user
	 * @param database name of the database to use
	 * @return <code>true</code> on success or <code>false</code> on failure
	 */
	public static boolean init(String driver, String host, String user, String pass, String database) {
		if (connection != null) {
			return false;
		}
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(host, user, pass);
			connection.setCatalog(database);
			return true;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Close the {@link Connection} to the database
	 * 
	 * @return <code>true</code> if the {@link Connection} was closed
	 * @throws SQLException
	 */
	public static boolean close() throws SQLException {
		if (connection == null) {
			return false;
		}

		if (connection.isClosed()) {
			return false;
		}

		connection.close();
		return true;
	}

	/**
	 * Fetches a list of blocked domains from the database
	 * 
	 * @return {@link HashMap} of blocked domains and message
	 * @throws SQLException
	 */
	public static HashMap<String, String> getBlockedDomains() throws SQLException {
		HashMap<String, String> blockedDomains = new HashMap<String, String>();

		String sql = "SELECT * FROM `blocked_domains`";

		PreparedStatement ps = DBConnection.connection.prepareStatement(sql);

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			blockedDomains.put(rs.getString("domain").toLowerCase(), rs.getString("message"));
		}

		rs.close();
		ps.close();

		return blockedDomains;
	}

	/**
	 * Fetches a list of all servers from the database
	 * 
	 * @return {@link ArrayList} of {@link ServerConfiguration}
	 * @throws SQLException
	 */
	public static ArrayList<ServerConfiguration> getServers() throws SQLException {
		ArrayList<ServerConfiguration> servers = new ArrayList<ServerConfiguration>();
		String sql = "SELECT * FROM `servers`";

		PreparedStatement ps = DBConnection.connection.prepareStatement(sql);

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			String domain = rs.getString("domain");

			if (domain != null) {
				domain = domain.toLowerCase();
			}

			ServerConfiguration sc = new ServerConfiguration(rs.getInt("id"), rs.getString("name"), rs.getString("displayname"), rs.getString("prefix"), rs.getString("lore"), rs.getString("host"), rs.getInt("port"), rs.getBoolean("use_domain"), domain, rs.getBoolean("require_direct_join"), rs.getBoolean("show_in_server_list"), rs.getString("icon"), rs.getBoolean("is_modded"));

			servers.add(sc);
		}

		rs.close();
		ps.close();

		return servers;
	}

	/**
	 * Fetches a list of all blacklisted mods from the database
	 * 
	 * @return {@link ArrayList} of {@link BlacklistedMod}
	 * @throws SQLException
	 */
	public static ArrayList<BlacklistedMod> getBlacklistedMods() throws SQLException {
		ArrayList<BlacklistedMod> blacklistedMods = new ArrayList<BlacklistedMod>();
		String sql = "SELECT * FROM `mod_blacklist`";

		PreparedStatement ps = DBConnection.connection.prepareStatement(sql);

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			blacklistedMods.add(new BlacklistedMod(rs.getInt("id"), rs.getString("mod_name"), rs.getString("ban_message"), rs.getInt("ban_hours")));
		}

		rs.close();
		ps.close();

		return blacklistedMods;
	}

	/**
	 * Gets the longest active ban for a player
	 * 
	 * @param uuid {@link UUID} of the player to check
	 * @return {@link BanInfo} for the longest active ban
	 * @throws SQLException
	 * @throws ParseException
	 */
	public static BanInfo getLongestActiveBan(UUID uuid) throws SQLException, ParseException {
		BanInfo result = null;

		String sql = "SELECT * FROM banned_players WHERE uuid = ? AND active = 1 ORDER BY expires";

		PreparedStatement ps = DBConnection.connection.prepareStatement(sql);

		ps.setString(1, uuid.toString());

		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			UUID bUuid = UUID.fromString(rs.getString("uuid"));

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			Date expiresAt = null;
			String expireAtStr = rs.getString("expires");
			if (expireAtStr != null) {
				expiresAt = df.parse(expireAtStr);
			}

			Date bannedAt = df.parse(rs.getString("banned_at"));

			result = new BanInfo(rs.getInt("id"), rs.getBoolean("active"), bUuid, rs.getString("username"), rs.getString("message"), expiresAt, bannedAt);
		}

		rs.close();
		ps.close();

		return result;
	}

	/**
	 * Sets all bans that has expired as inactive
	 * 
	 * @return <code>true</code> on success
	 */
	public static boolean updateBanExpiration() {
		try {
			String sql = "UPDATE banned_players SET active = 0 WHERE expires < CURRENT_TIMESTAMP";
			PreparedStatement ps = DBConnection.connection.prepareStatement(sql);

			ps.executeUpdate();
			ps.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}

		return true;
	}
}