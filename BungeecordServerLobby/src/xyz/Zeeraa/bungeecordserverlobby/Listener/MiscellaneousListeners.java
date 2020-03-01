package xyz.Zeeraa.bungeecordserverlobby.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MiscellaneousListeners implements Listener {
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			if (((Player) e.getDamager()).isOp()) {
				return;
			}

			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if (e.getPlayer() != null) {
			if (e.getPlayer().isOp()) {
				return;
			}
		}

		e.setCancelled(true);
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if (e.getPlayer() != null) {
			if (e.getPlayer().isOp()) {
				return;
			}
		}

		e.setCancelled(true);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		e.setQuitMessage(null);
	}

	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
		if (e.getPlayer().isOp()) {
			return;
		}

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