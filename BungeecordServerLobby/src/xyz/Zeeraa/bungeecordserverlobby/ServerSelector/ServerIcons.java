package xyz.Zeeraa.bungeecordserverlobby.ServerSelector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import xyz.Zeeraa.bungeecordserverlobby.BungeecordServerLobby;
import xyz.zeeraa.BungeecordServerCommons.Database.Objects.ServerConfiguration;
import xyz.zeeraa.BungeecordServerCommons.Log.Log;

public class ServerIcons {
	private HashMap<String, ItemStack> serverIcons = new HashMap<String, ItemStack>();

	/**
	 * Get all server icons
	 * 
	 * @return {@link HashMap} with all server icons
	 */
	public HashMap<String, ItemStack> getServerIcons() {
		return serverIcons;
	}

	/**
	 * Get the server icon {@link ItemStack} for a server
	 * 
	 * @param server {@link ServerConfiguration} of the server
	 * @return returns server icon {@link ItemStack} for the server or
	 *         <code>null</code> if server icon does not exist for that server
	 */
	public ItemStack getServerIcon(ServerConfiguration server) {
		return this.getServerIcon(server.getName());
	}

	/**
	 * Get the server icon {@link ItemStack} for a server
	 * 
	 * @param server Name of the server
	 * @return returns server icon {@link ItemStack} for the server or
	 *         <code>null</code> if server icon does not exist for that server
	 */
	public ItemStack getServerIcon(String server) {
		return serverIcons.get(server);
	}

	/**
	 * Get a clone of the server icon {@link ItemStack} for a server
	 * 
	 * @param server {@link ServerConfiguration} of the server
	 * @return returns server icon {@link ItemStack} for the server as a clone or
	 *         <code>null</code> if server icon does not exist for that server
	 */
	public ItemStack getServerIconClone(ServerConfiguration server) {
		return this.getServerIconClone(server.getName());
	}

	/**
	 * Get a clone of the server icon {@link ItemStack} for a server
	 * 
	 * @param server Name of the server
	 * @return returns server icon {@link ItemStack} for the server as a clone or
	 *         <code>null</code> if server icon does not exist for that server
	 */
	public ItemStack getServerIconClone(String server) {
		if (!this.hasServerIcon(server)) {
			return null;
		}

		return serverIcons.get(server).clone();
	}

	/**
	 * Checks if a server has an icon
	 * 
	 * @param server {@link ServerConfiguration} of the server
	 * @return <code>true</code> if the server has an icon
	 */
	public boolean hasServerIcon(ServerConfiguration server) {
		return this.hasServerIcon(server.getName());
	}

	/**
	 * Checks if a server has an icon
	 * 
	 * @param server Name of the server
	 * @return <code>true</code> if the server has an icon
	 */
	public boolean hasServerIcon(String server) {
		return serverIcons.containsKey(server);
	}

	/**
	 * Loads server icons for all servers in list. To reload all server data from
	 * the database use {@link BungeecordServerLobby#reloadServers()}
	 * 
	 * @param servers {@link ArrayList} of {@link ServerConfiguration}
	 */
	public void loadServerIcons(ArrayList<ServerConfiguration> servers) {
		serverIcons.clear();
		for (ServerConfiguration sc : servers) {
			if (sc.hasIcon()) {
				Material material = Material.BARRIER; // use barrier as icon on error

				try {
					material = Material.getMaterial(sc.getIcon());
				} catch (Exception e) {
					Log.warning("Bad server icon for server " + sc.getName());
				}

				ItemStack icon = new ItemStack(material);

				icon.setAmount(1);
				ItemMeta meta = icon.getItemMeta();

				String itemName = ChatColor.translateAlternateColorCodes('§', sc.getDisplayName());

				meta.setDisplayName(itemName);

				if (sc.hasLore()) {
					List<String> lore = new ArrayList<String>();
					String[] lines = sc.getLore().split(Pattern.quote("\\n"));

					for (int i = 0; i < lines.length; i++) {
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