package xyz.zeeraa.BungeecordServerCommons.Database.Objects;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import net.md_5.bungee.api.ChatColor;

public class BanInfo {
	private int id;
	private boolean active;
	private UUID uuid;
	private String username;
	private String message;
	private Date expiresAt;
	private Date bannedAt;

	public BanInfo(int id, boolean active, UUID uuid, String username, String message, Date expiresAt, Date bannedAt) {
		this.id = id;
		this.active = active;
		this.uuid = uuid;
		this.username = username;
		this.message = message;
		this.expiresAt = expiresAt;
		this.bannedAt = bannedAt;
	}

	/**
	 * Get the database id for the ban
	 * @return id of the ban
	 */
	public int getId() {
		return id;
	}

	/**
	 * Check if the ban is active
	 * 
	 * @return <code>true</code> if the ban is active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Get the banned players {@link UUID}
	 * 
	 * @return the {@link UUID} of the banned player
	 */
	public UUID getUuid() {
		return uuid;
	}

	/**
	 * Get the banned players user name
	 * 
	 * @return user name of the player
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Get the ban message, for full ban message including details use
	 * {@link BanInfo#getFullMessage()}
	 * 
	 * @return ban message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Get the {@link Date} when the ban expires
	 * 
	 * @return {@link Date} when the ban expires or <code>null</code> if ban is
	 *         permanent
	 */
	public Date getExpiresAt() {
		return expiresAt;
	}

	/**
	 * Get the {@link Date} when the player was banned
	 * 
	 * @return {@link Date} when the player was banned
	 */
	public Date getBannedAt() {
		return bannedAt;
	}

	/**
	 * Check if the ban is permanent
	 * 
	 * @return <code>true</code> if permanent ban
	 */
	public boolean isPermban() {
		return expiresAt == null;
	}

	/**
	 * Get the full ban message to display when a player tries to join
	 * 
	 * @return ban message
	 */
	public String getFullMessage() {
		String result = "";

		result += ChatColor.RED + "You have been globally " + (this.isPermban() ? "permb" : "b") + "anned from this server\n\n";

		if (message != null) {
			result += ChatColor.RED + "Reason: " + ChatColor.WHITE + "" + ChatColor.translateAlternateColorCodes('$', this.getMessage()) + "\n\n";
		}

		if (!isPermban()) {
			result += ChatColor.GOLD + "Your ban will expire at: " + ChatColor.AQUA + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.getExpiresAt()) + "\n\n";
		}

		result += ChatColor.WHITE + "[" + this.getUuid().toString() + " | " + this.getId() + "]";

		return result;
	}
}