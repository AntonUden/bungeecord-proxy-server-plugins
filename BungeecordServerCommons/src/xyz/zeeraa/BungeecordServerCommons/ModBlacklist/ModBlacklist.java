package xyz.zeeraa.BungeecordServerCommons.ModBlacklist;

import java.util.ArrayList;
import java.util.HashMap;

import xyz.zeeraa.BungeecordServerCommons.Database.DBConnection;
import xyz.zeeraa.BungeecordServerCommons.Database.Objects.BlacklistedMod;

public class ModBlacklist {
	private HashMap<String, BlacklistedMod> blacklistedMods = new HashMap<String, BlacklistedMod>();
	
	public ModBlacklist() {
		this(true);
	}
	
	public ModBlacklist(boolean loadOnConstruct) {
		if(loadOnConstruct) {
			this.reload();
		}
	}
	
	public HashMap<String, BlacklistedMod> getBlacklistedMods() {
		return blacklistedMods;
	}
	
	public boolean isBlacklisted(String modName) {
		return blacklistedMods.containsKey(modName.toLowerCase());
	}
	
	public BlacklistedMod getBlacklistedMod(String modName) {
		return blacklistedMods.get(modName.toLowerCase());
	}
	
	public boolean reload() {
		blacklistedMods.clear();
		
		try {
			ArrayList<BlacklistedMod> list = DBConnection.getBlacklistedMods();
			
			for (BlacklistedMod blacklistedMod : list) {
				blacklistedMods.put(blacklistedMod.getModName().toLowerCase(), blacklistedMod);
			}
			
			return true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}