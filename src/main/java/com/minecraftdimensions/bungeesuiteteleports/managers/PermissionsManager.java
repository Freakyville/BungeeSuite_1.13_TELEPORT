package com.minecraftdimensions.bungeesuiteteleports.managers;

import com.minecraftdimensions.bungeesuiteteleports.BungeeSuiteTeleports;
import org.bukkit.entity.Player;

public class PermissionsManager {
	
	public static void addAllPermissions(Player player) {
		player.addAttachment(BungeeSuiteTeleports.instance, "bungeesuite.teleports.*", true);
	}
	public static void addAdminPermissions(Player player) {
		player.addAttachment(BungeeSuiteTeleports.instance, "bungeesuite.teleports.admin", true);
	}
	public static void addUserPermissions(Player player) {
		player.addAttachment(BungeeSuiteTeleports.instance, "bungeesuite.teleports.user", true);
	}
	public static void addVIPPermissions(Player player) {
		player.addAttachment(BungeeSuiteTeleports.instance, "bungeesuite.teleports.vip", true);
	}
}
