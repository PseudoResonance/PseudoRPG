package io.github.pseudoresonance.pseudorpg.completers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudorpg.xp.XPType;

public class SkillXPTC implements TabCompleter {

	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> possible = new ArrayList<String>();
		if (!(sender instanceof Player) || sender.hasPermission("pseudorpg.skillxp")) {
			if (args.length == 1) {
				possible.add("add");
				possible.add("set");
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
			} else if (args.length == 2) {
				for (XPType type : XPType.values())
					possible.add(type.toString().toLowerCase());
				if (args[1].equalsIgnoreCase("")) {
					return possible;
				} else {
					List<String> checked = new ArrayList<String>();
					for (String check : possible) {
						if (check.toLowerCase().startsWith(args[1].toLowerCase())) {
							checked.add(check);
						}
					}
					return checked;
				}
			} else if (args.length == 4) {
				for (Player p : Bukkit.getOnlinePlayers())
					possible.add(p.getName().toLowerCase());
				if (args[3].equalsIgnoreCase("")) {
					return possible;
				} else {
					List<String> checked = new ArrayList<String>();
					for (String check : possible) {
						if (check.toLowerCase().startsWith(args[3].toLowerCase())) {
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
