package xyz.Zeeraa.bungeecordserverlobby.ServerStatusChecker;

public enum ServerState {
	ONLINE, OFFLINE;
	
	public static ServerState fromBoolean(boolean bool) {
		return bool ? ONLINE : OFFLINE; 
	}
}