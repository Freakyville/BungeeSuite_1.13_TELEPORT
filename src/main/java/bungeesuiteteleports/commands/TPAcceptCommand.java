package bungeesuiteteleports.commands;

import com.minecraftdimensions.bungeesuiteteleports.managers.TeleportsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class TPAcceptCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

			TeleportsManager.tpAccept(sender);
			return true;
	}

}