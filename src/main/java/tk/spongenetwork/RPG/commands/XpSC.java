package tk.spongenetwork.RPG.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.wolfleader116.wolfapi.bukkit.ConfigOptions;
import io.github.wolfleader116.wolfapi.bukkit.Errors;
import io.github.wolfleader116.wolfapi.bukkit.Message;
import io.github.wolfleader116.wolfapi.bukkit.SubCommandExecutor;
import io.github.wolfleader116.wolfapi.bukkit.WolfAPI;
import tk.spongenetwork.RPG.RPG;
import tk.spongenetwork.RPG.XP.XP;
import tk.spongenetwork.RPG.XP.XPManager;
import tk.spongenetwork.RPG.XP.XPType;

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
							messages.add(ConfigOptions.border + "===---" + ConfigOptions.title + WolfAPI.getPlayerName(xp.getUUID()) + "'s XP" + ConfigOptions.border + "---===");
							for (XPType xpt : XPType.values()) {
								if (sender.hasPermission("rpg.xp.other." + xpt.getID())) {
									messages.add(ConfigOptions.command + xpt.getName() + ": " + ConfigOptions.description + "Level " + xp.getLevel(xpt) + " (" + xp.getXP(xpt) + " / " + xp.getNextXP(xpt) + ")");
								}
							}
							Message.sendMessage(sender, messages);
							return true;
						} else {
							RPG.message.sendPluginError(sender, Errors.NEVER_JOINED, args[0]);
							return false;
						}
					} else {
						RPG.message.sendPluginError(sender, Errors.NO_PERMISSION, "check xp of another player!");
						return false;
					}
				}
			} else {
				RPG.message.sendPluginError(sender, Errors.NO_PERMISSION, "check xp!");
				return false;
			}
		} else {
			if (args.length == 0) {
				RPG.message.sendPluginError(sender, Errors.CUSTOM, "You must specify a player!");
				return false;
			} else {
				List<Object> messages = new ArrayList<Object>();
				XP xp = XPManager.getPlayerXP(args[0]);
				if (xp != null) {
					messages.add(ConfigOptions.border + "===---" + ConfigOptions.title + WolfAPI.getPlayerName(xp.getUUID()) + "'s XP" + ConfigOptions.border + "---===");
					for (XPType xpt : XPType.values()) {
						messages.add(ConfigOptions.command + xpt.getName() + ": " + ConfigOptions.description + "Level " + xp.getLevel(xpt) + " (" + xp.getXP(xpt) + " / " + xp.getNextXP(xpt) + ")");
					}
					Message.sendMessage(sender, messages);
					return true;
				} else {
					RPG.message.sendPluginError(sender, Errors.NEVER_JOINED, args[0]);
					return false;
				}
			}
		}
	}

}
