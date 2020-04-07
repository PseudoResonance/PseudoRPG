package io.github.pseudoresonance.pseudorpg.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.language.LanguageManager;
import io.github.pseudoresonance.pseudorpg.PseudoRPG;
import io.github.pseudoresonance.pseudorpg.xp.XP;
import io.github.pseudoresonance.pseudorpg.xp.XPManager;
import io.github.pseudoresonance.pseudorpg.xp.XPType;
import io.github.pseudoresonance.pseudoapi.bukkit.Chat.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.Chat;
import io.github.pseudoresonance.pseudoapi.bukkit.Config;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;

public class XpSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player) || sender.hasPermission("pseudorpg.xp")) {
			Player p = null;
			if (args.length == 0) {
				if (sender instanceof Player) {
					p = (Player) sender;
				} else {
					PseudoRPG.plugin.getChat().sendPluginError(sender, Errors.CUSTOM, LanguageManager.getLanguage(sender).getMessage("pseudorpg.specify_player"));
					return false;
				}
			}
			if (args.length >= 1) {
				if ((!(sender instanceof Player) || sender.hasPermission("pseudorpg.xp.others"))) {
					p = Bukkit.getPlayer(args[0]);
					if (p == null) {
						PseudoRPG.plugin.getChat().sendPluginError(sender, Errors.NOT_ONLINE, args[0]);
						return false;
					}
				} else {
					PseudoRPG.plugin.getChat().sendPluginError(sender, Errors.NO_PERMISSION, LanguageManager.getLanguage(sender).getMessage("pseudorpg.permission_xp_others"));
					return false;
				}
			}
			List<Object> messages = new ArrayList<Object>();
			XP xp = XPManager.getPlayerXP(p);
			messages.add(Config.borderColor + "===---" + Config.titleColor + LanguageManager.getLanguage(sender).getMessage("pseudorpg.xp_header", p.getName()) + Config.borderColor + "---===");
			for (XPType type : XPType.values())
				messages.add(LanguageManager.getLanguage(sender).getMessage("pseudorpg.xp_entry", Config.commandColor + LanguageManager.getLanguage(sender).getMessage("pseudorpg." + type.toString().toLowerCase() + "_name"), Config.descriptionColor + LanguageManager.getLanguage(sender).getMessage("pseudorpg.xp_info", xp.getLevel(type), xp.getXP(type), xp.getNextXP(type))));
			Chat.sendMessage(sender, messages);
		} else {
			PseudoRPG.plugin.getChat().sendPluginError(sender, Errors.NO_PERMISSION, LanguageManager.getLanguage(sender).getMessage("pseudorpg.permission_xp"));
			return false;
		}
		return false;
	}

}
