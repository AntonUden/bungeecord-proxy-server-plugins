package xyz.Zeeraa.bungeecordproxymanager.Configuration;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import xyz.zeeraa.BungeecordServerCommons.Database.DBConnection;
import xyz.zeeraa.BungeecordServerCommons.Database.Objects.ServerConfiguration;
import xyz.zeeraa.BungeecordServerCommons.Log.BLog;

public class ConfigurationManager {
	private ArrayList<ServerConfiguration> servers = new ArrayList<ServerConfiguration>();
	
	private HashMap<String, ServerConfiguration> domainBinds = new HashMap<String, ServerConfiguration>();
	
	private HashMap<String, String> blockedDomains = new HashMap<String, String>();
	
	public boolean loadConfiguration() {
		blockedDomains.clear();
		servers.clear();
		domainBinds.clear();
		
		try {
			blockedDomains = DBConnection.getBlockedDomains();
			BLog.info(blockedDomains.size() + " blocked domains loaded");
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		try {
			servers = DBConnection.getServers();
			BLog.info(servers.size() + " servers loaded");
			
			for(ServerConfiguration sc : servers) {
				if(sc.isUseDomain()) {
					domainBinds.put(sc.getDomain(), sc);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public ServerConfiguration getServerConfiguration(String server) {
		for(ServerConfiguration sc : servers) {
			if(sc.getName() == server) {
				return sc;
			}
		}
		return null;
	}
	
	public boolean serverConfigurationExists(String server) {
		for(ServerConfiguration sc : servers) {
			if(sc.getName() == server) {
				return true;
			}
		}
		return false;
	}
	
	
	public void registerServers() {
		for(ServerConfiguration sc : servers) {
			InetSocketAddress address = new InetSocketAddress(sc.getHost(), sc.getPort());
			
			ServerInfo si = ProxyServer.getInstance().constructServerInfo(sc.getName(), address, sc.getLore(), false);
			
			ProxyServer.getInstance().getServers().put(si.getName(), si);
		}
	}
	
	
	public ArrayList<ServerConfiguration> getServers() {
		return servers;
	}
	
	public HashMap<String, ServerConfiguration> getDomainBinds() {
		return domainBinds;
	}
	
	public HashMap<String, String> getBlockedDomains() {
		return blockedDomains;
	}
	
	public boolean isDomainBlocked(String domain) {
		return blockedDomains.containsKey(domain.toLowerCase());
	}
	
	public String getDomainBlockMessage(String domain) {
		return blockedDomains.get(domain.toLowerCase());
	}
	
	public boolean hasDomainBind(String domain) {
		return domainBinds.containsKey(domain.toLowerCase());
	}
	
	public String getDomainBindServer(String domain) {
		return domainBinds.get(domain.toLowerCase()).getName();
	}
}