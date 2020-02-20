package xyz.Zeeraa.bungeecordserverlobby;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.Zeeraa.bungeecordserverlobby.Listener.AfkKickListener;
import xyz.Zeeraa.bungeecordserverlobby.Listener.KickJoiningPlayers;
import xyz.Zeeraa.bungeecordserverlobby.Listener.MiscellaneousListeners;
import xyz.Zeeraa.bungeecordserverlobby.Listener.WeatherListener;
import xyz.Zeeraa.bungeecordserverlobby.ServerSelector.ServerSelector;
import xyz.Zeeraa.bungeecordserverlobby.ServerStatusChecker.ServerStatusManager;
import xyz.zeeraa.BungeecordServerCommons.Database.DBConnection;
import xyz.zeeraa.BungeecordServerCommons.Database.Objects.ServerConfiguration;
import xyz.zeeraa.BungeecordServerCommons.Log.SLog;

public class BungeecordServerLobby extends JavaPlugin implements Listener {
	private ArrayList<ServerConfiguration> servers = new ArrayList<ServerConfiguration>();
	private ServerSelector serverSelector;
	private ServerStatusManager serverStatusManager;
	private AfkKickListener afkKickListener;

	private static BungeecordServerLobby instance;

	public static BungeecordServerLobby getInstance() {
		return instance;
	}

	public ArrayList<ServerConfiguration> getServers() {
		return servers;
	}

	public ServerStatusManager getServerStatusManager() {
		return serverStatusManager;
	}

	public ServerConfiguration getServerByName(String server) {
		for (ServerConfiguration sc : servers) {
			if (sc.getName().equals(server)) {
				return sc;
			}
		}
		return null;
	}

	public ServerSelector getServerSelector() {
		return serverSelector;
	}

	private void denyJoin() {
		getServer().getPluginManager().registerEvents(new KickJoiningPlayers(), this);
		for (Player p : getServer().getOnlinePlayers()) {
			p.kickPlayer(ChatColor.RED + "Internal server error. Please try again later");
		}
	}

	@Override
	public void onEnable() {
		instance = this;

		saveDefaultConfig();

		if (!DBConnection.init(getConfig().getString("mysql.driver"), getConfig().getString("mysql.host"), getConfig().getString("mysql.username"), getConfig().getString("mysql.password"), getConfig().getString("mysql.database"))) {
			SLog.warning("Failed to connect to database! kicking all players");
			denyJoin();
			return;
		}

		try {
			servers = DBConnection.getServers();
		} catch (Exception e) {
			SLog.warning("Failed to load servers! kicking all players");
			denyJoin();
			return;
		}

		serverStatusManager = new ServerStatusManager();
		serverSelector = new ServerSelector();
		afkKickListener = new AfkKickListener(getConfig().getInt("afk-kick-delay"));

		Location spawnLocation = new Location(this.getServer().getWorlds().get(0), getConfig().getDouble("spawn.x"), getConfig().getDouble("spawn.y"), getConfig().getDouble("spawn.z"), (float) getConfig().getDouble("spawn.yaw"), (float) getConfig().getDouble("spawn.pitch"));

		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "proxymanager:pm");

		this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", serverStatusManager);
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "proxymanager:pm", serverStatusManager);

		this.getServer().clearRecipes();
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getServer().getPluginManager().registerEvents(new WeatherListener(), this);
		this.getServer().getPluginManager().registerEvents(new MiscellaneousListeners(spawnLocation), this);
		this.getServer().getPluginManager().registerEvents(serverSelector, this);
		this.getServer().getPluginManager().registerEvents(serverStatusManager, this);
		this.getServer().getPluginManager().registerEvents(afkKickListener, this);

	}

	@Override
	public void onDisable() {
		Bukkit.getScheduler().cancelTasks(this);
	}
}