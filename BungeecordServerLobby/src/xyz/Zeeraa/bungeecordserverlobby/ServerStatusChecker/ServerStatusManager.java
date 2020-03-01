package xyz.Zeeraa.bungeecordserverlobby.ServerStatusChecker;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import xyz.Zeeraa.bungeecordserverlobby.BungeecordServerLobby;
import xyz.zeeraa.BungeecordServerCommons.Database.Objects.ServerConfiguration;

public class ServerStatusManager implements PluginMessageListener, Listener {
	private HashMap<String, ServerStatus> serverStatus = new HashMap<String, ServerStatus>();

	private boolean pingOnJoin = true;

	public ServerStatusManager() {
		for (ServerConfiguration sc : BungeecordServerLobby.getInstance().getServers()) {
			serverStatus.put(sc.getName(), new ServerStatus());
		}

		Bukkit.getScheduler().scheduleSyncRepeatingTask(BungeecordServerLobby.getInstance(), new Runnable() {
			@Override
			public void run() {
				update();
				pingOnJoin = true;
			}
		}, 100L, 100L);
	}

	/**
	 * Requests updated server status from BungeeCord.
	 * 
	 */
	public void update() {
		Player player = Iterables.getFirst(Bukkit.getServer().getOnlinePlayers(), null);

		if (player == null) {
			return;
		}

		for (ServerConfiguration sc : BungeecordServerLobby.getInstance().getServers()) {
			try {
				ByteArrayDataOutput out = ByteStreams.newDataOutput();
				out.writeUTF("serverstatus");
				out.writeUTF(sc.getName());
				player.sendPluginMessage(BungeecordServerLobby.getInstance(), "proxymanager:pm", out.toByteArray());
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (serverStatus.get(sc.getName()).onlineStateCheckCountdown > 0) {
				serverStatus.get(sc.getName()).onlineStateCheckCountdown--;
			} else {
				if (serverStatus.get(sc.getName()).isOnline()) {
					serverStatus.get(sc.getName()).setOffline();
				}
			}
		}
	}

	/**
	 * @param server Name of server to get status for
	 * @return {@link ServerStatus} or <code>null</code> if server does not exist
	 */
	public ServerStatus getServerStatus(String server) {
		return serverStatus.get(server);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		if (pingOnJoin) {
			pingOnJoin = false;
			update();
		}
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (channel.equals("proxymanager:pm")) {
			ByteArrayDataInput in = ByteStreams.newDataInput(message);
			String subchannel = in.readUTF();
			if (subchannel.equals("serverstatus")) {
				String server = in.readUTF();

				// request failed
				if (!in.readBoolean()) {
					// System.err.println("Request failure for server " + server);
					return;
				}

				boolean online = in.readBoolean();
				int playercount = in.readInt();
				int maxplayers = in.readInt();

				// System.out.println("c: " + channel + " sc: " + subchannel + " s: " + server +
				// " o: " + online + " op: " + playercount + " mp: " + maxplayers);

				if (serverStatus.containsKey(server)) {
					serverStatus.get(server).set(playercount, maxplayers, ServerState.fromBoolean(online)).onlineStateCheckCountdown = 3;
				}
			}
		}
	}
}