package tk.spongenetwork.RPG.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.wolfleader116.wolfapi.bukkit.Errors;
import io.github.wolfleader116.wolfapi.bukkit.SubCommandExecutor;
import io.github.wolfleader116.wolfapi.bukkit.WolfAPI;
import tk.spongenetwork.RPG.RPG;
import tk.spongenetwork.RPG.XP.XP;
import tk.spongenetwork.RPG.XP.XPManager;
import tk.spongenetwork.RPG.XP.XPType;

public class TakeXpSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (sender.hasPermission("rpg.takexp")) {
				if (args.length == 0) {
					RPG.message.sendPluginError(sender, Errors.CUSTOM, "You must specify a skill and XP amount!");
					return false;
				} else if (args.length == 1) {
					RPG.message.sendPluginError(sender, Errors.CUSTOM, "You must specify an amount!");
					return false;
				} else if (args.length == 2) {
					if (args[1].endsWith("L")) {
						String check = args[1].substring(0, args[1].length() - 1);
						try {
							XPType xpt = XPType.valueOf(args[0].toUpperCase());
							XP xp = XPManager.getPlayerXP(sender.getName());
							int add = Integer.valueOf(check);
							if (add < 0) {
								RPG.message.sendPluginError(sender, Errors.CUSTOM, args[1] + " cannot be negative!");
								return false;
							}
							xp.removeLevel(xpt, add);
							RPG.message.sendPluginMessage(sender, "Removed " + add + " levels from " + xpt.getName());
							return true;
						} catch (NumberFormatException e) {
							RPG.message.sendPluginError(sender, Errors.NOT_A_NUMBER, check);
							return false;
						} catch (IllegalArgumentException e) {
							RPG.message.sendPluginError(sender, Errors.CUSTOM, "Unknown skill name!");
							return false;
						}
					} else { 
						try {
							XPType xpt = XPType.valueOf(args[0].toUpperCase());
							XP xp = XPManager.getPlayerXP(sender.getName());
							int add = Integer.valueOf(args[1]);
							if (add < 0) {
								RPG.message.sendPluginError(sender, Errors.CUSTOM, args[1] + " cannot be negative!");
								return false;
							}
							xp.removeXP(xpt, add);
							RPG.message.sendPluginMessage(sender, "Removed " + add + " xp from " + xpt.getName());
							return true;
						} catch (NumberFormatException e) {
							RPG.message.sendPluginError(sender, Errors.NOT_A_NUMBER, args[1]);
							return false;
						} catch (IllegalArgumentException e) {
							RPG.message.sendPluginError(sender, Errors.CUSTOM, "Unknown skill name!");
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
									RPG.message.sendPluginError(sender, Errors.CUSTOM, args[1] + " cannot be negative!");
									return false;
								}
								xp.removeLevel(xpt, add);
								RPG.message.sendPluginMessage(sender, "Removed " + add + " levels to " + xpt.getName() + " for " + WolfAPI.getPlayerName(xp.getUUID()));
								return true;
							} catch (NumberFormatException e) {
								RPG.message.sendPluginError(sender, Errors.NOT_A_NUMBER, check);
								return false;
							} catch (IllegalArgumentException e) {
								RPG.message.sendPluginError(sender, Errors.CUSTOM, "Unknown skill name!");
								return false;
							}
						} else { 
							try {
								XPType xpt = XPType.valueOf(args[0].toUpperCase());
								int add = Integer.valueOf(args[1]);
								if (add < 0) {
									RPG.message.sendPluginError(sender, Errors.CUSTOM, args[1] + " cannot be negative!");
									return false;
								}
								xp.removeXP(xpt, add);
								RPG.message.sendPluginMessage(sender, "Removed " + add + " xp from " + xpt.getName() + " for " + WolfAPI.getPlayerName(xp.getUUID()));
								return true;
							} catch (NumberFormatException e) {
								RPG.message.sendPluginError(sender, Errors.NOT_A_NUMBER, args[1]);
								return false;
							} catch (IllegalArgumentException e) {
								RPG.message.sendPluginError(sender, Errors.CUSTOM, "Unknown skill name!");
								return false;
							}
						}
					} else {
						RPG.message.sendPluginError(sender, Errors.NEVER_JOINED, args[2]);
						return false;
					}
				}
			} else {
				RPG.message.sendPluginError(sender, Errors.NO_PERMISSION, "take xp!");
				return false;
			}
		} else {
			if (args.length == 0) {
				RPG.message.sendPluginError(sender, Errors.CUSTOM, "You must specify a skill, XP amount and player!");
				return false;
			} else if (args.length == 1) {
				RPG.message.sendPluginError(sender, Errors.CUSTOM, "You must specify a XP amount and player!");
				return false;
			} else if (args.length == 2) {
				RPG.message.sendPluginError(sender, Errors.CUSTOM, "You must specify a player!");
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
								RPG.message.sendPluginError(sender, Errors.CUSTOM, args[1] + " cannot be negative!");
								return false;
							}
							xp.removeLevel(xpt, add);
							RPG.message.sendPluginMessage(sender, "Removed " + add + " levels to " + xpt.getName() + " for " + WolfAPI.getPlayerName(xp.getUUID()));
							return true;
						} catch (NumberFormatException e) {
							RPG.message.sendPluginError(sender, Errors.NOT_A_NUMBER, check);
							return false;
						} catch (IllegalArgumentException e) {
							RPG.message.sendPluginError(sender, Errors.CUSTOM, "Unknown skill name!");
							return false;
						}
					} else { 
						try {
							XPType xpt = XPType.valueOf(args[0].toUpperCase());
							int add = Integer.valueOf(args[1]);
							if (add < 0) {
								RPG.message.sendPluginError(sender, Errors.CUSTOM, args[1] + " cannot be negative!");
								return false;
							}
							xp.removeXP(xpt, add);
							RPG.message.sendPluginMessage(sender, "Removed " + add + " xp from " + xpt.getName() + " for " + WolfAPI.getPlayerName(xp.getUUID()));
							return true;
						} catch (NumberFormatException e) {
							RPG.message.sendPluginError(sender, Errors.NOT_A_NUMBER, args[1]);
							return false;
						} catch (IllegalArgumentException e) {
							RPG.message.sendPluginError(sender, Errors.CUSTOM, "Unknown skill name!");
							return false;
						}
					}
				} else {
					RPG.message.sendPluginError(sender, Errors.NEVER_JOINED, args[2]);
					return false;
				}
			}
		}
	}

}
