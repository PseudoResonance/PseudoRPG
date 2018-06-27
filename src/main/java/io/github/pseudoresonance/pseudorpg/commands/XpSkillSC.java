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

public class XpSkillSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (sender.hasPermission("rpg.xp")) {
				if (args.length == 0) {
					PseudoRPG.message.sendPluginError(sender, Errors.CUSTOM, "You must specify a skill!");
					return false;
				} else if (args.length == 1) {
					try {
						XPType xpt = XPType.valueOf(args[0].toUpperCase());
						if (sender.hasPermission("rpg.xp.skill." + xpt.getID())) {
							List<Object> messages = new ArrayList<Object>();
							XP xp = XPManager.getPlayerXP(sender.getName());
							messages.add(ConfigOptions.border + "===---" + ConfigOptions.title + xpt.getName() + " XP" + ConfigOptions.border + "---===");
							messages.add(ConfigOptions.command + "Level: " + ConfigOptions.description + xp.getLevel(xpt));
							messages.add(ConfigOptions.command + "XP: " + ConfigOptions.description + xp.getXP(xpt));
							messages.add(ConfigOptions.command + "XP to Level Up: " + ConfigOptions.description + xp.getNextXP(xpt));
							messages.add(ConfigOptions.command + "Total XP Earned: " + ConfigOptions.description + xp.getTotalXP(xpt));
							Message.sendMessage(sender, messages);
							return true;
						} else {
							PseudoRPG.message.sendPluginError(sender, Errors.NO_PERMISSION, "check xp for " + xpt.getName() + "!");
							return false;
						}
					} catch (IllegalArgumentException e) {
						PseudoRPG.message.sendPluginError(sender, Errors.CUSTOM, "Unknown skill name!");
						return false;
					}
				} else {
					if (sender.hasPermission("rpg.xp.other")) {
						try {
							XPType xpt = XPType.valueOf(args[0].toUpperCase());
							if (sender.hasPermission("rpg.xp.other." + xpt.getID())) {
								List<Object> messages = new ArrayList<Object>();
								XP xp = XPManager.getPlayerXP(args[1]);
								if (xp != null) {
									messages.add(ConfigOptions.border + "===---" + ConfigOptions.title + PlayerDataController.getName(xp.getUUID()) + "'s " + xpt.getName() + " XP" + ConfigOptions.border + "---===");
									messages.add(ConfigOptions.command + "Level: " + ConfigOptions.description + xp.getLevel(xpt));
									messages.add(ConfigOptions.command + "XP: " + ConfigOptions.description + xp.getXP(xpt));
									messages.add(ConfigOptions.command + "XP to Level Up: " + ConfigOptions.description + xp.getNextXP(xpt));
									messages.add(ConfigOptions.command + "Total XP Earned: " + ConfigOptions.description + xp.getTotalXP(xpt));
									Message.sendMessage(sender, messages);
									return true;
								} else {
									PseudoRPG.message.sendPluginError(sender, Errors.NEVER_JOINED, args[1]);
									return false;
								}
							} else {
								PseudoRPG.message.sendPluginError(sender, Errors.NO_PERMISSION, "check xp for " + xpt.getName() + "!");
								return false;
							}
						} catch (IllegalArgumentException e) {
							PseudoRPG.message.sendPluginError(sender, Errors.CUSTOM, "Unknown skill name!");
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
				PseudoRPG.message.sendPluginError(sender, Errors.CUSTOM, "You must specify a skill and a player!");
				return false;
			} if (args.length == 1) {
				PseudoRPG.message.sendPluginError(sender, Errors.CUSTOM, "You must specify a player!");
				return false;
			} else {
				XPType xpt = XPType.valueOf(args[0].toUpperCase());
				List<Object> messages = new ArrayList<Object>();
				XP xp = XPManager.getPlayerXP(args[1]);
				if (xp != null) {
					messages.add(ConfigOptions.border + "===---" + ConfigOptions.title + PlayerDataController.getName(xp.getUUID()) + "'s " + xpt.getName() + " XP" + ConfigOptions.border + "---===");
					messages.add(ConfigOptions.command + "Level: " + ConfigOptions.description + xp.getLevel(xpt));
					messages.add(ConfigOptions.command + "XP: " + ConfigOptions.description + xp.getXP(xpt));
					messages.add(ConfigOptions.command + "XP to Level Up: " + ConfigOptions.description + xp.getNextXP(xpt));
					messages.add(ConfigOptions.command + "Total XP Earned: " + ConfigOptions.description + xp.getTotalXP(xpt));
					Message.sendMessage(sender, messages);
					return true;
				} else {
					PseudoRPG.message.sendPluginError(sender, Errors.NEVER_JOINED, args[1]);
					return false;
				}
			}
		}
	}

}
