package xyz.Zeeraa.bungeecordproxymanager.Anticheat;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import xyz.Zeeraa.bungeecordproxymanager.BungeecordProxyManager;
import xyz.Zeeraa.bungeecordproxymanager.BanManager.BanManager;
import xyz.zeeraa.BungeecordServerCommons.Database.Objects.BlacklistedMod;
import xyz.zeeraa.BungeecordServerCommons.Log.Log;

public class PlayerModlistChecker implements Runnable {
	private ScheduledTask task;
	private UUID playerUuid;
	private int checkCount;

	/**
	 * Starts task to check a players mod list to see if the player has any
	 * blacklisted mods
	 * 
	 * @param playerUuid {@link UUID} of player to check
	 */
	public PlayerModlistChecker(UUID playerUuid) {
		this.playerUuid = playerUuid;
		this.checkCount = 0;
		this.task = ProxyServer.getInstance().getScheduler().schedule(BungeecordProxyManager.getInstance(), this, 10, 10, TimeUnit.SECONDS);
	}

	/**
	 * Cancel the check
	 */
	private void cancel() {
		task.cancel();
		BungeecordProxyManager.getInstance().getAnticheat().setChecked(playerUuid);
	}

	/**
	 * Checks player for mod list and ban player if blacklisted mods is detected
	 */
	@Override
	public void run() {
		checkCount++;
		if (checkCount > 5) {
			Log.info("Player mod list check for " + playerUuid.toString() + " timed out");
			cancel();
			return;
		}

		ProxiedPlayer p = ProxyServer.getInstance().getPlayer(playerUuid);
		if (p == null) {
			cancel();
			return;
		}

		Map<String, String> modList = p.getModList();
		if (modList.size() != 0) {
			Log.info("Player mod list received for " + p.getName());
			for (String mod : modList.keySet()) {
				if (BungeecordProxyManager.getInstance().getModBlacklist().isBlacklisted(mod)) {
					BlacklistedMod bm = BungeecordProxyManager.getInstance().getModBlacklist().getBlacklistedMod(mod);

					Log.info("Blacklisted mod detected for player: " + p.getName() + " mod name: " + mod);

					String message = "§cOur anti cheat has detected that you have a mod installed that can be used for cheating / malicious activities\n";
					message += "§e" + bm.getBanMessage() + "\n";
					message += "§6Detected mod: §b" + mod + " " + modList.get(mod) + "\n";

					String comment = "Modlist anitcheat ban. detected mods: ";

					for (String m : modList.keySet()) {
						comment += "[" + m + "|" + modList.get(m) + "] ";
					}

					comment += ". Trigger: [" + mod + "|" + modList.get(mod) + "].";

					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date());
					cal.add(Calendar.HOUR_OF_DAY, bm.getBanHours());

					BanManager.banPlayer(p.getUniqueId(), p.getName(), message, comment, cal.getTime());

					break;
				}
			}

			cancel();
			return;
		}
	}
}