package xyz.zeeraa.BungeecordServerCommons.Database.Objects;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import net.md_5.bungee.api.ChatColor;

public class BanInfo {
	private int     id;
	private boolean active;
	private UUID    uuid;
	private String  username;
	private String  message;
	private Date    expiresAt;
	private Date    bannedAt;
	
	public BanInfo(int id, boolean active, UUID uuid, String username, String message, Date expiresAt, Date bannedAt) {
		this.id = id;
		this.active = active;
		this.uuid = uuid;
		this.username = username;
		this.message = message;
		this.expiresAt = expiresAt;
		this.bannedAt = bannedAt;
	}
	
	public int getId() {
		return id;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getMessage() {
		return message;
	}
	
	public Date getExpiresAt() {
		return expiresAt;
	}
	
	public Date getBannedAt() {
		return bannedAt;
	}
	
	public boolean isPermban() {
		return expiresAt == null;
	}

	public String getFullMessage() {
		String result = "";
		
		result += ChatColor.RED + "You have been globally " + (this.isPermban() ? "permb" : "b") + "anned from this server\n\n";
		
		if(message != null) {
			result += ChatColor.RED + "Reason: " + ChatColor.WHITE + "" +  ChatColor.translateAlternateColorCodes('$', this.getMessage()) + "\n\n";
		}
		
		if(!isPermban()) {
			result += ChatColor.GOLD + "Your ban will expire at: " + ChatColor.AQUA + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.getExpiresAt()) +  "\n\n";
		}
		
		result += ChatColor.WHITE + "[" + this.getUuid().toString() + " | " + this.getId() + "]";
		
		return result;
	}
}