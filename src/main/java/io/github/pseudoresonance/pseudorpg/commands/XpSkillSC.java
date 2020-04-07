package io.github.pseudoresonance.pseudorpg.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudorpg.PseudoRPG;
import io.github.pseudoresonance.pseudorpg.xp.XP;
import io.github.pseudoresonance.pseudorpg.xp.XPManager;
import io.github.pseudoresonance.pseudorpg.xp.XPType;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;
import io.github.pseudoresonance.pseudoapi.bukkit.Chat;
import io.github.pseudoresonance.pseudoapi.bukkit.Chat.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.Config;
import io.github.pseudoresonance.pseudoapi.bukkit.language.LanguageManager;

public class XpSkillSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player) || sender.hasPermission("pseudorpg.xp")) {
			if (args.length == 0) {
				String options = "";
				for (XPType t : XPType.values())
					options += "'" + t.toString().toLowerCase() + "', ";
				options = options.substring(0, options.length() - 2);
				PseudoRPG.plugin.getChat().sendPluginError(sender, Errors.INVALID_SUBCOMMAND, options);
				return false;
			} else if (args.length >= 1) {
				XPType type = XPType.valueOf(args[0].toUpperCase());
				if (type == null) {
					String options = "";
					for (XPType t : XPType.values())
						options += "'" + t.toString().toLowerCase() + "', ";
					options = options.substring(0, options.length() - 2);
					PseudoRPG.plugin.getChat().sendPluginError(sender, Errors.INVALID_SUBCOMMAND, options);
					return false;
				} else {
					Player p = null;
					if (args.length == 1) {
						if (sender instanceof Player) {
							p = (Player) sender;
						} else {
							PseudoRPG.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudorpg.specify_player"));
							return false;
						}
					}
					if (args.length >= 2) {
						if ((!(sender instanceof Player) || sender.hasPermission("pseudorpg.xp.others"))) {
							p = Bukkit.getPlayer(args[1]);
							if (p == null) {
								PseudoRPG.plugin.getChat().sendPluginError(sender, Errors.NOT_ONLINE, args[1]);
								return false;
							}
						} else {
							PseudoRPG.plugin.getChat().sendPluginError(sender, Errors.NO_PERMISSION, LanguageManager.getLanguage(sender).getMessage("pseudorpg.permission_xp_others"));
							return false;
						}
					}
					List<Object> messages = new ArrayList<Object>();
					XP xp = XPManager.getPlayerXP(p);
					messages.add(Config.borderColor + "===---" + Config.titleColor + LanguageManager.getLanguage(sender).getMessage("pseudorpg.xpskill_header", p.getName(), LanguageManager.getLanguage(sender).getMessage("pseudorpg." + type.toString().toLowerCase() + "_name")) + Config.borderColor + "---===");
					messages.add(Config.commandColor + LanguageManager.getLanguage(sender).getMessage("pseudorpg.xpskill_level", Config.descriptionColor + xp.getLevel(type)));
					messages.add(Config.commandColor + LanguageManager.getLanguage(sender).getMessage("pseudorpg.xpskill_xp", Config.descriptionColor + xp.getXP(type)));
					messages.add(Config.commandColor + LanguageManager.getLanguage(sender).getMessage("pseudorpg.xpskill_xp_to_next", Config.descriptionColor + xp.getNextXP(type)));
					messages.add(Config.commandColor + LanguageManager.getLanguage(sender).getMessage("pseudorpg.xpskill_total_xp", Config.descriptionColor + xp.getTotalXP(type)));
					Chat.sendMessage(sender, messages);
				}
			}
		} else {
			PseudoRPG.plugin.getChat().sendPluginError(sender, Errors.NO_PERMISSION, LanguageManager.getLanguage(sender).getMessage("pseudorpg.permission_xp"));
			return false;
		}
		return false;
	}

}
