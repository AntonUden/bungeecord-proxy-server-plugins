package xyz.Zeeraa.bungeecordserverlobby.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.Zeeraa.bungeecordserverlobby.BungeecordServerLobby;

public class ReloadServersCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(!(p.isOp() || p.hasPermission("bungeecordserverlobby.reloadservers"))) {
				sender.sendMessage(ChatColor.DARK_RED + "You don't have permission to use this command");
				return false;
			}
		}
		
		sender.sendMessage(ChatColor.GOLD + "Reloading server list...");
		if(BungeecordServerLobby.getInstance().reloadServers()) {
			sender.sendMessage(ChatColor.GREEN + "Server list reloaded");
		} else {
			sender.sendMessage(ChatColor.RED + "Failed to reload server list");
		}
		
		return true;
	}
}