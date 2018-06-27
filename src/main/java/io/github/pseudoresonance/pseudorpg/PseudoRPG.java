package io.github.pseudoresonance.pseudorpg;

import io.github.pseudoresonance.pseudoapi.bukkit.CommandDescription;
import io.github.pseudoresonance.pseudoapi.bukkit.Config;
import io.github.pseudoresonance.pseudoapi.bukkit.HelpSC;
import io.github.pseudoresonance.pseudoapi.bukkit.MainCommand;
import io.github.pseudoresonance.pseudoapi.bukkit.Message;
import io.github.pseudoresonance.pseudoapi.bukkit.PseudoAPI;
import io.github.pseudoresonance.pseudoapi.bukkit.PseudoPlugin;
import io.github.pseudoresonance.pseudoapi.bukkit.PseudoUpdater;
import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.Column;
import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.PlayerDataController;
import io.github.pseudoresonance.pseudorpg.commands.GiveXpSC;
import io.github.pseudoresonance.pseudorpg.commands.ReloadSC;
import io.github.pseudoresonance.pseudorpg.commands.ResetSC;
import io.github.pseudoresonance.pseudorpg.commands.SetXpSC;
import io.github.pseudoresonance.pseudorpg.commands.TakeXpSC;
import io.github.pseudoresonance.pseudorpg.commands.XpSC;
import io.github.pseudoresonance.pseudorpg.commands.XpSkillSC;
import io.github.pseudoresonance.pseudorpg.completers.GiveXPTC;
import io.github.pseudoresonance.pseudorpg.completers.RPGTC;
import io.github.pseudoresonance.pseudorpg.completers.RPGXPTC;
import io.github.pseudoresonance.pseudorpg.completers.SetXPTC;
import io.github.pseudoresonance.pseudorpg.completers.TakeXPTC;
import io.github.pseudoresonance.pseudorpg.completers.XPSkillTC;
import io.github.pseudoresonance.pseudorpg.events.BlockBreakEH;
import io.github.pseudoresonance.pseudorpg.events.EntityDamageEH;
import io.github.pseudoresonance.pseudorpg.events.EntityDeathEH;
import io.github.pseudoresonance.pseudorpg.events.PlayerJoinEH;
import io.github.pseudoresonance.pseudorpg.listeners.PlayerJoinLeaveL;

public class PseudoRPG extends PseudoPlugin {

	public static PseudoPlugin plugin;
	public static Message message;
	
	private static MainCommand mainCommand;
	private static HelpSC helpSubCommand;
	
	private static XpSC xpSubCommand;
	private static XpSkillSC xpSkillSubCommand;
	private static GiveXpSC giveXPSubCommand;
	private static TakeXpSC takeXPSubCommand;
	private static SetXpSC setXPSubCommand;
	
	private static ConfigOptions configOptions;
	
	public void onLoad() {
		PseudoUpdater.registerPlugin(this);
	}

	@Override
	public void onEnable() {
		super.onEnable();
		this.saveDefaultConfig();
		Config xp = new Config("xp.yml", this);
		xp.saveDefaultConfig();
		plugin = this;
		PlayerDataController.addColumn(new Column("xpwood", "INT(8)", "0"));
		PlayerDataController.addColumn(new Column("xpmining", "INT(8)", "0"));
		PlayerDataController.addColumn(new Column("xpfishing", "INT(8)", "0"));
		PlayerDataController.addColumn(new Column("xphunting", "INT(8)", "0"));
		PlayerDataController.addColumn(new Column("xpsmelting", "INT(8)", "0"));
		PlayerDataController.addColumn(new Column("xpcrafting", "INT(8)", "0"));
		PlayerDataController.addColumn(new Column("xpblacksmith", "INT(8)", "0"));
		configOptions = new ConfigOptions();
		ConfigOptions.updateConfig();
		configOptions.reloadConfig();
		message = new Message(this);
		mainCommand = new MainCommand(plugin);
		helpSubCommand = new HelpSC(plugin);
		xpSubCommand = new XpSC();
		xpSkillSubCommand = new XpSkillSC();
		giveXPSubCommand = new GiveXpSC();
		takeXPSubCommand = new TakeXpSC();
		setXPSubCommand = new SetXpSC();
		initializeCommands();
		initializeTabcompleters();
		initializeSubCommands();
		initializeListeners();
		setCommandDescriptions();
		PseudoAPI.registerConfig(configOptions);
	}

	@Override
	public void onDisable() {
		DataController.saveXP();
		super.onDisable();
	}
	
	public static ConfigOptions getConfigOptions() {
		return PseudoRPG.configOptions;
	}

	private void initializeCommands() {
		this.getCommand("pseudorpg").setExecutor(mainCommand);
		this.getCommand("rpgxp").setExecutor(xpSubCommand);
		this.getCommand("xpskill").setExecutor(xpSkillSubCommand);
		this.getCommand("givexp").setExecutor(giveXPSubCommand);
		this.getCommand("takexp").setExecutor(takeXPSubCommand);
		this.getCommand("setxp").setExecutor(setXPSubCommand);
	}

	private void initializeSubCommands() {
		subCommands.put("help", helpSubCommand);
		subCommands.put("reload", new ReloadSC());
		subCommands.put("reset", new ResetSC());
		subCommands.put("xp", xpSubCommand);
		subCommands.put("xpskill", xpSkillSubCommand);
		subCommands.put("givexp", giveXPSubCommand);
		subCommands.put("takexp", takeXPSubCommand);
		subCommands.put("setxp", setXPSubCommand);
	}

	private void initializeTabcompleters() {
		this.getCommand("pseudorpg").setTabCompleter(new RPGTC());
		this.getCommand("rpgxp").setTabCompleter(new RPGXPTC());
		this.getCommand("xpskill").setTabCompleter(new XPSkillTC());
		this.getCommand("givexp").setTabCompleter(new GiveXPTC());
		this.getCommand("takexp").setTabCompleter(new TakeXPTC());
		this.getCommand("setxp").setTabCompleter(new SetXPTC());
	}
	private void setCommandDescriptions() {
		commandDescriptions.add(new CommandDescription("rpg", "Shows RPG information", ""));
		commandDescriptions.add(new CommandDescription("rpg help", "Shows RPG commands", ""));
		commandDescriptions.add(new CommandDescription("rpg xp", "Check xp stats", "rpg.xp"));
		commandDescriptions.add(new CommandDescription("rpg xp <Player>", "Check xp level for other player", "rpg.xp.other", false));
		commandDescriptions.add(new CommandDescription("rpg xpskill <Skill>", "Check skill stats", "rpg.xp", false));
		commandDescriptions.add(new CommandDescription("rpg xpskill <Skill> <Player>", "Check skill stats for other player", "rpg.xp.other", false));
		commandDescriptions.add(new CommandDescription("rpg givexp <Amount>", "Gives XP", "rpg.givexp", false));
		commandDescriptions.add(new CommandDescription("rpg takexp <Amount>", "Takes XP", "rpg.takexp", false));
		commandDescriptions.add(new CommandDescription("rpg setxp <Amount>", "Sets XP", "rpg.setxp", false));
		commandDescriptions.add(new CommandDescription("rpg reload", "Reloads RPG config", "rpg.reload"));
		commandDescriptions.add(new CommandDescription("rpg reset", "Resets RPG config", "rpg.reset"));
	}

	public void initializeListeners() {
		getServer().getPluginManager().registerEvents(new BlockBreakEH(), this);
		getServer().getPluginManager().registerEvents(new PlayerJoinEH(), this);
		getServer().getPluginManager().registerEvents(new EntityDeathEH(), this);
		getServer().getPluginManager().registerEvents(new EntityDamageEH(), this);
		getServer().getPluginManager().registerEvents(new PlayerJoinLeaveL(), this);
	}

}