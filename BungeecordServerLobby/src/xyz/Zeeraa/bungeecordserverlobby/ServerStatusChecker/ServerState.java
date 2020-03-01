package xyz.Zeeraa.bungeecordserverlobby.ServerStatusChecker;

public enum ServerState {
	ONLINE, OFFLINE;

	/**
	 * @param bool
	 * @return ONLINE on <code>true</code> and OFFLINE on <code>false</code>
	 */
	public static ServerState fromBoolean(boolean bool) {
		return bool ? ONLINE : OFFLINE;
	}
}