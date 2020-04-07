package io.github.pseudoresonance.pseudorpg.completers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudorpg.xp.XPType;

public class RPGTC implements TabCompleter {

	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> possible = new ArrayList<String>();
		if (args.length == 1) {
			possible.add("help");
			if (sender.hasPermission("pseudorpg.reload")) {
				possible.add("reload");
			}
			if (sender.hasPermission("pseudorpg.reset")) {
				possible.add("reset");
			}
			if (sender.hasPermission("pseudorpg.reloadlocalization")) {
				possible.add("reloadlocalization");
			}
			if (sender.hasPermission("pseudorpg.resetlocalization")) {
				possible.add("resetlocalization");
			}
			if (sender.hasPermission("pseudorpg.xp")) {
				possible.add("xp");
				possible.add("xpskill");
			}
			if (sender.hasPermission("pseudorpg.skillxp")) {
				possible.add("skillxp");
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
		} else if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("skillxp")) {
				if (!(sender instanceof Player) || sender.hasPermission("pseudorpg.skillxp")) {
					if (args.length == 2) {
						possible.add("add");
						possible.add("set");
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
					} else if (args.length == 3) {
						for (XPType type : XPType.values())
							possible.add(type.toString().toLowerCase());
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
					} else if (args.length == 5) {
						for (Player p : Bukkit.getOnlinePlayers())
							possible.add(p.getName().toLowerCase());
						if (args[4].equalsIgnoreCase("")) {
							return possible;
						} else {
							List<String> checked = new ArrayList<String>();
							for (String check : possible) {
								if (check.toLowerCase().startsWith(args[4].toLowerCase())) {
									checked.add(check);
								}
							}
							return checked;
						}
					}
				}
			} else if (args[0].equalsIgnoreCase("xpskill")) {
				if (!(sender instanceof Player) || sender.hasPermission("pseudorpg.xp")) {
					if (args.length == 2) {
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
					} else if (args.length == 3 && sender.hasPermission("pseudorpg.xp.others")) {
						for (Player p : Bukkit.getOnlinePlayers())
							possible.add(p.getName().toLowerCase());
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
				}
			} else if (args[0].equalsIgnoreCase("xp")) {
				if (!(sender instanceof Player) || sender.hasPermission("pseudorpg.xp")) {
					if (args.length == 2 && sender.hasPermission("pseudorpg.xp.others")) {
						for (Player p : Bukkit.getOnlinePlayers())
							possible.add(p.getName().toLowerCase());
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
				}
			}
		}
		return possible;
	}

}