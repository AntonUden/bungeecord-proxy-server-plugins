package xyz.zeeraa.BungeecordServerCommons.Database.Objects;

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

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}
	
	@Deprecated
	public String getPrefix() {
		return prefix;
	}

	public String getLore() {
		return lore;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public boolean isUseDomain() {
		return useDomain;
	}

	public String getDomain() {
		return domain;
	}

	public boolean isRequireDirectJoin() {
		return requireDirectJoin;
	}

	public boolean isShowInServerList() {
		return showInServerList;
	}
	
	public String getIcon() {
		return icon;
	}
	
	public boolean hasIcon() {
		return icon != null;
	}
	
	public boolean hasPrefix() {
		if(prefix != null) {
			if(prefix.length() > 0) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasLore() {
		if(lore != null) {
			if(lore.length() > 0) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isModded() {
		return isModded;
	}
}