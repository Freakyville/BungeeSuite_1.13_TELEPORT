package com.minecraftdimensions.bungeesuiteteleports.managers;

import com.minecraftdimensions.bungeesuiteteleports.BungeeSuiteTeleports;
import com.minecraftdimensions.bungeesuiteteleports.redis.RedisManager;
import com.minecraftdimensions.bungeesuiteteleports.tasks.PluginMessageTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class TeleportsManager {


    public static HashMap<String, Player> pendingTeleports = new HashMap<String, Player>();
    public static HashMap<String, Location> pendingTeleportLocations = new HashMap<String, Location>();
    public static ArrayList<Player> ignoreTeleport = new ArrayList<Player>();

    private static void sendRequest(String type, String sender, String targetPlayer){
        StringBuilder sb = new StringBuilder();
        sb.append(type).append(";");
        sb.append(sender).append(";");
        sb.append(targetPlayer);
        RedisManager.getInstance().publish(sb.toString(), "TELEPORT_REQUEST");
    }

    public static void tpAll( CommandSender sender, String targetPlayer ) {
        sendRequest("TpAll", sender.getName(), targetPlayer);
    }

    public static void tpaRequest( CommandSender sender, String targetPlayer ) {
        sendRequest("TpaRequest", sender.getName(), targetPlayer);
    }

    public static void tpaHereRequest( CommandSender sender, String targetPlayer ) {
        sendRequest("TpaHereRequest", sender.getName(), targetPlayer);
    }

    public static void tpAccept( CommandSender sender ) {
        StringBuilder sb = new StringBuilder();
        sb.append("TpAccept").append(";");
        sb.append(sender.getName());
        RedisManager.getInstance().publish(sb.toString(), "TELEPORT_REQUEST");
    }

    public static void tpDeny( String sender ) {
        StringBuilder sb = new StringBuilder();
        sb.append("TpDeny").append(";");
        sb.append(sender);
        RedisManager.getInstance().publish(sb.toString(), "TELEPORT_REQUEST");
    }

    public static void sendDeathBackLocation( Player p ) {
        StringBuilder sb = new StringBuilder();
        sb.append("PlayersDeathBackLocation").append(";");
        sb.append(p.getName()).append(";");
        Location l = p.getLocation();
        sb.append(l.getWorld().getName()).append(";");
        sb.append(l.getX()).append(";");
        sb.append(l.getY()).append(";");
        sb.append(l.getZ());
        RedisManager.getInstance().publish(sb.toString(), "TELEPORT_REQUEST");
    }

    public static void sendTeleportBackLocation( Player p) {
        StringBuilder sb = new StringBuilder();
        sb.append("PlayersTeleportBackLocation").append(";");
        sb.append(p.getName()).append(";");
        Location l = p.getLocation();
        sb.append(l.getWorld().getName()).append(";");
        sb.append(l.getX()).append(";");
        sb.append(l.getY()).append(";");
        sb.append(l.getZ());
        RedisManager.getInstance().publish(sb.toString(), "TELEPORT_REQUEST");
    }

    public static void sendPlayerBack( CommandSender sender ) {
        StringBuilder sb = new StringBuilder();
        sb.append("SendPlayerBack").append(";");
        sb.append(sender.hasPermission( "bungeesuite.teleports.back.death" )).append(";");
        sb.append(sender.hasPermission( "bungeesuite.teleports.back.teleport" ));
        RedisManager.getInstance().publish(sb.toString(), "TELEPORT_REQUEST");
    }

    public static void toggleTeleports( String name ) {
        RedisManager.getInstance().publish("ToggleTeleports;" + name, "TELEPORT_REQUEST");
    }

    public static void teleportPlayerToPlayer( final String player, String target ) {
        Player p = Bukkit.getPlayer( player );
        Player t = Bukkit.getPlayer( target );
        if ( p != null ) {
            p.teleport( t );
        } else {
            pendingTeleports.put( player, t );
            //clear pending teleport if they dont connect
            Bukkit.getScheduler().runTaskLaterAsynchronously( BungeeSuiteTeleports.instance, new Runnable() {
                @Override
                public void run() {
                    if ( pendingTeleports.containsKey( player ) ) {
                        pendingTeleports.remove( player );
                    }

                }
            }, 100 );
        }
    }

    public static void teleportPlayerToLocation( final String player, String world, double x, double y, double z ) {
        Location t = new Location( Bukkit.getWorld( world ), x, y, z );
        Player p = Bukkit.getPlayer( player );
        if ( p != null ) {
            p.teleport( t );
        } else {
            pendingTeleportLocations.put( player, t );
            //clear pending teleport if they dont connect
            Bukkit.getScheduler().runTaskLaterAsynchronously( BungeeSuiteTeleports.instance, new Runnable() {
                @Override
                public void run() {
                    if ( pendingTeleportLocations.containsKey( player ) ) {
                        pendingTeleportLocations.remove( player );
                    }
                }
            }, 100 );
        }
    }

    public static void teleportToPlayer( CommandSender sender, String player, String target ) {
        StringBuilder sb = new StringBuilder();
        sb.append("TeleportToPlayer").append(";");
        sb.append(sender.getName()).append(";");
        sb.append(player).append(";");
        sb.append(target).append(";");
        sb.append(sender.hasPermission( "bungeesuite.teleports.tp.silent" )).append(";");
        sb.append(sender.hasPermission( "bungeesuite.teleports.tp.bypass" ));
        RedisManager.getInstance().publish(sb.toString(), "TELEPORT_REQUEST");
    }

    public static void teleportToLocation( String player, String loc ) {
        StringBuilder sb = new StringBuilder();
        sb.append("TeleportToLocation").append(";");
        sb.append(player).append(";");
        sb.append(loc);
        RedisManager.getInstance().publish(sb.toString(), "TELEPORT_REQUEST");
    }

    public static void sendVersion() {
        StringBuilder sb = new StringBuilder();
        sb.append("SendVersion").append(";");
        sb.append(ChatColor.RED + "Teleports - " + ChatColor.GOLD).append(BungeeSuiteTeleports.instance.getDescription().getVersion());
        RedisManager.getInstance().publish(sb.toString(), "TELEPORT_REQUEST");
    }
}
