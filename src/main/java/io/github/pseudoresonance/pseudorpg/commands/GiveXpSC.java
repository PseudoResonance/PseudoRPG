package io.github.pseudoresonance.pseudorpg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.Message.Errors;
import io.github.pseudoresonance.pseudorpg.PseudoRPG;
import io.github.pseudoresonance.pseudorpg.xp.XP;
import io.github.pseudoresonance.pseudorpg.xp.XPManager;
import io.github.pseudoresonance.pseudorpg.xp.XPType;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;
import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.PlayerDataController;

public class GiveXpSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (sender.hasPermission("rpg.givexp")) {
				if (args.length == 0) {
					PseudoRPG.message.sendPluginError(sender, Errors.CUSTOM, "You must specify a skill and XP amount!");
					return false;
				} else if (args.length == 1) {
					PseudoRPG.message.sendPluginError(sender, Errors.CUSTOM, "You must specify an amount!");
					return false;
				} else if (args.length == 2) {
					if (args[1].endsWith("L")) {
						String check = args[1].substring(0, args[1].length() - 1);
						try {
							XPType xpt = XPType.valueOf(args[0].toUpperCase());
							XP xp = XPManager.getPlayerXP(sender.getName());
							int add = Integer.valueOf(check);
							if (add < 0) {
								PseudoRPG.message.sendPluginError(sender, Errors.CUSTOM, args[1] + " cannot be negative!");
								return false;
							}
							xp.addLevel(xpt, add);
							PseudoRPG.message.sendPluginMessage(sender, "Added " + add + " levels to " + xpt.getName());
							return true;
						} catch (NumberFormatException e) {
							PseudoRPG.message.sendPluginError(sender, Errors.NOT_A_NUMBER, check);
							return false;
						} catch (IllegalArgumentException e) {
							PseudoRPG.message.sendPluginError(sender, Errors.CUSTOM, "Unknown skill name!");
							return false;
						}
					} else { 
						try {
							XPType xpt = XPType.valueOf(args[0].toUpperCase());
							XP xp = XPManager.getPlayerXP(sender.getName());
							int add = Integer.valueOf(args[1]);
							if (add < 0) {
								PseudoRPG.message.sendPluginError(sender, Errors.CUSTOM, args[1] + " cannot be negative!");
								return false;
							}
							xp.addXP(xpt, add);
							PseudoRPG.message.sendPluginMessage(sender, "Added " + add + " xp to " + xpt.getName());
							return true;
						} catch (NumberFormatException e) {
							PseudoRPG.message.sendPluginError(sender, Errors.NOT_A_NUMBER, args[1]);
							return false;
						} catch (IllegalArgumentException e) {
							PseudoRPG.message.sendPluginError(sender, Errors.CUSTOM, "Unknown skill name!");
							return false;
						}
					}
				} else {
					XP xp = XPManager.getPlayerXP(args[2]);
					if (xp != null) {
						if (args[1].endsWith("L")) {
							String check = args[1].substring(0, args[1].length() - 1);
							try {
								XPType xpt = XPType.valueOf(args[0].toUpperCase());
								int add = Integer.valueOf(check);
								if (add < 0) {
									PseudoRPG.message.sendPluginError(sender, Errors.CUSTOM, args[1] + " cannot be negative!");
									return false;
								}
								xp.addLevel(xpt, add);
								PseudoRPG.message.sendPluginMessage(sender, "Added " + add + " levels to " + xpt.getName() + " for " + PlayerDataController.getName(xp.getUUID()));
								return true;
							} catch (NumberFormatException e) {
								PseudoRPG.message.sendPluginError(sender, Errors.NOT_A_NUMBER, check);
								return false;
							} catch (IllegalArgumentException e) {
								PseudoRPG.message.sendPluginError(sender, Errors.CUSTOM, "Unknown skill name!");
								return false;
							}
						} else { 
							try {
								XPType xpt = XPType.valueOf(args[0].toUpperCase());
								int add = Integer.valueOf(args[1]);
								if (add < 0) {
									PseudoRPG.message.sendPluginError(sender, Errors.CUSTOM, args[1] + " cannot be negative!");
									return false;
								}
								xp.addXP(xpt, add);
								PseudoRPG.message.sendPluginMessage(sender, "Added " + add + " xp to " + xpt.getName() + " for " + PlayerDataController.getName(xp.getUUID()));
								return true;
							} catch (NumberFormatException e) {
								PseudoRPG.message.sendPluginError(sender, Errors.NOT_A_NUMBER, args[1]);
								return false;
							} catch (IllegalArgumentException e) {
								PseudoRPG.message.sendPluginError(sender, Errors.CUSTOM, "Unknown skill name!");
								return false;
							}
						}
					} else {
						PseudoRPG.message.sendPluginError(sender, Errors.NEVER_JOINED, args[2]);
						return false;
					}
				}
			} else {
				PseudoRPG.message.sendPluginError(sender, Errors.NO_PERMISSION, "give xp!");
				return false;
			}
		} else {
			if (args.length == 0) {
				PseudoRPG.message.sendPluginError(sender, Errors.CUSTOM, "You must specify a skill, XP amount and player!");
				return false;
			} else if (args.length == 1) {
				PseudoRPG.message.sendPluginError(sender, Errors.CUSTOM, "You must specify a XP amount and player!");
				return false;
			} else if (args.length == 2) {
				PseudoRPG.message.sendPluginError(sender, Errors.CUSTOM, "You must specify a player!");
				return false;
			} else {
				XP xp = XPManager.getPlayerXP(args[2]);
				if (xp != null) {
					if (args[1].endsWith("L")) {
						String check = args[1].substring(0, args[1].length() - 1);
						try {
							XPType xpt = XPType.valueOf(args[0].toUpperCase());
							int add = Integer.valueOf(check);
							if (add < 0) {
								PseudoRPG.message.sendPluginError(sender, Errors.CUSTOM, args[1] + " cannot be negative!");
								return false;
							}
							xp.addLevel(xpt, add);
							PseudoRPG.message.sendPluginMessage(sender, "Added " + add + " levels to " + xpt.getName() + " for " + PlayerDataController.getName(xp.getUUID()));
							return true;
						} catch (NumberFormatException e) {
							PseudoRPG.message.sendPluginError(sender, Errors.NOT_A_NUMBER, check);
							return false;
						} catch (IllegalArgumentException e) {
							PseudoRPG.message.sendPluginError(sender, Errors.CUSTOM, "Unknown skill name!");
							return false;
						}
					} else { 
						try {
							XPType xpt = XPType.valueOf(args[0].toUpperCase());
							int add = Integer.valueOf(args[1]);
							if (add < 0) {
								PseudoRPG.message.sendPluginError(sender, Errors.CUSTOM, args[1] + " cannot be negative!");
								return false;
							}
							xp.addXP(xpt, add);
							PseudoRPG.message.sendPluginMessage(sender, "Added " + add + " xp to " + xpt.getName() + " for " + PlayerDataController.getName(xp.getUUID()));
							return true;
						} catch (NumberFormatException e) {
							PseudoRPG.message.sendPluginError(sender, Errors.NOT_A_NUMBER, args[1]);
							return false;
						} catch (IllegalArgumentException e) {
							PseudoRPG.message.sendPluginError(sender, Errors.CUSTOM, "Unknown skill name!");
							return false;
						}
					}
				} else {
					PseudoRPG.message.sendPluginError(sender, Errors.NEVER_JOINED, args[2]);
					return false;
				}
			}
		}
	}

}
