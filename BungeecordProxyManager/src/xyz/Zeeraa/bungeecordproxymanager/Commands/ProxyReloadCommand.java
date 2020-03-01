package xyz.Zeeraa.bungeecordproxymanager.Commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import xyz.Zeeraa.bungeecordproxymanager.BungeecordProxyManager;

public class ProxyReloadCommand extends Command {
	public ProxyReloadCommand() {
		super("proxyreload");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender instanceof ProxiedPlayer) {
			sender.sendMessage(new TextComponent(ChatColor.RED + "Only the console can reload the proxy!"));
			return;
		}
		
		sender.sendMessage(new TextComponent(ChatColor.GOLD + "Reloading mod blacklist"));
		if(BungeecordProxyManager.getInstance().getModBlacklist().reload()) {
			sender.sendMessage(new TextComponent(ChatColor.GREEN + "Mod blacklist reloaded"));
		} else {
			sender.sendMessage(new TextComponent(ChatColor.RED + "Failed to reload mod blacklist"));
		}
		
		sender.sendMessage(new TextComponent(ChatColor.GOLD + "Reloading server configuration"));
		if(BungeecordProxyManager.getInstance().getConfigurationManager().loadConfiguration()) {
			sender.sendMessage(new TextComponent(ChatColor.GREEN + "Server configuration reloaded"));
		} else {
			sender.sendMessage(new TextComponent(ChatColor.RED + "Failed to reload server configuration"));
		}
	}
}