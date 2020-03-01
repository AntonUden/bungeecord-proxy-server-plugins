package xyz.Zeeraa.bungeecordserverlobby.ServerSelector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import xyz.Zeeraa.bungeecordserverlobby.BungeecordServerLobby;
import xyz.Zeeraa.bungeecordserverlobby.Misc.ItemBuilder;
import xyz.Zeeraa.bungeecordserverlobby.ServerStatusChecker.ServerState;
import xyz.Zeeraa.bungeecordserverlobby.ServerStatusChecker.ServerStatus;
import xyz.zeeraa.BungeecordServerCommons.Database.Objects.ServerConfiguration;

public class ServerSelector implements Listener {
	private HashMap<Integer, Inventory> serverMenuPages = new HashMap<Integer, Inventory>();
	private int pages = 0;
	private ServerIcons serverIcons;

	private int serversPerPage = 36;

	private int serverStartSlot = 18;

	public ServerSelector() {
		serverIcons = new ServerIcons();

		serverIcons.loadServerIcons(BungeecordServerLobby.getInstance().getServers());

		loadMenuPages();

		Bukkit.getScheduler().scheduleSyncRepeatingTask(BungeecordServerLobby.getInstance(), new Runnable() {
			@Override
			public void run() {
				if (Bukkit.getOnlinePlayers().size() > 0) {
					updateStatus();
				}
			}
		}, 20L, 20L);
	}

	/**
	 * Creates all menu pages or update pages if already loaded
	 */
	public void loadMenuPages() {
		serverMenuPages.clear();

		ItemStack previousPageIcon = new ItemBuilder(Material.PAPER, 1).setName("Previous page").build();
		ItemStack nextPageIcon = new ItemBuilder(Material.PAPER, 1).setName("Next page").build();
		ItemStack backgroundItem = new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability((short) 0).build();

		ArrayList<ServerConfiguration> serversToAdd = new ArrayList<ServerConfiguration>();
		for (ServerConfiguration sc : BungeecordServerLobby.getInstance().getServers()) {
			if (sc.isShowInServerList()) {
				if (serverIcons.hasServerIcon(sc)) {
					serversToAdd.add(sc);
				}
			}
		}

		pages = (int) Math.ceil(((double) serversToAdd.size()) / ((double) serversPerPage));

		for (int i = 1; i <= pages; i++) {
			Inventory menuPage = Bukkit.createInventory(new ServerMenuHolder(i, (i != 1), (i < pages)), 54, ChatColor.GREEN + "Server Selector. Page " + i + "/" + pages);

			for (int j = 0; j < menuPage.getSize(); j++) {
				menuPage.setItem(j, backgroundItem);
			}

			menuPage.setItem(0, previousPageIcon);
			menuPage.setItem(8, nextPageIcon);

			int slot = serverStartSlot;

			for (int j = 0; j < serversPerPage; j++) {
				int realIndex = ((i - 1) * serversPerPage) + j;

				if (realIndex >= serversToAdd.size()) {
					// end of server list
					break;
				}

				ServerConfiguration sc = serversToAdd.get(realIndex);

				ItemStack icon = serverIcons.getServerIcon(sc);

				if (icon == null) {
					continue;
				}

				ItemMeta meta = icon.getItemMeta();
				List<String> lore = meta.getLore();

				if (lore == null) {
					lore = new ArrayList<String>();
				}

				lore.add("");
				lore.add(ChatColor.RED + "Offline");
				lore.add(ChatColor.AQUA + "0/0 players online");

				meta.setLore(lore);
				icon.setItemMeta(meta);

				menuPage.setItem(slot, icon);
				((ServerMenuHolder) menuPage.getHolder()).serverName.put(slot, sc.getName());

				slot++;
			}

			// System.out.println("adding page " + i + " to page list");

			serverMenuPages.put(i, menuPage);
		}
	}

	/**
	 * Reloads all server icons from server list
	 */
	public void reloadIcons() {
		serverIcons.loadServerIcons(BungeecordServerLobby.getInstance().getServers());
	}

	/**
	 * Updates all server icons with data from ServerStatusManager. To reload all
	 * server data from the database use
	 * {@link BungeecordServerLobby#reloadServers()}
	 */
	public void updateStatus() {
		for (Integer i : serverMenuPages.keySet()) {
			ServerMenuHolder smh = (ServerMenuHolder) serverMenuPages.get(i).getHolder();
			for (int j = 0; j < serverMenuPages.get(i).getSize(); j++) {
				if (smh.serverName.containsKey(j)) {
					String server = smh.serverName.get(j);
					ServerStatus status = BungeecordServerLobby.getInstance().getServerStatusManager().getServerStatus(server);

					if (status != null) {
						ItemStack item = serverMenuPages.get(i).getItem(j);

						ItemMeta meta = item.getItemMeta();

						List<String> lore = meta.getLore();

						if (lore == null) {
							continue;
						}

						int loreSize = lore.size();

						lore.set(loreSize - 2, (status.getServerState() == ServerState.ONLINE ? ChatColor.GREEN + "Online" : ChatColor.RED + "Offline"));
						lore.set(loreSize - 1, ChatColor.AQUA + "" + status.getOnlinePlayers() + "/" + status.getMaxPlayers() + " player" + (status.getOnlinePlayers() == 0 ? "" : "s") + " online");

						meta.setLore(lore);
						item.setItemMeta(meta);
					}
				}
			}
		}

	}

	/**
	 * Shows page 1 of the menu to a player
	 * 
	 * @param player the player to show the menu to
	 */
	public void show(Player player) {
		this.show(player, 1);
	}

	/**
	 * @param player the player to show the menu to
	 * @param page   the page of the menu to show
	 */
	public void show(Player player, int page) {
		if (page > pages) {

			page = 1;
		}

		if (page < 1) {
			page = 1;
		}
		player.openInventory(serverMenuPages.get(page));
	}

	/* ---------- Listeners ---------- */
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		// e.getWhoClicked().sendMessage(e.getInventory().getHolder().getClass().getName());
		if (e.getInventory().getHolder() instanceof ServerMenuHolder) {
			e.setCancelled(true);

			int slot = e.getSlot();

			if (e.getClickedInventory().getHolder() instanceof ServerMenuHolder) {
				ServerMenuHolder smh = (ServerMenuHolder) e.getClickedInventory().getHolder();
				Player player = (Player) e.getWhoClicked();

				if (slot == 0) {
					player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1F, 1F);

					if (smh.hasPrevious()) {
						this.show(player, smh.getPageNumber() - 1);
					}
					return;
				}

				if (slot == 8) {
					player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1F, 1F);

					if (smh.hasNext()) {
						this.show(player, smh.getPageNumber() + 1);
					}
					return;
				}

				if (smh.serverName.containsKey(slot)) {
					String server = smh.serverName.get(slot);
					ServerConfiguration sc = BungeecordServerLobby.getInstance().getServerByName(server);

					player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1F, 1F);

					if (sc == null) {
						player.sendMessage(ChatColor.RED + "ERR:BAD_SERVER_CONFIGURATION");
						return;
					}

					if (sc.isRequireDirectJoin()) {
						player.kickPlayer(ChatColor.GOLD + "Please join with ip " + ChatColor.AQUA + sc.getDomain() + ChatColor.GOLD + " to join " + sc.getDisplayName());
					} else {

						player.sendMessage(ChatColor.GOLD + "Joining " + server);

						ByteArrayDataOutput out = ByteStreams.newDataOutput();
						out.writeUTF("Connect");
						out.writeUTF(server);

						player.sendPluginMessage(BungeecordServerLobby.getInstance(), "BungeeCord", out.toByteArray());
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getItem() != null) {
				Player p = e.getPlayer();

				if (e.getItem().getType() == Material.COMPASS) {
					this.show(p);
					p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1F, 1F);
				}
			}
		}
	}
}
