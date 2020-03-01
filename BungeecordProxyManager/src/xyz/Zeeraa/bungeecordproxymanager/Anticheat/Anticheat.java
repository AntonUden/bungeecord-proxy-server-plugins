package xyz.Zeeraa.bungeecordproxymanager.Anticheat;

import java.util.ArrayList;
import java.util.UUID;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import xyz.Zeeraa.bungeecordproxymanager.BungeecordProxyManager;
import xyz.zeeraa.BungeecordServerCommons.Database.Objects.ServerConfiguration;
import xyz.zeeraa.BungeecordServerCommons.Log.Log;

public class Anticheat implements Listener {
	/**
	 * players {@link UUID} gets added to checked player when joining a server
	 * running forge
	 */
	private ArrayList<UUID> checkedPlayers = new ArrayList<UUID>();

	/**
	 * Set the player as checked
	 * 
	 * @param uuid {@link UUID} of the player
	 * @return <code>false</code> if the player is already checked
	 */
	public boolean setChecked(UUID uuid) {
		if (!checkedPlayers.contains(uuid)) {
			checkedPlayers.add(uuid);
			return true;
		}

		return false;
	}

	/**
	 * {@link EventHandler} to remove player from the checked list
	 * 
	 * @param e {@link PlayerDisconnectEvent}
	 */
	@EventHandler
	public void onPlayerDisconnect(PlayerDisconnectEvent e) {
		checkedPlayers.remove(e.getPlayer().getUniqueId());
	}

	/**
	 * {@link EventHandler} to start mod list check
	 * 
	 * @param e {@link ServerConnectEvent}
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onServerConnect(ServerConnectEvent e) {
		if (e.isCancelled()) {
			return;
		}

		ServerConfiguration sc = BungeecordProxyManager.getInstance().getConfigurationManager().getServerConfiguration(e.getTarget().getName());

		if (sc != null) {
			if (sc.isModded()) {
				Log.info("Scheduled mod list check for player " + e.getPlayer().getName());
				new PlayerModlistChecker(e.getPlayer().getUniqueId());
			}
		}
	}
}