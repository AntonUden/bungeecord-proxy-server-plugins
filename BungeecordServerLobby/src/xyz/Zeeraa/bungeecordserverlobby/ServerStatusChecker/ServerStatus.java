package xyz.Zeeraa.bungeecordserverlobby.ServerStatusChecker;

public class ServerStatus {
	private int onlinePlayers;
	private int maxPlayers;
	private ServerState serverState;

	public int onlineStateCheckCountdown = 0;

	public ServerStatus() {
		onlinePlayers = 0;
		maxPlayers = 0;
		serverState = ServerState.OFFLINE;
		onlineStateCheckCountdown = 0;
	}

	public int getOnlinePlayers() {
		return onlinePlayers;
	}

	public ServerStatus setOnlinePlayers(int onlinePlayers) {
		this.onlinePlayers = onlinePlayers;
		return this;
	}

	public ServerState getServerState() {
		return serverState;
	}

	public ServerStatus setServerState(ServerState serverState) {
		this.serverState = serverState;
		return this;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public ServerStatus setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
		return this;
	}

	public ServerStatus set(int onlinePlayers, int maxPlayers, ServerState serverState) {
		this.onlinePlayers = onlinePlayers;
		this.maxPlayers = maxPlayers;
		this.serverState = serverState;
		return this;
	}

	public ServerStatus setOffline() {
		onlinePlayers = 0;
		maxPlayers = 0;
		serverState = ServerState.OFFLINE;
		return this;
	}

	public boolean isOnline() {
		return serverState == ServerState.ONLINE;
	}
}