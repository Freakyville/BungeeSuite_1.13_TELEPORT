package com.minecraftdimensions.bungeesuiteteleports.commands;

import com.minecraftdimensions.bungeesuiteteleports.managers.TeleportsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class TPAllCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

		if(args.length==0){
			TeleportsManager.tpAll(sender,sender.getName());
			return true;
		}
		if(args.length==1){
			TeleportsManager.tpAll(sender,args[0]);
			return true;
		}
		return false;
	}

}
