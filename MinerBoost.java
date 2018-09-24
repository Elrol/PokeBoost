package com.github.elrol.minerboost;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.text.Text;

import com.github.elrol.minerboost.commands.BattlePointsExecutor;
import com.github.elrol.minerboost.commands.BattlePointsModifyExecutor;
import com.github.elrol.minerboost.commands.BattlePointsSetExecutor;
import com.github.elrol.minerboost.commands.BoostExecutor;
import com.github.elrol.minerboost.commands.CustomizeExecutor;
import com.github.elrol.minerboost.commands.EventPointsExecutor;
import com.github.elrol.minerboost.commands.EventPointsModifyExecutor;
import com.github.elrol.minerboost.commands.EventPointsSetExecutor;
import com.github.elrol.minerboost.commands.ExchangeExecutor;
import com.github.elrol.minerboost.configuration.ConfigurationManager;
import com.github.elrol.minerboost.libs.Permissions;
import com.github.elrol.minerboost.libs.PluginInfo;
import com.github.elrol.minerboost.utils.BattlePointUtils;
import com.github.elrol.minerboost.utils.EventPointUtils;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import net.minecraftforge.fml.common.Mod.Instance;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@Plugin(id = PluginInfo.pluginId, name = PluginInfo.pluginId, version = PluginInfo.pluginId, authors = {
        "Elrol_Arrowsend" }, dependencies = { @Dependency(id = "pixelmon"), @Dependency(id = "minercore"),
                @Dependency(id = "economylite") })

public class MinerBoost {

    private ConfigurationManager configManager;
    @Inject
    private Logger logger;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private Path defaultConfig;
    @Inject
    @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> loader;
    @Inject
    @ConfigDir(sharedRoot = false)
    private Path privateConfigDir;

    private Random r = new Random();
    
    @Instance
    public static MinerBoost instance;

    @Listener
    public void gameStoppingEvent(GameStoppingServerEvent event) {
        System.out.print("Saving profiles and unloading PixelEvents");
    }
    
	@Listener
	public void gameStartingEvent(GameStartedServerEvent event) {
	    registerCurrencies();
	    System.out.print("Call Elrol if an issue occurs: 217-430-4908");
	}

    @Listener
    public void onStartEvent(GameInitializationEvent event) {
        instance = this;

        configManager = new ConfigurationManager();

        logger.info("Starting PixelEvents");
        try {
            configManager.setup(loader, defaultConfig);
        } catch (IOException e) {
            logger.error("Error reloading config", e);
        }
        PluginInfo.registerInfo();
        registerCommands();
    }

    @Listener
    public void onReloadEvent(GameReloadEvent event) {
        try {
            configManager.setup(loader, defaultConfig);
        } catch (IOException e) {
            logger.error("Error reloading config", e);
        }
        PluginInfo.registerInfo();
    }

    public Random getRandom() {
        return r;
    }

    public Logger getLogger() {
        return logger;
    }

    public ConfigurationManager getConfigManager() {
        return configManager;
    }

    private void registerCommands() {
        CommandSpec battlePoints_Set = CommandSpec.builder().description(Text.of("Set the BattlePoints of a player"))
                .executor(new BattlePointsSetExecutor()).permission(Permissions.battlePoints_Set)
                .arguments(GenericArguments.optional(GenericArguments.user(Text.of("player"))),
                        GenericArguments.integer(Text.of("points")))
                .build();

        CommandSpec battlePoints_Modify = CommandSpec.builder()
                .description(Text.of("Modify the BattlePoints of a player")).executor(new BattlePointsModifyExecutor())
                .permission(Permissions.battlePoints_Modify)
                .arguments(GenericArguments.optional(GenericArguments.user(Text.of("player"))),
                        GenericArguments.integer(Text.of("points")))
                .build();

        CommandSpec battlePoints = CommandSpec.builder().description(Text.of("Displays how many BattlePoints you have"))
                .executor(new BattlePointsExecutor()).permission(Permissions.battlePoints)
                .arguments(GenericArguments.optional(GenericArguments.user(Text.of("player"))))
                .child(battlePoints_Modify, Lists.newArrayList("modify", "mod"))
                .child(battlePoints_Set, Lists.newArrayList("set", "s")).build();

        CommandSpec eventPoints_Set = CommandSpec.builder().description(Text.of("Set the EventPoints of a player"))
                .executor(new EventPointsSetExecutor()).permission(Permissions.eventPoints_Set)
                .arguments(GenericArguments.optional(GenericArguments.user(Text.of("player"))),
                        GenericArguments.integer(Text.of("points")))
                .build();

        CommandSpec eventPoints_Modify = CommandSpec.builder()
                .description(Text.of("Modify the EventPoints of a player")).executor(new EventPointsModifyExecutor())
                .permission(Permissions.eventPoints_Modify)
                .arguments(GenericArguments.optional(GenericArguments.user(Text.of("player"))),
                        GenericArguments.integer(Text.of("points")))
                .build();

        CommandSpec eventPoints = CommandSpec.builder().description(Text.of("Displays how many EventPoints you have"))
                .executor(new EventPointsExecutor()).permission(Permissions.eventPoints)
                .arguments(GenericArguments.optional(GenericArguments.user(Text.of("player"))))
                .child(eventPoints_Modify, Lists.newArrayList("modify", "mod"))
                .child(eventPoints_Set, Lists.newArrayList("set", "s")).build();

        CommandSpec boost = CommandSpec.builder().description(Text.of("Displays the Boost menu for a pokemon"))
                .executor(new BoostExecutor()).permission(Permissions.boost)
                .arguments(GenericArguments.integer(Text.of("slot"))).build();

        CommandSpec customize = CommandSpec.builder().description(Text.of("Displays the Customize menu for a pokemon"))
                .executor(new CustomizeExecutor()).permission(Permissions.customize)
                .arguments(GenericArguments.integer(Text.of("slot"))).build();

        CommandSpec exchange = CommandSpec.builder().description(Text.of("Displays the Exchange menu for EventPoints"))
                .executor(new ExchangeExecutor()).permission(Permissions.exchange).build();

        Sponge.getCommandManager().register(this, battlePoints, Lists.newArrayList("battlepoints", "bp"));
        Sponge.getCommandManager().register(this, eventPoints, Lists.newArrayList("eventpoints", "ep"));
        Sponge.getCommandManager().register(this, boost, Lists.newArrayList("boost"));
        Sponge.getCommandManager().register(this, customize, Lists.newArrayList("customize", "custom"));
        Sponge.getCommandManager().register(this, exchange, Lists.newArrayList("exchange"));

    }

    public static MinerBoost getInstance() {
        return instance;
    }
    
	public static void registerCurrencies() {
		Optional<EconomyService> economyService = Sponge.getServiceManager().provide(EconomyService.class);
	    if(!economyService.isPresent() && !economyService.get().getCurrencies().contains(EventPointUtils.getEventPoints())) {
			Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "currency create " + PluginInfo.epSingular.toPlain() + " " + PluginInfo.epPlural.toPlain() + " " + PluginInfo.epSymbol.toPlain());
			System.out.print("EventPoint currency added");
		} else {
			System.out.print("EventPoint currency, already exists");
		}
	    
	    if(!economyService.isPresent() && !economyService.get().getCurrencies().contains(BattlePointUtils.getBattlePoints())) {
			Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "currency create " + PluginInfo.bpSingular.toPlain() + " " + PluginInfo.bpPlural.toPlain() + " " + PluginInfo.bpSymbol.toPlain());
			System.out.print("BattlePoint currecny added");
		} else {
			System.out.print("BattlePoint currency, already exists");
		}
	}

}