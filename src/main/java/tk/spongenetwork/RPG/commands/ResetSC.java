package tk.spongenetwork.RPG.commands;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.wolfleader116.wolfapi.bukkit.Errors;
import io.github.wolfleader116.wolfapi.bukkit.SubCommandExecutor;
import tk.spongenetwork.RPG.RPG;

public class ResetSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (sender.hasPermission("rpg.reset")) {
				try {
					File conf = new File(RPG.plugin.getDataFolder(), "config.yml");
					conf.delete();
					RPG.plugin.saveDefaultConfig();
					RPG.plugin.reloadConfig();
				} catch (Exception e) {
					RPG.message.sendPluginError(sender, Errors.GENERIC);
					return false;
				}
				RPG.getConfigOptions().reloadConfig();
				RPG.message.sendPluginMessage(sender, "Plugin config reset!");
				return true;
			} else {
				RPG.message.sendPluginError(sender, Errors.NO_PERMISSION, "reset the config!");
				return false;
			}
		} else {
			try {
				File conf = new File(RPG.plugin.getDataFolder(), "config.yml");
				conf.delete();
				RPG.plugin.saveDefaultConfig();
				RPG.plugin.reloadConfig();
			} catch (Exception e) {
				RPG.message.sendPluginError(sender, Errors.GENERIC);
				return false;
			}
			RPG.getConfigOptions().reloadConfig();
			RPG.message.sendPluginMessage(sender, "Plugin config reset!");
			return true;
		}
	}

}
