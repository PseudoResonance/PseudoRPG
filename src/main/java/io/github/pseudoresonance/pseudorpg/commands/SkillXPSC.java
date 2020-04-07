package io.github.pseudoresonance.pseudorpg.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudorpg.PseudoRPG;
import io.github.pseudoresonance.pseudorpg.xp.XP;
import io.github.pseudoresonance.pseudorpg.xp.XPManager;
import io.github.pseudoresonance.pseudorpg.xp.XPType;
import io.github.pseudoresonance.pseudoapi.bukkit.Chat.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;
import io.github.pseudoresonance.pseudoapi.bukkit.language.LanguageManager;

public class SkillXPSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player) || sender.hasPermission("pseudorpg.skillxp")) {
			if (args.length == 0) {
				PseudoRPG.plugin.getChat().sendPluginError(sender, Errors.INVALID_SUBCOMMAND, "'add', 'set'");
				return false;
			} else if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("set")) {
					if (args.length == 1) {
						String options = "";
						for (XPType t : XPType.values())
							options += "'" + t.toString().toLowerCase() + "', ";
						options = options.substring(0, options.length() - 2);
						PseudoRPG.plugin.getChat().sendPluginError(sender, Errors.INVALID_SUBCOMMAND, options);
						return false;
					} else if (args.length >= 2) {
						XPType type = XPType.valueOf(args[1].toUpperCase());
						if (type == null) {
							String options = "";
							for (XPType t : XPType.values())
								options += "'" + t.toString().toLowerCase() + "', ";
							options = options.substring(0, options.length() - 2);
							PseudoRPG.plugin.getChat().sendPluginError(sender, Errors.INVALID_SUBCOMMAND, options);
							return false;
						} else {
							if (args.length == 2) {
								PseudoRPG.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudorpg.specify_amount"));
								return false;
							} else if (args.length >= 3) {
								try {
									boolean level = false;
									int num = 0;
									if (args[2].toLowerCase().endsWith("l")) {
										level = true;
										num = Integer.valueOf(args[2].substring(0, args[2].length() - 1));
									} else
										num = Integer.valueOf(args[2]);
									Player p = null;
									if (args.length == 3) {
										if (sender instanceof Player) {
											p = (Player) sender;
										} else {
											PseudoRPG.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudorpg.specify_player"));
											return false;
										}
									}
									if (args.length >= 4) {
										p = Bukkit.getPlayer(args[3]);
										if (p == null) {
											PseudoRPG.plugin.getChat().sendPluginError(sender, Errors.NOT_ONLINE, args[3]);
											return false;
										}
									}
									if (args[0].equalsIgnoreCase("add")) {
										XP xp = XPManager.getPlayerXP(p);
										if (num > 0) {
											if (level) {
												xp.addLevel(type, num);
												PseudoRPG.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudorpg.added_level", num, p.getName(), type.toString().toLowerCase()));
											} else {
												xp.addXP(type, num);
												PseudoRPG.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudorpg.added_xp", num, p.getName(), type.toString().toLowerCase()));
											}
										} else if (num < 0) {
											if (level) {
												xp.removeLevel(type, -num);
												PseudoRPG.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudorpg.removed_level", -num, p.getName(), type.toString().toLowerCase()));
											} else {
												xp.removeXP(type, -num);
												PseudoRPG.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudorpg.removed_xp", -num, p.getName(), type.toString().toLowerCase()));
											}
										} else {
											PseudoRPG.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudorpg.specify_amount"));
											return false;
										}
									} else if (args[0].equalsIgnoreCase("set")) {
										XP xp = XPManager.getPlayerXP(p);
										if (num >= 0) {
											if (level) {
												xp.setLevel(type, num);
												PseudoRPG.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudorpg.set_level", p.getName(), type.toString().toLowerCase(), num));
											} else {
												xp.setXP(type, num);
												PseudoRPG.plugin.getChat().sendPluginMessage(sender, LanguageManager.getLanguage(sender).getMessage("pseudorpg.set_xp", p.getName(), type.toString().toLowerCase(), num));
											}
										} else if (num < 0) {
											PseudoRPG.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudorpg.specify_amount"));
											return false;
										}
									}
									return true;
								} catch (NumberFormatException ex) {
									PseudoRPG.plugin.getChat().sendPluginError(sender, Errors.NOT_A_NUMBER, args[2]);
									return false;
								}
							}
						}
					}
				} else {
					PseudoRPG.plugin.getChat().sendPluginError(sender, Errors.INVALID_SUBCOMMAND, "'add', 'set'");
					return false;
				}
			}
		} else {
			PseudoRPG.plugin.getChat().sendPluginError(sender, Errors.NO_PERMISSION, LanguageManager.getLanguage(sender).getMessage("pseudorpg.permission_skillxp"));
			return false;
		}
		return false;
	}

}
