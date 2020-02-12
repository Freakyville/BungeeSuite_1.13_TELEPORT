package com.minecraftdimensions.bungeesuiteteleports.redis;

import com.minecraftdimensions.bungeesuiteteleports.BungeeSuiteTeleports;
import com.minecraftdimensions.bungeesuiteteleports.managers.TeleportsManager;
import org.bukkit.Bukkit;
import redis.clients.jedis.*;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class RedisManager {
    private JedisPoolConfig poolConfig;
    private JedisPool jedisPool;
    private String host;
    private String password;
    private int port;
    private int timeout;
    private Jedis subjedis;

    private static RedisManager instance;

    private RedisManager() {

    }

    public RedisManager init(String host, String password, int port, int timeout) {
        this.host = host;
        this.password = password;
        this.port = port;
        this.timeout = timeout;
        poolConfig = buildPoolConfig();
        setup();
        listen();
        return instance;
    }

    public static RedisManager getInstance() {
        if (instance == null) {
            instance = new RedisManager();
        }
        return instance;
    }

    private void setup() {
        jedisPool = new JedisPool(poolConfig,
                host,
                port,
                timeout,
                password,
                false);
    }


    public void listen() {
        JedisShardInfo jedisShardInfo = new JedisShardInfo(host, port);
        jedisShardInfo.setPassword(password);
        subjedis = new Jedis(jedisShardInfo);
        CompletableFuture.runAsync(() -> {
            subjedis.subscribe(new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    try {
                        Bukkit.getLogger().info("TELEPORT REDIS RESPONSE: " + message);
                        String[] args = message.split(";");
                        if (args[0].equalsIgnoreCase("TeleportToPlayer")) {
                            if (args[1].equalsIgnoreCase(BungeeSuiteTeleports.server)) {
                                TeleportsManager.teleportPlayerToPlayer(args[2], args[3]);
                            }
                        } else if (args[0].equalsIgnoreCase("TeleportToLocation")) {
                            if (args[1].equalsIgnoreCase(BungeeSuiteTeleports.server)) {
                                String name = args[2];
                                String loc = args[3];
                                String[] locs = loc.split("~!~");
                                TeleportsManager.teleportPlayerToLocation(name, locs[1], Double.parseDouble(locs[2]), Double.parseDouble(locs[3]), Double.parseDouble(locs[4]));
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }, "TELEPORT_RESPONSE");
        });
    }

    public void publish(String data, String channel) {
        CompletableFuture.runAsync(() -> {
            Bukkit.getLogger().info("TELEPORT REDIS PUBLISH: " + data + " (" + channel + ")");
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.publish(channel, data);
            }
        });
    }

    private JedisPoolConfig buildPoolConfig() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(128);
        poolConfig.setMinIdle(16);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);
        return poolConfig;
    }
}
