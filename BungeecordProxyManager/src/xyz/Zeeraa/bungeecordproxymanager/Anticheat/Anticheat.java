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
import xyz.zeeraa.BungeecordServerCommons.Log.BLog;

public class Anticheat implements Listener {
	// players uuid gets added to checked player when joining a server running forge
	private ArrayList<UUID> checkedPlayers = new ArrayList<UUID>();
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onServerConnect(ServerConnectEvent e) {
		if(e.isCancelled()) {
			return;
		}
		
		ServerConfiguration sc = BungeecordProxyManager.getInstance().getConfigurationManager().getServerConfiguration(e.getTarget().getName());
		
		if(sc != null) {
			if(sc.isModded()) {
				BLog.info("Scheduled mod list check for player " + e.getPlayer().getName());
				new PlayerModlistChecker(e.getPlayer().getUniqueId());
			}
		}
	}
	
	public boolean setChecked(UUID uuid) {
		if(!checkedPlayers.contains(uuid)) {
			checkedPlayers.add(uuid);
			return true;
		}
		
		return false;
	}
	
	
	@EventHandler
	public void onPlayerDisconnect(PlayerDisconnectEvent e) {
		checkedPlayers.remove(e.getPlayer().getUniqueId());
	}
}