package xyz.zeeraa.BungeecordServerCommons.Database.Objects;

public class BlacklistedMod {
	private int id;
	private String modName;
	private String banMessage;
	private int banHours;

	public BlacklistedMod(int id, String modName, String banMessage, int banHours) {
		this.id = id;
		this.modName = modName;
		this.banMessage = banMessage;
		this.banHours = banHours;
	}

	/**
	 * Get the id of the blacklisted mod in the database
	 * 
	 * @return id of the blacklisted mod in the database
	 */
	public int getId() {
		return id;
	}

	/**
	 * Get the name of the mod
	 * 
	 * @return name of the mod
	 */
	public String getModName() {
		return modName;
	}

	/**
	 * Get the ban message to display when a player is banned for using this mod
	 * @return ban message
	 */
	public String getBanMessage() {
		return banMessage;
	}

	/**
	 * Get how many hours to ban the player for using this mod
	 * @return ban time in hours
	 */
	public int getBanHours() {
		return banHours;
	}
}
