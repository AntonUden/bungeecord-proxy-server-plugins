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

	/**
	 * @param loadOnConstruct if <code>false</code> blacklist will not load until
	 *                        {@link ModBlacklist#reload()} is called
	 */
	public ModBlacklist(boolean loadOnConstruct) {
		if (loadOnConstruct) {
			this.reload();
		}
	}

	/**
	 * Get list of all blacklisted mods
	 * 
	 * @return {@link HashMap} of blacklisted mods
	 */
	public HashMap<String, BlacklistedMod> getBlacklistedMods() {
		return blacklistedMods;
	}

	/**
	 * Check if mod is black listed
	 * 
	 * @param modName name of the mod to check
	 * @return <code>true</code> if mod is blacklisted
	 */
	public boolean isBlacklisted(String modName) {
		return blacklistedMods.containsKey(modName.toLowerCase());
	}

	/**
	 * Get {@link BlacklistedMod} info for mod by name
	 * 
	 * @param modName name of the mod to get
	 * @return {@link BlacklistedMod} for that mod or <code>null</code> if mod is
	 *         not blacklisted
	 */
	public BlacklistedMod getBlacklistedMod(String modName) {
		return blacklistedMods.get(modName.toLowerCase());
	}

	/**
	 * Reload list of blacklisted mods from database
	 * 
	 * @return <code>true</code> on success
	 */
	public boolean reload() {
		blacklistedMods.clear();

		try {
			ArrayList<BlacklistedMod> list = DBConnection.getBlacklistedMods();

			for (BlacklistedMod blacklistedMod : list) {
				blacklistedMods.put(blacklistedMod.getModName().toLowerCase(), blacklistedMod);
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}