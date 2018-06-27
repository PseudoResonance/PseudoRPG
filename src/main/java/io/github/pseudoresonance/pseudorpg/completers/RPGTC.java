package io.github.pseudoresonance.pseudorpg.completers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.PlayerDataController;
import io.github.pseudoresonance.pseudorpg.xp.XPType;

public class RPGTC implements TabCompleter {

	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> possible = new ArrayList<String>();
		if (args.length == 1) {
			possible.add("help");
			if (sender.hasPermission("rpg.reload")) {
				possible.add("reload");
			}
			if (sender.hasPermission("rpg.reset")) {
				possible.add("reset");
			}
			if (sender.hasPermission("rpg.xp")) {
				possible.add("xp");
				possible.add("xpskill");
			}
			if (sender.hasPermission("rpg.givexp")) {
				possible.add("givexp");
			}
			if (sender.hasPermission("rpg.takexp")) {
				possible.add("takexp");
			}
			if (sender.hasPermission("rpg.setxp")) {
				possible.add("setxp");
			}
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
			if (args[0].equalsIgnoreCase("xp")) {
				for (String name : PlayerDataController.getNames()) {
					possible.add(name);
				}
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
			} else if (args[0].equalsIgnoreCase("xpskill")) {
				for (XPType xpt : XPType.values()) {
					possible.add(xpt.getName());
				}
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
			} else if (args[0].equalsIgnoreCase("givexp")) {
				for (XPType xpt : XPType.values()) {
					possible.add(xpt.getName());
				}
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
			} else if (args[0].equalsIgnoreCase("takexp")) {
				for (XPType xpt : XPType.values()) {
					possible.add(xpt.getName());
				}
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
			} else if (args[0].equalsIgnoreCase("setxp")) {
				for (XPType xpt : XPType.values()) {
					possible.add(xpt.getName());
				}
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
			}
		} else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("xpskill")) {
				for (String name : PlayerDataController.getNames()) {
					possible.add(name);
				}
				if (args[2].equalsIgnoreCase("")) {
					return possible;
				} else {
					List<String> checked = new ArrayList<String>();
					for (String check : possible) {
						if (check.toLowerCase().startsWith(args[2].toLowerCase())) {
							checked.add(check);
						}
					}
					return checked;
				}
			}
		} else if (args.length == 4) {
			if (args[0].equalsIgnoreCase("givexp")) {
				for (String name : PlayerDataController.getNames()) {
					possible.add(name);
				}
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
			} else if (args[0].equalsIgnoreCase("takexp")) {
				for (String name : PlayerDataController.getNames()) {
					possible.add(name);
				}
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
			} else if (args[0].equalsIgnoreCase("setxp")) {
				for (String name : PlayerDataController.getNames()) {
					possible.add(name);
				}
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
		return null;
	}

}