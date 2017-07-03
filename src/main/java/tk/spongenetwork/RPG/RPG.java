package tk.spongenetwork.RPG;

import io.github.wolfleader116.wolfapi.bukkit.CommandDescription;
import io.github.wolfleader116.wolfapi.bukkit.Config;
import io.github.wolfleader116.wolfapi.bukkit.HelpSC;
import io.github.wolfleader116.wolfapi.bukkit.MainCommand;
import io.github.wolfleader116.wolfapi.bukkit.Message;
import io.github.wolfleader116.wolfapi.bukkit.WolfAPI;
import io.github.wolfleader116.wolfapi.bukkit.WolfPlugin;
import tk.spongenetwork.RPG.commands.GiveXpSC;
import tk.spongenetwork.RPG.commands.ReloadSC;
import tk.spongenetwork.RPG.commands.ResetSC;
import tk.spongenetwork.RPG.commands.SetXpSC;
import tk.spongenetwork.RPG.commands.TakeXpSC;
import tk.spongenetwork.RPG.commands.XpSC;
import tk.spongenetwork.RPG.commands.XpSkillSC;
import tk.spongenetwork.RPG.completers.GiveXPTC;
import tk.spongenetwork.RPG.completers.RPGTC;
import tk.spongenetwork.RPG.completers.RPGXPTC;
import tk.spongenetwork.RPG.completers.SetXPTC;
import tk.spongenetwork.RPG.completers.TakeXPTC;
import tk.spongenetwork.RPG.completers.XPSkillTC;
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
	
	private static XpSC xpSubCommand;
	private static XpSkillSC xpSkillSubCommand;
	private static GiveXpSC giveXPSubCommand;
	private static TakeXpSC takeXPSubCommand;
	private static SetXpSC setXPSubCommand;
	
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
		this.getCommand("rpg").setTabCompleter(new RPGTC());
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
		getServer().getPluginManager().registerEvents(new PlayerInteractEH(), this);
		getServer().getPluginManager().registerEvents(new InventoryClickEH(), this);
	}

}