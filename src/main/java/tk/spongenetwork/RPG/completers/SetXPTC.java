package tk.spongenetwork.RPG.completers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import io.github.wolfleader116.wolfapi.bukkit.DataController;
import io.github.wolfleader116.wolfapi.bukkit.data.Data;
import tk.spongenetwork.RPG.XP.XPType;

public class SetXPTC implements TabCompleter {

	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> possible = new ArrayList<String>();
		if (args.length == 1) {
			for (XPType xpt : XPType.values()) {
				possible.add(xpt.getName());
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
		} else if (args.length == 3) {
			for (String name : DataController.getUUIDS(Data.getBackend()).values()) {
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
		}
		return null;
	}

}
