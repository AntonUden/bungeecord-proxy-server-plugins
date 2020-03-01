package xyz.Zeeraa.bungeecordserverlobby.Listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import xyz.Zeeraa.bungeecordserverlobby.BungeecordServerLobby;
import xyz.Zeeraa.bungeecordserverlobby.Misc.ItemBuilder;

public class SpawnListener implements Listener {
	private Location spawnLocation;

	public SpawnListener(Location spawnLocation) {
		this.spawnLocation = spawnLocation;
	}

	/**
	 * Get the server spawn {@link Location}
	 * 
	 * @return {@link Location} of the server spawn
	 */
	public Location getSpawnLocation() {
		return spawnLocation;
	}

	/**
	 * Set spawn {@link Location} for the server
	 * 
	 * @param spawnLocation {@link Location} for server spawn
	 */
	public void setSpawnLocation(Location spawnLocation) {
		this.spawnLocation = spawnLocation;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();

		e.setJoinMessage(null);

		for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
			pl.hidePlayer(p);
			p.hidePlayer(pl);
		}

		p.teleport(spawnLocation);
		p.setCompassTarget(spawnLocation);

		p.spigot().setCollidesWithEntities(false);

		p.getInventory().clear();
		p.getInventory().addItem(new ItemBuilder(Material.COMPASS, 1).setName("Server selector").addLore("Right click to show server menu").build());
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();

		Bukkit.getScheduler().scheduleSyncDelayedTask(BungeecordServerLobby.getInstance(), new Runnable() {
			@Override
			public void run() {
				p.spigot().respawn();
				p.teleport(spawnLocation);
				p.getInventory().clear();
				p.getInventory().addItem(new ItemBuilder(Material.COMPASS, 1).setName("Server selector").addLore("Right click to show server menu").build());
			}
		}, 10L);
	}
}