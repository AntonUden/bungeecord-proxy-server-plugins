package xyz.zeeraa.BungeecordServerCommons.Database.Objects;

import org.apache.commons.lang.ObjectUtils.Null;
import org.bukkit.Material;

public class ServerConfiguration {
	private int id;
	private String name;
	private String displayName;
	private String prefix;
	private String lore;
	private String host;
	private int port;
	private boolean useDomain;
	private String domain;
	private boolean requireDirectJoin;
	private boolean showInServerList;
	private String icon;
	private boolean isModded;

	public ServerConfiguration(int id, String name, String displayName, String prefix, String lore, String host, int port, boolean useDomain, String domain, boolean requireDirectJoin, boolean showInServerList, String icon, boolean isModded) {
		this.id = id;
		this.name = name;
		this.displayName = displayName;
		this.prefix = prefix;
		this.lore = lore;
		this.host = host;
		this.port = port;
		this.useDomain = useDomain;
		this.domain = domain;
		this.requireDirectJoin = requireDirectJoin;
		this.showInServerList = showInServerList;
		this.icon = icon;
		this.isModded = isModded;
	}

	/**
	 * Get id of the server
	 * 
	 * @return id of the server
	 */
	public int getId() {
		return id;
	}

	/**
	 * Get the BungeeCord name of the server
	 * 
	 * @return name of the server
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the display name for the server
	 * 
	 * @return display name of the server
	 */
	public String getDisplayName() {
		return displayName;
	}

	@Deprecated
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Get the lore for the server icon
	 * 
	 * @return the lore for the server
	 */
	public String getLore() {
		return lore;
	}

	/**
	 * Get the IP or host name for the server
	 * 
	 * @return {@link String} of IP or host name for the server
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Return the port for the server
	 * 
	 * @return port for the server
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Check if the server is linked to a domain or sub domain
	 * 
	 * @return <code>true</code> if the server is linked to a domain or sub domain
	 */
	public boolean isUseDomain() {
		return useDomain;
	}

	/**
	 * Get the domain or sub domain used to join the server directly
	 * 
	 * @return the domain or sub domain used to join the server directly or
	 *         {@link Null} if the server is not linked to a domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * Check if players need to join the server directly using a domain or sub
	 * domain instead of using the server selector
	 * 
	 * @return <code>true</code> if the server requires players to join directly
	 */
	public boolean isRequireDirectJoin() {
		return requireDirectJoin;
	}

	/**
	 * Check if the server should be shown in the server selector
	 * 
	 * @return <code>true</code> if the server should be shown in the server
	 *         selector
	 */
	public boolean isShowInServerList() {
		return showInServerList;
	}

	/**
	 * Get name of the {@link Material} used for the server icon
	 * 
	 * @return name of {@link Material} that the icon will use or <code>null</code>
	 *         if not defined in the database
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * Check if the server has an icon
	 * 
	 * @return <code>true</code> if the server has an icon
	 */
	public boolean hasIcon() {
		return icon != null;
	}

	@Deprecated
	public boolean hasPrefix() {
		if (prefix != null) {
			if (prefix.length() > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if the server has lore to display in the icon
	 * 
	 * @return <code>true</code> if the server has any lore
	 */
	public boolean hasLore() {
		if (lore != null) {
			if (lore.length() > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if the server is using a modded version of Minecraft, This is used to
	 * check if players should be checked for blacklisted mods when they join the
	 * server
	 * 
	 * @return <code>true</code> if the server is using mods
	 */
	public boolean isModded() {
		return isModded;
	}
}