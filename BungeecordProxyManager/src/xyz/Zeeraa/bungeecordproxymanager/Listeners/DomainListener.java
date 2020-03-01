package xyz.Zeeraa.bungeecordproxymanager.Listeners;

import java.net.InetSocketAddress;
import java.util.HashMap;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import xyz.Zeeraa.bungeecordproxymanager.BungeecordProxyManager;

public class DomainListener implements Listener {
	private HashMap<String, String> redirect = new HashMap<String, String>();

	/**
	 * Check if player is allowed to join with domain or if player should be sent to
	 * a server directly on join
	 * 
	 * @param e {@link PreLoginEvent}
	 */
	@EventHandler
	public void onPreLogin(PreLoginEvent e) {
		if (redirect.containsKey(e.getConnection().getName())) {
			redirect.remove(e.getConnection().getName());
		}
		if (e.getConnection().isLegacy()) {
			e.setCancelled(true);
			e.setCancelReason(new TextComponent(ChatColor.RED + "Legacy connections are not supported"));
			return;
		}

		InetSocketAddress address = e.getConnection().getVirtualHost();

		if (address == null) {
			return;
		}

		String hostname = address.getHostName().toLowerCase();
		ProxyServer.getInstance().getLogger().info(e.getConnection().getName() + " is connecting with " + hostname);

		if (BungeecordProxyManager.getInstance().getConfigurationManager().isDomainBlocked(hostname)) {
			String message = BungeecordProxyManager.getInstance().getConfigurationManager().getDomainBlockMessage(hostname);

			if (message == null) {
				message = ChatColor.RED + "Unknown error";
			}

			if (message.length() == 0) {
				message = ChatColor.RED + "Blocked domain";
			}

			ProxyServer.getInstance().getLogger().info("Connection blocked with message " + message);
			e.setCancelled(true);
			e.setCancelReason(new TextComponent(ChatColor.translateAlternateColorCodes('§', message)));
			return;
		}

		if (BungeecordProxyManager.getInstance().getConfigurationManager().hasDomainBind(hostname)) {
			String targetServer = BungeecordProxyManager.getInstance().getConfigurationManager().getDomainBindServer(hostname);
			redirect.put(e.getConnection().getName(), targetServer);
		}
	}

	/**
	 * Redirect player to server if they joined with a specific domain
	 * 
	 * @param e {@link ServerConnectEvent}
	 */
	@EventHandler
	public void onServerConnect(ServerConnectEvent e) {
		if (redirect.containsKey(e.getPlayer().getName())) {
			String target = redirect.get(e.getPlayer().getName());

			System.out.println("Redirecting " + e.getPlayer().getDisplayName() + " to " + target);

			e.setTarget(ProxyServer.getInstance().getServerInfo(target));
			redirect.remove(e.getPlayer().getName());
		}
	}
}