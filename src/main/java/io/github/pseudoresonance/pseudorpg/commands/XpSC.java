package io.github.pseudoresonance.pseudorpg.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.ConfigOptions;
import io.github.pseudoresonance.pseudoapi.bukkit.Message;
import io.github.pseudoresonance.pseudoapi.bukkit.Message.Errors;
import io.github.pseudoresonance.pseudorpg.PseudoRPG;
import io.github.pseudoresonance.pseudorpg.xp.XP;
import io.github.pseudoresonance.pseudorpg.xp.XPManager;
import io.github.pseudoresonance.pseudorpg.xp.XPType;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;
import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.PlayerDataController;

public class XpSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (sender.hasPermission("rpg.xp")) {
				if (args.length == 0) {
					List<Object> messages = new ArrayList<Object>();
					messages.add(ConfigOptions.border + "===---" + ConfigOptions.title + "XP" + ConfigOptions.border + "---===");
					XP xp = XPManager.getPlayerXP(sender.getName());
					for (XPType xpt : XPType.values()) {
						if (sender.hasPermission("rpg.xp." + xpt.getID())) {
							messages.add(ConfigOptions.command + xpt.getName() + ": " + ConfigOptions.description + "Level " + xp.getLevel(xpt) + " (" + xp.getXP(xpt) + " / " + xp.getNextXP(xpt) + ")");
						}
					}
					Message.sendMessage(sender, messages);
					return true;
				} else {
					if (sender.hasPermission("rpg.xp.other")) {
						XP xp = XPManager.getPlayerXP(args[0]);
						if (xp != null) {
							List<Object> messages = new ArrayList<Object>();
							messages.add(ConfigOptions.border + "===---" + ConfigOptions.title + PlayerDataController.getName(xp.getUUID()) + "'s XP" + ConfigOptions.border + "---===");
							for (XPType xpt : XPType.values()) {
								if (sender.hasPermission("rpg.xp.other." + xpt.getID())) {
									messages.add(ConfigOptions.command + xpt.getName() + ": " + ConfigOptions.description + "Level " + xp.getLevel(xpt) + " (" + xp.getXP(xpt) + " / " + xp.getNextXP(xpt) + ")");
								}
							}
							Message.sendMessage(sender, messages);
							return true;
						} else {
							PseudoRPG.message.sendPluginError(sender, Errors.NEVER_JOINED, args[0]);
							return false;
						}
					} else {
						PseudoRPG.message.sendPluginError(sender, Errors.NO_PERMISSION, "check xp of another player!");
						return false;
					}
				}
			} else {
				PseudoRPG.message.sendPluginError(sender, Errors.NO_PERMISSION, "check xp!");
				return false;
			}
		} else {
			if (args.length == 0) {
				PseudoRPG.message.sendPluginError(sender, Errors.CUSTOM, "You must specify a player!");
				return false;
			} else {
				List<Object> messages = new ArrayList<Object>();
				XP xp = XPManager.getPlayerXP(args[0]);
				if (xp != null) {
					messages.add(ConfigOptions.border + "===---" + ConfigOptions.title + PlayerDataController.getName(xp.getUUID()) + "'s XP" + ConfigOptions.border + "---===");
					for (XPType xpt : XPType.values()) {
						messages.add(ConfigOptions.command + xpt.getName() + ": " + ConfigOptions.description + "Level " + xp.getLevel(xpt) + " (" + xp.getXP(xpt) + " / " + xp.getNextXP(xpt) + ")");
					}
					Message.sendMessage(sender, messages);
					return true;
				} else {
					PseudoRPG.message.sendPluginError(sender, Errors.NEVER_JOINED, args[0]);
					return false;
				}
			}
		}
	}

}
