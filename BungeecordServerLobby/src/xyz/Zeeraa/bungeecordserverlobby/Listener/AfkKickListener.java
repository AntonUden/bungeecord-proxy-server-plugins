package xyz.Zeeraa.bungeecordserverlobby.Listener;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import xyz.Zeeraa.bungeecordserverlobby.BungeecordServerLobby;

public class AfkKickListener implements Listener {
	private HashMap<UUID, Integer> playerKickDelay = new HashMap<UUID, Integer>();
	private int afkKickDelay;

	/**
	 * Get delay in seconds for players to be AFK kicked
	 * 
	 * @return AFK kick delay
	 */
	public int getAfkKickDelay() {
		return afkKickDelay;
	}

	/**
	 * Set delay in seconds for players to be AFK kicked
	 * 
	 * @param afkKickDelay AFK kick delay
	 */
	public void setAfkKickDelay(int afkKickDelay) {
		this.afkKickDelay = afkKickDelay;
	}

	/**
	 * Use default AFK kick delay of 240
	 */
	public AfkKickListener() {
		this(240);
	}

	/**
	 * Use custom AFK kick delay
	 * 
	 * @param afkKickDelay AFK kick delay
	 */
	public AfkKickListener(int afkKickDelay) {
		this.afkKickDelay = afkKickDelay;

		Bukkit.getScheduler().scheduleSyncRepeatingTask(BungeecordServerLobby.getInstance(), new Runnable() {
			@Override
			public void run() {
				for (UUID uuid : playerKickDelay.keySet()) {
					int timeLeft = playerKickDelay.get(uuid);
					if (timeLeft > 0) {
						playerKickDelay.put(uuid, timeLeft - 1);
					} else {
						Player p = Bukkit.getServer().getPlayer(uuid);

						if (p == null) {
							playerKickDelay.remove(uuid);
						} else {
							p.kickPlayer(ChatColor.RED + "You have been kicked for being afk");
						}
					}
				}
			}
		}, 20L, 20L);
	}

	/**
	 * Reset player AFK kick delay
	 * 
	 * @param e {@link PlayerMoveEvent}
	 */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void playerMoveEvent(PlayerMoveEvent e) {
		Location movedFrom = e.getFrom();
		Location movedTo = e.getTo();
		if ((movedFrom.getX() != movedTo.getX()) || (movedFrom.getY() != movedTo.getY()) || (movedFrom.getZ() != movedTo.getZ())) {
			playerKickDelay.put(e.getPlayer().getUniqueId(), afkKickDelay);
		}
	}

	/**
	 * Add player from AFK kick delay list
	 * 
	 * @param e {@link PlayerJoinEvent}
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent e) {
		playerKickDelay.put(e.getPlayer().getUniqueId(), afkKickDelay);
	}

	/**
	 * Remove player from AFK kick delay list
	 * 
	 * @param e {@link PlayerQuitEvent}
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent e) {
		playerKickDelay.remove(e.getPlayer().getUniqueId());
	}
}