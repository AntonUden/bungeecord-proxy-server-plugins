package xyz.Zeeraa.bungeecordserverlobby;

import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.Zeeraa.bungeecordserverlobby.Commands.ReloadServersCommand;
import xyz.Zeeraa.bungeecordserverlobby.Commands.ServerListCommand;
import xyz.Zeeraa.bungeecordserverlobby.Listener.AfkKickListener;
import xyz.Zeeraa.bungeecordserverlobby.Listener.KickJoiningPlayers;
import xyz.Zeeraa.bungeecordserverlobby.Listener.MiscellaneousListeners;
import xyz.Zeeraa.bungeecordserverlobby.Listener.SpawnListener;
import xyz.Zeeraa.bungeecordserverlobby.Listener.WeatherListener;
import xyz.Zeeraa.bungeecordserverlobby.ServerSelector.ServerIcons;
import xyz.Zeeraa.bungeecordserverlobby.ServerSelector.ServerSelector;
import xyz.Zeeraa.bungeecordserverlobby.ServerStatusChecker.ServerStatusManager;
import xyz.zeeraa.BungeecordServerCommons.Database.DBConnection;
import xyz.zeeraa.BungeecordServerCommons.Database.Objects.ServerConfiguration;
import xyz.zeeraa.BungeecordServerCommons.Log.Log;

public class BungeecordServerLobby extends JavaPlugin implements Listener {
	private ArrayList<ServerConfiguration> servers = new ArrayList<ServerConfiguration>();
	private ServerSelector serverSelector;
	private ServerStatusManager serverStatusManager;
	private AfkKickListener afkKickListener;
	private SpawnListener spawnListener;

	private static BungeecordServerLobby instance;

	/**
	 * @return instance of {@link BungeecordServerLobby}
	 */
	public static BungeecordServerLobby getInstance() {
		return instance;
	}

	/**
	 * @return {@link ArrayList} with all {@link ServerConfiguration}
	 */
	public ArrayList<ServerConfiguration> getServers() {
		return servers;
	}

	/**
	 * @return instance of {@link ServerStatusManager}
	 */
	public ServerStatusManager getServerStatusManager() {
		return serverStatusManager;
	}

	/**
	 * @return instance of {@link ServerSelector}
	 */
	public ServerSelector getServerSelector() {
		return serverSelector;
	}

	/**
	 * @return instance of {@link AfkKickListener}
	 */
	public AfkKickListener getAfkKickListener() {
		return afkKickListener;
	}

	/**
	 * @return instance of {@link SpawnListener}
	 */
	public SpawnListener getSpawnListener() {
		return spawnListener;
	}

	/**
	 * @param server Name of the server to get
	 * @return returns the {@link ServerConfiguration} for a specific server or
	 *         <code>null</code> if the server does not exist
	 */
	public ServerConfiguration getServerByName(String server) {
		for (ServerConfiguration sc : servers) {
			if (sc.getName().equals(server)) {
				return sc;
			}
		}
		return null;
	}

	/**
	 * @param server Name of the server to check
	 * @return returns <code>true</code> if the server exists
	 */
	public boolean serverExists(String server) {
		for (ServerConfiguration sc : servers) {
			if (sc.getName().equals(server)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onEnable() {
		Log.setLogger(Bukkit.getLogger());
		instance = this;

		saveDefaultConfig();
		serverExists("");
		if (!DBConnection.init(getConfig().getString("mysql.driver"), getConfig().getString("mysql.host"), getConfig().getString("mysql.username"), getConfig().getString("mysql.password"), getConfig().getString("mysql.database"))) {
			Log.warning("Failed to connect to database! kicking all players");
			denyJoin();
			return;
		}

		try {
			servers = DBConnection.getServers();
		} catch (Exception e) {
			Log.warning("Failed to load servers! kicking all players");
			denyJoin();
			return;
		}

		Location spawnLocation = new Location(this.getServer().getWorlds().get(0), getConfig().getDouble("spawn.x"), getConfig().getDouble("spawn.y"), getConfig().getDouble("spawn.z"), (float) getConfig().getDouble("spawn.yaw"), (float) getConfig().getDouble("spawn.pitch"));

		serverStatusManager = new ServerStatusManager();
		serverSelector = new ServerSelector();
		afkKickListener = new AfkKickListener(getConfig().getInt("afk-kick-delay"));
		spawnListener = new SpawnListener(spawnLocation);

		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "proxymanager:pm");

		this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", serverStatusManager);
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "proxymanager:pm", serverStatusManager);

		this.getServer().clearRecipes();

		this.getServer().getPluginManager().registerEvents(this, this);
		this.getServer().getPluginManager().registerEvents(new WeatherListener(), this);
		this.getServer().getPluginManager().registerEvents(new MiscellaneousListeners(), this);
		this.getServer().getPluginManager().registerEvents(serverSelector, this);
		this.getServer().getPluginManager().registerEvents(serverStatusManager, this);
		this.getServer().getPluginManager().registerEvents(afkKickListener, this);
		this.getServer().getPluginManager().registerEvents(spawnListener, this);

		this.getCommand("reloadservers").setExecutor(new ReloadServersCommand());
		this.getCommand("serverlist").setExecutor(new ServerListCommand());
	}

	@Override
	public void onDisable() {
		Bukkit.getScheduler().cancelTasks(this);

		try {
			Log.info("Closing database connection");
			DBConnection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Log.warning("Failed to close the database connection");
		}
	}

	/**
	 * Deny all players from joining
	 */
	private void denyJoin() {
		getServer().getPluginManager().registerEvents(new KickJoiningPlayers(), this);
		for (Player p : getServer().getOnlinePlayers()) {
			p.kickPlayer(ChatColor.RED + "Internal server error. Please try again later");
		}
	}

	/**
	 * Reload list of {@link ServerConfiguration} from database and updates
	 * {@link ServerIcons} and {@link ServerSelector}
	 * 
	 * @return <code>true</code> if successful
	 */
	public boolean reloadServers() {
		try {
			servers = DBConnection.getServers();
		} catch (Exception e) {
			return false;
		}
		serverSelector.reloadIcons();
		serverSelector.loadMenuPages();

		return true;
	}
}