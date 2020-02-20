package xyz.Zeeraa.bungeecordserverlobby.ServerSelector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import xyz.zeeraa.BungeecordServerCommons.Database.Objects.ServerConfiguration;
import xyz.zeeraa.BungeecordServerCommons.Log.SLog;

public class ServerIcons {
	private HashMap<String, ItemStack> serverIcons = new HashMap<String, ItemStack>();
	
	public HashMap<String, ItemStack> getServerIcons() {
		return serverIcons;
	}
	
	public ItemStack getServerIcon(ServerConfiguration server) {
		return this.getServerIcon(server.getName());
	}
	
	public ItemStack getServerIcon(String server) {
		return serverIcons.get(server);
	}
	
	public ItemStack getServerIconClone(String server) {
		return serverIcons.get(server).clone();
	}
	
	public boolean hasServerIcon(ServerConfiguration server) {
		return this.hasServerIcon(server.getName());
	}
	
	public boolean hasServerIcon(String server) {
		return serverIcons.containsKey(server);
	}
	
	public void loadServerIcons(ArrayList<ServerConfiguration> servers) {
		serverIcons.clear();
		for(ServerConfiguration sc : servers) {
			if(sc.hasIcon()) {
				Material material = Material.BARRIER; // use barrier as icon on error
				
				try {
					material = Material.getMaterial(sc.getIcon());
				} catch(Exception e) {
					SLog.warning("Bad server icon for server " + sc.getName());
				}
				
				ItemStack icon = new ItemStack(material);
				
				icon.setAmount(1);
				ItemMeta meta = icon.getItemMeta();
				
				String itemName = ChatColor.translateAlternateColorCodes('§', sc.getDisplayName());
				
				meta.setDisplayName(itemName);
				
				if(sc.hasLore()) {
					List<String> lore = new ArrayList<String>();
					String[] lines = sc.getLore().split(Pattern.quote("\\n"));
					
					for(int i = 0; i < lines.length; i++) {
						lore.add(ChatColor.translateAlternateColorCodes('§', lines[i]));
					}
					meta.setLore(lore);
				}
				
				icon.setItemMeta(meta);
				
				serverIcons.put(sc.getName(), icon);
			}
		}
	}
}