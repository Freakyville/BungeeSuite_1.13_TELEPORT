package com.minecraftdimensions.bungeesuiteteleports;

import com.minecraftdimensions.bungeesuiteteleports.commands.*;
import com.minecraftdimensions.bungeesuiteteleports.listeners.TeleportsListener;
import com.minecraftdimensions.bungeesuiteteleports.listeners.TeleportsMessageListener;
import com.minecraftdimensions.bungeesuiteteleports.managers.CooldownManager;
import com.minecraftdimensions.bungeesuiteteleports.redis.RedisManager;
import io.github.freakyville.utils.config.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class BungeeSuiteTeleports extends JavaPlugin {


    public static String OUTGOING_PLUGIN_CHANNEL = "bsuite:tp-in";
    static String INCOMING_PLUGIN_CHANNEL = "bsuite:tp-out";
    public static BungeeSuiteTeleports instance;
    public static String server;

    @Override
    public void onEnable() {
        instance = this;
        registerListeners();
        registerChannels();
        registerCommands();

        ConfigHandler configHandler = new ConfigHandler(instance, "config.yml");

        server = configHandler.getString("server");
        Map<String, Map<String, Integer>> cooldowns = new HashMap<>();
        for (String command : configHandler.getConfigSection("cooldowns").getKeys(false)) {
            cooldowns.putIfAbsent(command, new HashMap<>());
            for (String perm : configHandler.getConfigSection("cooldowns." + command).getKeys(false)) {
                cooldowns.get(command).put(perm, configHandler.getInt("cooldowns." + command + "." + perm));
            }
        }
        CooldownManager.getInstance().setCooldowns(cooldowns);

        RedisManager.getInstance().init(configHandler.getString("host"), configHandler.getString("password"), configHandler.getInt("port"), configHandler.getInt("timeout"));
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
