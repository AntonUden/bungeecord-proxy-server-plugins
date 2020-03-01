package xyz.Zeeraa.bungeecordproxymanager.Configuration;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import xyz.zeeraa.BungeecordServerCommons.Database.DBConnection;
import xyz.zeeraa.BungeecordServerCommons.Database.Objects.ServerConfiguration;
import xyz.zeeraa.BungeecordServerCommons.Log.Log;

public class ConfigurationManager {
	private ArrayList<ServerConfiguration> servers = new ArrayList<ServerConfiguration>();

	private HashMap<String, ServerConfiguration> domainBinds = new HashMap<String, ServerConfiguration>();

	private HashMap<String, String> blockedDomains = new HashMap<String, String>();

	/**
	 * Load all server data from the database
	 * 
	 * @return <code>true</code> on success
	 */
	public boolean loadConfiguration() {
		ArrayList<String> oldServers = new ArrayList<String>();
		if (servers.size() != 0) {
			for (ServerConfiguration sc : servers) {
				oldServers.add(sc.getName());
			}
		}

		blockedDomains.clear();
		servers.clear();
		domainBinds.clear();

		try {
			blockedDomains = DBConnection.getBlockedDomains();
			Log.info(blockedDomains.size() + " blocked domains loaded");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		try {
			servers = DBConnection.getServers();
			Log.info(servers.size() + " servers loaded");

			for (ServerConfiguration sc : servers) {
				if (sc.isUseDomain()) {
					domainBinds.put(sc.getDomain(), sc);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		if (oldServers.size() != 0) {
			for (ServerConfiguration sc : servers) {
				oldServers.remove(sc.getName());
			}
		}

		if (oldServers.size() != 0) {
			Log.info("Removing " + oldServers.size() + " old servers from list");
			for (String name : oldServers) {
				Log.info("Removing " + name);
				this.unregisterServer(name);
			}
		}

		return true;
	}

	/**
	 * Get {@link ServerConfiguration} for server by name
	 * 
	 * @param server Name of the server
	 * @return {@link ServerConfiguration} of the server or <code>null</code> if the
	 *         server does not exist
	 */
	public ServerConfiguration getServerConfiguration(String server) {
		for (ServerConfiguration sc : servers) {
			if (sc.getName() == server) {
				return sc;
			}
		}
		return null;
	}

	/**
	 * Check if server exists in the server list
	 * 
	 * @param server Name of the server to check
	 * @return <code>true</code> if the server exists
	 */
	public boolean serverConfigurationExists(String server) {
		for (ServerConfiguration sc : servers) {
			if (sc.getName() == server) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Removes a server from BungeeCords server list
	 * 
	 * @param name Name of the server to remove
	 * @return <code>true</code> a server was removed
	 */
	public boolean unregisterServer(String name) {
		return ProxyServer.getInstance().getServers().remove(name) != null;
	}

	/**
	 * Register all servers to BungeeCord
	 */
	public void registerServers() {
		for (ServerConfiguration sc : servers) {
			InetSocketAddress address = new InetSocketAddress(sc.getHost(), sc.getPort());

			ServerInfo si = ProxyServer.getInstance().constructServerInfo(sc.getName(), address, sc.getLore(), false);

			ProxyServer.getInstance().getServers().put(si.getName(), si);
		}
	}

	/**
	 * Get {@link ServerConfiguration} for all servers
	 * 
	 * @return {@link ArrayList} with {@link ServerConfiguration} for all servers
	 */
	public ArrayList<ServerConfiguration> getServers() {
		return servers;
	}

	/**
	 * Get list of domains used to directly join servers
	 * 
	 * @return {@link HashMap} with domains for servers
	 */
	public HashMap<String, ServerConfiguration> getDomainBinds() {
		return domainBinds;
	}

	/**
	 * Get {@link HashMap} with all blocked domains
	 * 
	 * @return {@link HashMap} with domains and messages
	 */
	public HashMap<String, String> getBlockedDomains() {
		return blockedDomains;
	}

	/**
	 * Check if a domain is blocked
	 * 
	 * @param domain Domain to check
	 * @return <code>true</code> if the domain is blocked
	 */
	public boolean isDomainBlocked(String domain) {
		return blockedDomains.containsKey(domain.toLowerCase());
	}

	/**
	 * Get massage for blocked domain
	 * 
	 * @param domain Domain to get message for
	 * @return {@link String} with message or <code>null</code> if domain is not
	 *         blocked
	 */
	public String getDomainBlockMessage(String domain) {
		return blockedDomains.get(domain.toLowerCase());
	}

	/**
	 * Check if domain will cause the player to directly join a server
	 * 
	 * @param domain to check
	 * @return <code>true</code> if domain will redirect player
	 */
	public boolean hasDomainBind(String domain) {
		return domainBinds.containsKey(domain.toLowerCase());
	}

	/**
	 * Get server that domain will redirect to
	 * 
	 * @param domain Domain to get server for
	 * @return {@link String} with server name or <code>null</code> if domain does
	 *         not exist in the list
	 */
	public String getDomainBindServer(String domain) {
		return domainBinds.get(domain.toLowerCase()).getName();
	}
}