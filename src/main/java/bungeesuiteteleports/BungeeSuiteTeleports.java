package bungeesuiteteleports;

import com.minecraftdimensions.bungeesuiteteleports.commands.*;
import com.minecraftdimensions.bungeesuiteteleports.listeners.TeleportsListener;
import com.minecraftdimensions.bungeesuiteteleports.listeners.TeleportsMessageListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BungeeSuiteTeleports extends JavaPlugin {


	public static String OUTGOING_PLUGIN_CHANNEL = "bsuite:tp-in";
	static String INCOMING_PLUGIN_CHANNEL = "bsuite:tp-out";
	public static BungeeSuiteTeleports instance;

	@Override
	public void onEnable() {
		instance=this;
		registerListeners();
		registerChannels();
		registerCommands();
	}
	
	private void registerCommands() {
		getCommand("tp").setExecutor(new TPCommand());
		getCommand("tphere").setExecutor(new TPHereCommand());
		getCommand("tpall").setExecutor(new TPAllCommand());
		getCommand("tpa").setExecutor(new TPACommand());
		getCommand("tpahere").setExecutor(new TPAHereCommand());
		getCommand("tpaccept").setExecutor(new TPAcceptCommand());
		getCommand("tpdeny").setExecutor(new TPDenyCommand());
		getCommand("back").setExecutor(new BackCommand());
		getCommand("tptoggle").setExecutor(new ToggleCommand());
	}

	private void registerChannels() {
		Bukkit.getMessenger().registerIncomingPluginChannel(this,
				INCOMING_PLUGIN_CHANNEL, new TeleportsMessageListener());
		Bukkit.getMessenger().registerOutgoingPluginChannel(this,
				OUTGOING_PLUGIN_CHANNEL);
	}

	private void registerListeners() {
		getServer().getPluginManager().registerEvents(
				new TeleportsListener(), this);
	}


}
