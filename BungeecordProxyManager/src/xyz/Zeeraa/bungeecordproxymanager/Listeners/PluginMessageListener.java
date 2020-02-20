package xyz.Zeeraa.bungeecordproxymanager.Listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PluginMessageListener implements Listener {
	@EventHandler
	public void onPluginMessage(PluginMessageEvent e) {
		if (e.getTag().equalsIgnoreCase("proxymanager:pm")) {
			ByteArrayDataInput in = ByteStreams.newDataInput(e.getData());
			String subChannel = in.readUTF();

			if (subChannel.equalsIgnoreCase("serverstatus")) {
				String serverName = in.readUTF();

				ServerInfo server = ProxyServer.getInstance().getServerInfo(serverName);

				ProxiedPlayer send = ProxyServer.getInstance().getPlayer(e.getReceiver().toString());

				if (send == null) {
					return;
				}

				// proxymanager:pm -> serverstatus
				// Structure:
				// "proxymanager:pm"
				// "serverstatus"
				// server <String>
				// success <Boolean>
				// online <Boolean>
				// playerCount <int>
				// maxPlayers <int>

				if (server == null) {
					ByteArrayDataOutput out = ByteStreams.newDataOutput();
					out.writeUTF(serverName);
					out.writeBoolean(false);
					out.writeBoolean(false);
					out.writeInt(0);
					out.writeInt(0);
					send.getServer().sendData("proxymanager:pm", out.toByteArray());
				} else {
					server.ping(new Callback<ServerPing>() {
						@Override
						public void done(ServerPing result, Throwable error) {
							ByteArrayDataOutput out = ByteStreams.newDataOutput();
							if (error != null) {
								// error.printStackTrace();
								out.writeUTF("serverstatus");
								out.writeUTF(serverName);
								out.writeBoolean(true);
								out.writeBoolean(false);
								out.writeInt(0);
								out.writeInt(0);
							} else {
								//System.out.println("ping ok: o: " + result.getPlayers().getOnline() + " m: " + result.getPlayers().getMax() + " ts " + result.toString());
								out.writeUTF("serverstatus");
								out.writeUTF(serverName);
								out.writeBoolean(true);
								out.writeBoolean(true);
								out.writeInt(result.getPlayers().getOnline());
								out.writeInt(result.getPlayers().getMax());
							}
							
							send.getServer().sendData("proxymanager:pm", out.toByteArray());
						}
					});
				}

			}
		}
	}
}