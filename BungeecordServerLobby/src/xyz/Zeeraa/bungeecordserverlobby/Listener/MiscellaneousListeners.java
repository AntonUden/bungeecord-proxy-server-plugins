package xyz.Zeeraa.bungeecordserverlobby.Listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import xyz.Zeeraa.bungeecordserverlobby.BungeecordServerLobby;
import xyz.Zeeraa.bungeecordserverlobby.Misc.ItemBuilder;

public class MiscellaneousListeners implements Listener {
	private Location spawnLocation;

	public MiscellaneousListeners(Location spawnLocation) {
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

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getItem() != null) {
				Player p = e.getPlayer();

				if (e.getItem().getType() == Material.COMPASS) {
					BungeecordServerLobby.getInstance().getServerSelector().show(p);
					p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1F, 1F);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		e.setQuitMessage(null);
	}

	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onPlayerAchievementAwarded(PlayerAchievementAwardedEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}
}