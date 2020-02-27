package xyz.Zeeraa.bungeecordproxymanager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import xyz.Zeeraa.bungeecordproxymanager.Anticheat.Anticheat;
import xyz.Zeeraa.bungeecordproxymanager.BanManager.BanManager;
import xyz.Zeeraa.bungeecordproxymanager.Configuration.ConfigurationManager;
import xyz.Zeeraa.bungeecordproxymanager.Listeners.DomainListener;
import xyz.Zeeraa.bungeecordproxymanager.Listeners.PlayerLogger;
import xyz.Zeeraa.bungeecordproxymanager.Listeners.PluginMessageListener;
import xyz.zeeraa.BungeecordServerCommons.Database.DBConnection;
import xyz.zeeraa.BungeecordServerCommons.Log.BLog;
import xyz.zeeraa.BungeecordServerCommons.ModBlacklist.ModBlacklist;
import xyz.Zeeraa.bungeecordproxymanager.Listeners.DenyJoinListener;

public class BungeecordProxyManager extends Plugin implements Listener {
	private Configuration configuration;
	private ConfigurationManager configurationManager;
	private BanManager banManager;
	private Anticheat anticheat;
	private ModBlacklist modBlacklist;
	
	private static BungeecordProxyManager instance;
	
	public static BungeecordProxyManager getInstance() {
		return instance;
	}
	
	public ConfigurationManager getConfigurationManager() {
		return configurationManager;
	}
	
	public Anticheat getAnticheat() {
		return anticheat;
	}
	
	public ModBlacklist getModBlacklist() {
		return modBlacklist;
	}
	
	@Override
	public void onEnable() {
		instance = this;
		configurationManager = new ConfigurationManager();
		try {
			if (!getDataFolder().exists()) {
				getDataFolder().mkdir();
			}
			
			File cfgfile = new File(getDataFolder(), "config.yml");

		       
            if (!cfgfile.exists()) {
                try (InputStream in = getResourceAsStream("config.yml")) {
                    Files.copy(in, cfgfile.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
			configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));

			getProxy().getPluginManager().registerListener(this, this);
			
			DBConnection.init(configuration.getString("mysql.driver"), configuration.getString("mysql.host"), configuration.getString("mysql.username"), configuration.getString("mysql.password"), configuration.getString("mysql.database"));
			
			if(!configurationManager.loadConfiguration()) {
				BLog.warning("Failed to load configuration. Kicking all connecting players");
				getProxy().getPluginManager().registerListener(this, new DenyJoinListener());
				return;
			}
			
			configurationManager.registerServers();
		
			banManager = new BanManager(this);
			
			modBlacklist = new ModBlacklist();
			
			anticheat = new Anticheat();
			
			getProxy().registerChannel("proxymanager:pm");
			
			BLog.info("Register listeners");
			getProxy().getPluginManager().registerListener(this, new PlayerLogger());
			getProxy().getPluginManager().registerListener(this, new DomainListener());
			getProxy().getPluginManager().registerListener(this, new PluginMessageListener());
			getProxy().getPluginManager().registerListener(this, banManager);
			getProxy().getPluginManager().registerListener(this, anticheat);
		} catch (IOException e) {
			e.printStackTrace();
			BLog.warning("Failed to enable BungeecordProxyManager. Kicking all connecting players");
			getProxy().getPluginManager().registerListener(this, new DenyJoinListener());
			return;
		}
		BLog.info("Done");
	}

	@Override
	public void onDisable() {
		if(banManager != null) {
			banManager.stop();
		}
		
		getProxy().getPluginManager().unregisterListeners((Plugin) this);
	}
}