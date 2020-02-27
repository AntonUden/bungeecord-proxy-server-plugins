package xyz.Zeeraa.bungeecordserverlobby.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.Zeeraa.bungeecordserverlobby.BungeecordServerLobby;

public class ServerListCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			BungeecordServerLobby.getInstance().getServerSelector().show(p);
			return true;
		} else {
			sender.sendMessage(ChatColor.DARK_RED + "This command can only be used by players");
		}
		
		return false;
	}
}