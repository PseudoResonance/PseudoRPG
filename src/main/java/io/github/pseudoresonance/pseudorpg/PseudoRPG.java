package io.github.pseudoresonance.pseudorpg;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.RegisteredServiceProvider;

import io.github.pseudoresonance.pseudoapi.bukkit.Chat;
import io.github.pseudoresonance.pseudoapi.bukkit.CommandDescription;
import io.github.pseudoresonance.pseudoapi.bukkit.HelpSC;
import io.github.pseudoresonance.pseudoapi.bukkit.MainCommand;
import io.github.pseudoresonance.pseudoapi.bukkit.PseudoAPI;
import io.github.pseudoresonance.pseudoapi.bukkit.PseudoPlugin;
import io.github.pseudoresonance.pseudoapi.bukkit.PseudoUpdater;
import io.github.pseudoresonance.pseudoapi.bukkit.language.LanguageManager;
import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.Column;
import io.github.pseudoresonance.pseudoapi.bukkit.playerdata.PlayerDataController;
import io.github.pseudoresonance.pseudorpg.commands.SkillXPSC;
import io.github.pseudoresonance.pseudorpg.commands.ReloadSC;
import io.github.pseudoresonance.pseudorpg.commands.ResetSC;
import io.github.pseudoresonance.pseudorpg.commands.XpSC;
import io.github.pseudoresonance.pseudorpg.commands.XpSkillSC;
import io.github.pseudoresonance.pseudorpg.completers.RPGTC;
import io.github.pseudoresonance.pseudorpg.completers.RPGXPSkillTC;
import io.github.pseudoresonance.pseudorpg.completers.RPGXPTC;
import io.github.pseudoresonance.pseudorpg.completers.SkillXPTC;
import io.github.pseudoresonance.pseudorpg.events.BlockBreakEH;
import io.github.pseudoresonance.pseudorpg.events.EntityDamageEH;
import io.github.pseudoresonance.pseudorpg.events.EntityDeathEH;
import io.github.pseudoresonance.pseudorpg.listeners.PlayerJoinLeaveL;

public class PseudoRPG extends PseudoPlugin {

	public static PseudoRPG plugin;
	
	private static MainCommand mainCommand;
	private static HelpSC helpSubCommand;
	
	private static XpSC xpSubCommand;
	private static XpSkillSC xpSkillSubCommand;
	private static SkillXPSC giveXPSubCommand;
	
	private static Config config;

	public static Object economy = null;
	
	@SuppressWarnings("unused")
	private static Metrics metrics = null;
	
	public void onLoad() {
		PseudoUpdater.registerPlugin(this);
	}

	@Override
	public void onEnable() {
		super.onEnable();
		this.saveDefaultConfig();
		plugin = this;
		if (!initVault())
			return;
		PlayerDataController.addColumn(new Column("xpwood", "INT(8)", "0"));
		PlayerDataController.addColumn(new Column("xpmining", "INT(8)", "0"));
		PlayerDataController.addColumn(new Column("xpfishing", "INT(8)", "0"));
		PlayerDataController.addColumn(new Column("xphunting", "INT(8)", "0"));
		PlayerDataController.addColumn(new Column("xpsmelting", "INT(8)", "0"));
		PlayerDataController.addColumn(new Column("xpcrafting", "INT(8)", "0"));
		PlayerDataController.addColumn(new Column("xpblacksmith", "INT(8)", "0"));
		config = new Config(this);
		config.updateConfig();
		config.reloadConfig();
		mainCommand = new MainCommand(plugin);
		helpSubCommand = new HelpSC(plugin);
		xpSubCommand = new XpSC();
		xpSkillSubCommand = new XpSkillSC();
		giveXPSubCommand = new SkillXPSC();
		initializeCommands();
		initializeTabcompleters();
		initializeSubCommands();
		initializeListeners();
		setCommandDescriptions();
		PseudoAPI.registerConfig(config);
		initializeMetrics();
	}

	@Override
	public void onDisable() {
		DataController.saveXP();
		super.onDisable();
	}
	
	private void initializeMetrics() {
		metrics = new Metrics(this);
	}

	public static Config getConfigOptions() {
		return PseudoRPG.config;
	}

	private boolean initVault() {
		if (getServer().getPluginManager().getPlugin("Vault") != null) {
			try {
				RegisteredServiceProvider<?> economyProvider = getServer().getServicesManager().getRegistration(Class.forName("net.milkbowl.vault.economy.Economy"));
				if (economyProvider != null)
					economy = economyProvider.getProvider();
				else {
					getChat().sendConsolePluginError(Chat.Errors.CUSTOM, LanguageManager.getLanguage().getMessage("pseudorpg.error_no_vault_economy_loaded"));
					this.setEnabled(false);
					return false;
				}
			} catch (ClassNotFoundException e) {
				getChat().sendConsolePluginError(Chat.Errors.CUSTOM, LanguageManager.getLanguage().getMessage("pseudorpg.error_no_vault_loaded"));
				this.setEnabled(false);
				return false;
			}
		} else {
			getChat().sendConsolePluginError(Chat.Errors.CUSTOM, LanguageManager.getLanguage().getMessage("pseudorpg.error_no_vault_loaded"));
			this.setEnabled(false);
			return false;
		}
		return true;
	}

	private void initializeCommands() {
		this.getCommand("pseudorpg").setExecutor(mainCommand);
		this.getCommand("rpgxp").setExecutor(xpSubCommand);
		this.getCommand("rpgxpskill").setExecutor(xpSkillSubCommand);
		this.getCommand("skillxp").setExecutor(giveXPSubCommand);
	}

	private void initializeSubCommands() {
		subCommands.put("help", helpSubCommand);
		subCommands.put("reload", new ReloadSC());
		subCommands.put("reset", new ResetSC());
		subCommands.put("xp", xpSubCommand);
		subCommands.put("xpskill", xpSkillSubCommand);
		subCommands.put("skillxp", giveXPSubCommand);
	}

	private void initializeTabcompleters() {
		this.getCommand("pseudorpg").setTabCompleter(new RPGTC());
		this.getCommand("rpgxp").setTabCompleter(new RPGXPTC());
		this.getCommand("rpgxpskill").setTabCompleter(new RPGXPSkillTC());
		this.getCommand("skillxp").setTabCompleter(new SkillXPTC());
	}
	private void setCommandDescriptions() {
		commandDescriptions.add(new CommandDescription("pseudorpg", "pseudorpg.pseudorpg_help", ""));
		commandDescriptions.add(new CommandDescription("pseudorpg help", "pseudorpg.pseudorpg_help_help", ""));
		commandDescriptions.add(new CommandDescription("pseudorpg reload", "pseudorpg.pseudorpg_reload_help", "pseudorpg.reload"));
		commandDescriptions.add(new CommandDescription("pseudorpg reloadlocalization", "pseudorpg.pseudorpg_reloadlocalization_help", "pseudorpg.reloadlocalization"));
		commandDescriptions.add(new CommandDescription("pseudorpg reset", "pseudorpg.pseudorpg_reset_help", "pseudorpg.reset"));
		commandDescriptions.add(new CommandDescription("pseudorpg resetlocalization", "pseudorpg.pseudorpg_resetlocalization_help", "pseudorpg.resetlocalization"));
		commandDescriptions.add(new CommandDescription("pseudorpg xp", "pseudorpg.pseudorpg_xp_help", "rpg.xp"));
		commandDescriptions.add(new CommandDescription("pseudorpg xpskill <skill>", "pseudorpg.pseudorpg_xpskill_help", "rpg.xp", false));
		commandDescriptions.add(new CommandDescription("pseudorpg skillxp <add|set> <skill> <amount> (player)", "pseudorpg.pseudorpg_skillxp_help", "rpg.skillxp", false));
	}

	public void initializeListeners() {
		getServer().getPluginManager().registerEvents(new BlockBreakEH(), this);
		getServer().getPluginManager().registerEvents(new EntityDeathEH(), this);
		getServer().getPluginManager().registerEvents(new EntityDamageEH(), this);
		getServer().getPluginManager().registerEvents(new PlayerJoinLeaveL(), this);
	}

}