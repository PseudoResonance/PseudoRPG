package tk.spongenetwork.RPG;

import io.github.wolfleader116.wolfapi.bukkit.CommandDescription;
import io.github.wolfleader116.wolfapi.bukkit.Config;
import io.github.wolfleader116.wolfapi.bukkit.HelpSC;
import io.github.wolfleader116.wolfapi.bukkit.MainCommand;
import io.github.wolfleader116.wolfapi.bukkit.Message;
import io.github.wolfleader116.wolfapi.bukkit.WolfAPI;
import io.github.wolfleader116.wolfapi.bukkit.WolfPlugin;
import tk.spongenetwork.RPG.commands.ReloadSC;
import tk.spongenetwork.RPG.commands.ResetSC;
import tk.spongenetwork.RPG.completers.RPGTC;
import tk.spongenetwork.RPG.events.BlockBreakEH;
import tk.spongenetwork.RPG.events.EntityDamageEH;
import tk.spongenetwork.RPG.events.EntityDeathEH;
import tk.spongenetwork.RPG.events.InventoryClickEH;
import tk.spongenetwork.RPG.events.PlayerInteractEH;
import tk.spongenetwork.RPG.events.PlayerJoinEH;

public class RPG extends WolfPlugin {

	public static WolfPlugin plugin;
	public static Message message;
	
	private static MainCommand mainCommand;
	private static HelpSC helpSubCommand;
	
	private static ConfigOptions configOptions;

	@Override
	public void onEnable() {
		super.onEnable();
		this.saveDefaultConfig();
		Config xp = new Config("xp.yml", this);
		xp.saveDefaultConfig();
		plugin = this;
		configOptions = new ConfigOptions();
		ConfigOptions.updateConfig();
		message = new Message(this);
		mainCommand = new MainCommand(plugin);
		helpSubCommand = new HelpSC(plugin);
		initializeCommands();
		initializeTabcompleters();
		initializeSubCommands();
		initializeListeners();
		setCommandDescriptions();
		configOptions.reloadConfig();
		WolfAPI.registerConfig(configOptions);
	}

	@Override
	public void onDisable() {
		DataController.updateXP();
		super.onDisable();
	}
	
	public static ConfigOptions getConfigOptions() {
		return RPG.configOptions;
	}

	private void initializeCommands() {
		this.getCommand("rpg").setExecutor(mainCommand);
	}

	private void initializeSubCommands() {
		subCommands.put("help", helpSubCommand);
		subCommands.put("reload", new ReloadSC());
		subCommands.put("reset", new ResetSC());
	}

	private void initializeTabcompleters() {
		this.getCommand("rpg").setTabCompleter(new RPGTC());
	}

	private void setCommandDescriptions() {
		commandDescriptions.add(new CommandDescription("rpg", "Shows RPG information", ""));
		commandDescriptions.add(new CommandDescription("rpg help", "Shows RPG commands", ""));
		commandDescriptions.add(new CommandDescription("rpg reload", "Reloads RPG config", "rpg.reload"));
		commandDescriptions.add(new CommandDescription("rpg reset", "Resets RPG config", "rpg.reset"));
	}

	public void initializeListeners() {
		getServer().getPluginManager().registerEvents(new BlockBreakEH(), this);
		getServer().getPluginManager().registerEvents(new PlayerJoinEH(), this);
		getServer().getPluginManager().registerEvents(new EntityDeathEH(), this);
		getServer().getPluginManager().registerEvents(new EntityDamageEH(), this);
		getServer().getPluginManager().registerEvents(new PlayerInteractEH(), this);
		getServer().getPluginManager().registerEvents(new InventoryClickEH(), this);
	}

}