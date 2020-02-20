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
	
	public Inventory getInventory() {
		return null;
	}
	
	public int getPageNumber() {
		return pageNumber;
	}
	
	public boolean hasNext() {
		return hasNext;
	}
	
	public boolean hasPrevious() {
		return hasPrevious;
	}

	public HashMap<Integer, String> serverName = new HashMap<Integer, String>();
}