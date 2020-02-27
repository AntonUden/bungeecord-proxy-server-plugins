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
	
	public int getId() {
		return id;
	}
	
	public String getModName() {
		return modName;
	}
	
	public String getBanMessage() {
		return banMessage;
	}
	
	public int getBanHours() {
		return banHours;
	}
}
