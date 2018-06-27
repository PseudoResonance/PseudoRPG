package io.github.pseudoresonance.pseudorpg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.Message.Errors;
import io.github.pseudoresonance.pseudorpg.PseudoRPG;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;

public class ReloadSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (sender.hasPermission("rpg.reload")) {
				try {
					PseudoRPG.plugin.reloadConfig();
				} catch (Exception e) {
					PseudoRPG.message.sendPluginError(sender, Errors.GENERIC);
					return false;
				}
				PseudoRPG.message.sendPluginMessage(sender, "Plugin config reloaded!");
				return true;
			} else {
				PseudoRPG.message.sendPluginError(sender, Errors.NO_PERMISSION, "reload the config!");
				return false;
			}
		} else {
			try {
				PseudoRPG.plugin.reloadConfig();
			} catch (Exception e) {
				PseudoRPG.message.sendPluginError(sender, Errors.GENERIC);
				return false;
			}
			PseudoRPG.getConfigOptions().reloadConfig();
			PseudoRPG.message.sendPluginMessage(sender, "Plugin config reloaded!");
			return true;
		}
	}

}
