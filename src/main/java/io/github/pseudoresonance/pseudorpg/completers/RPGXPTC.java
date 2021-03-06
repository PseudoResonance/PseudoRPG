package io.github.pseudoresonance.pseudorpg.completers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class RPGXPTC implements TabCompleter {

	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> possible = new ArrayList<String>();
		if (!(sender instanceof Player) || sender.hasPermission("pseudorpg.xp.others")) {
			if (args.length == 1) {
				for (Player p : Bukkit.getOnlinePlayers())
					possible.add(p.getName().toLowerCase());
				if (args[0].equalsIgnoreCase("")) {
					return possible;
				} else {
					List<String> checked = new ArrayList<String>();
					for (String check : possible) {
						if (check.toLowerCase().startsWith(args[0].toLowerCase())) {
							checked.add(check);
						}
					}
					return checked;
				}
			}
		}
		return possible;
	}

}
