package tk.spongenetwork.RPG.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.wolfleader116.wolfapi.bukkit.Errors;
import io.github.wolfleader116.wolfapi.bukkit.SubCommandExecutor;
import tk.spongenetwork.RPG.RPG;

public class ReloadSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (sender.hasPermission("rpg.reload")) {
				try {
					RPG.plugin.reloadConfig();
				} catch (Exception e) {
					RPG.message.sendPluginError(sender, Errors.GENERIC);
					return false;
				}
				RPG.message.sendPluginMessage(sender, "Plugin config reloaded!");
				return true;
			} else {
				RPG.message.sendPluginError(sender, Errors.NO_PERMISSION, "reload the config!");
				return false;
			}
		} else {
			try {
				RPG.plugin.reloadConfig();
			} catch (Exception e) {
				RPG.message.sendPluginError(sender, Errors.GENERIC);
				return false;
			}
			RPG.getConfigOptions().reloadConfig();
			RPG.message.sendPluginMessage(sender, "Plugin config reloaded!");
			return true;
		}
	}

}
