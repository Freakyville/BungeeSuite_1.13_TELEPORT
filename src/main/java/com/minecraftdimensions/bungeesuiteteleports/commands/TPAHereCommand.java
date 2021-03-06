package com.minecraftdimensions.bungeesuiteteleports.commands;

import com.minecraftdimensions.bungeesuiteteleports.managers.TeleportsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class TPAHereCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

		if(args.length>0){
			TeleportsManager.tpaHereRequest(sender,args[0]);
			return true;
		}
		return false;
	}

}
