package xyz.Zeeraa.bungeecordserverlobby.ServerSelector;

import java.util.HashMap;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class ServerMenuHolder implements InventoryHolder {
	private int pageNumber;
	private boolean hasPrevious;
	private boolean hasNext;

	public ServerMenuHolder(int pageNumber, boolean hasPrevious, boolean hasNext) {
		this.pageNumber = pageNumber;
		this.hasPrevious = hasPrevious;
		this.hasNext = hasNext;
	}

	@Deprecated
	public Inventory getInventory() {
		return null;
	}

	/**
	 * Get page number of server menu
	 * 
	 * @return page number
	 */
	public int getPageNumber() {
		return pageNumber;
	}

	/**
	 * Check if next page exists
	 * 
	 * @return <code>true</code> if next page exists
	 */
	public boolean hasNext() {
		return hasNext;
	}

	/**
	 * Check if previous page exist
	 * 
	 * @return <code>true</code> if previous page exists
	 */
	public boolean hasPrevious() {
		return hasPrevious;
	}

	/**
	 * {@link HashMap} that stores the server name for each slot
	 */
	public HashMap<Integer, String> serverName = new HashMap<Integer, String>();
}