package com.github.elrol.minerboost.configuration;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.Function;

import org.slf4j.Logger;

import com.github.elrol.minerboost.MinerBoost;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class ConfigurationManager {

    MinerBoost plugin;
    Logger logger;
    /* String transformer for converting ? Object to a String */
    Function<Object, String> stringTransformer = new Function<Object, String>() {
        @Override
        public String apply(Object input) {
            if (input instanceof String) {
                return (String) input;
            } else {
                return null;
            }
        }
    };

    public ConfigurationManager() {
        this.plugin = MinerBoost.instance;
        this.logger = plugin.getLogger();
    }

    public CommentedConfigurationNode getConfig() {
        return config;
    }

    public ConfigurationLoader<CommentedConfigurationNode> getLoader() {
        return loader;
    }

    private CommentedConfigurationNode config;

    ConfigurationLoader<CommentedConfigurationNode> loader;

    public void setup(ConfigurationLoader<CommentedConfigurationNode> loader, Path defaultConfig) throws IOException {
        plugin.getLogger().info("Running MinerBoost setup");
        this.loader = loader;
        ArrayList<String> defaultPremium = new ArrayList<String>();
        defaultPremium.add(EnumPokeballs.CherishBall.name());
        defaultPremium.add(EnumPokeballs.MasterBall.name());
        defaultPremium.add(EnumPokeballs.ParkBall.name());
        defaultPremium.add(EnumPokeballs.GSBall.name());

        ArrayList<String> pokeBoostBlackList = new ArrayList<String>();
        pokeBoostBlackList.add(EnumPokemon.Ditto.name());

        ArrayList<String> pokeCustomizeBlackList = new ArrayList<String>();

        config = loader.load();

        // If no file exists, put in some default values
        if (!defaultConfig.toFile().exists()) {
            config.getNode("Costs", "BattlePoints", "Level")
                    .setComment("How many BattlePoints to charge for each level (int)").setValue(1);
            config.getNode("General", "CanDecrease").setComment("Can players decrease their pokemon's level (boolean)")
                    .setValue(true);
            config.getNode("Costs", "BattlePoints", "Shiny")
                    .setComment("How many BattlePoints to charge for making a pokemon shiny (int)").setValue(500);
            config.getNode("Costs", "BattlePoints", "Nature")
                    .setComment("How many BattlePoints to change the nature (int)").setValue(500);
            config.getNode("Costs", "BattlePoints", "Ability")
                    .setComment("How many BattlePoinst to change the ability to a regular ability (int)").setValue(100);
            config.getNode("Costs", "BattlePoints", "HiddenAbility")
                    .setComment("How many BattlePoints to change the ability to a hidden ability (int)").setValue(1000);
            config.getNode("Costs", "BattlePoints", "Growth").setComment("How many BattlePoints to change the growth")
                    .setValue(25);
            config.getNode("Costs", "BattlePoints", "Ivs")
                    .setComment("How many BattlePoints to change the Ivs by 1 (int)").setValue(1);
            config.getNode("Costs", "BattlePoints", "Evs")
                    .setComment("How many BattlePoints to change the Evs by 1 (int)").setValue(1);
            config.getNode("Costs", "EventPoints", "Shiny")
                    .setComment("How many EventPoints to charge for making a pokemon shiny (int)").setValue(2000);
            config.getNode("Costs", "EventPoints", "Growth")
                    .setComment("How many EventPoints to charge for changing a pokemon's growth (int)").setValue(100);
            config.getNode("Costs", "EventPoints", "Pokeball")
                    .setComment("How many EventPoints to charge for changing a pokemon's pokeball (int)").setValue(100);
            config.getNode("Costs", "EventPoints", "Premium Pokeball")
                    .setComment(
                            "How many EventPoints to charge for changing a pokemon's pokeball to a premium ball (int)")
                    .setValue(250);
            config.getNode("General", "Premium Pokeballs").setComment("List of pokeball names that should be premium")
                    .setValue(defaultPremium);
            config.getNode("General", "Pokedollar Conversion")
                    .setComment("How many PokeDollars each EventPoint is worth (int)").setValue(400);
            config.getNode("General", "PokeCrate Conversion")
                    .setComment("How many EventPoints a PokeCrate Key is worth (int)").setValue(2500);
            config.getNode("General", "ShinyCrate Conversion")
                    .setComment("How many EventPoints a ShinyCrate Key is worth (int)").setValue(5000);
            config.getNode("General", "Boost Blacklist").setComment("A list of pokemon that cant be boosted")
                    .setValue(pokeBoostBlackList);
            config.getNode("General", "Customize Blacklist").setComment("A list of pokemon that cant be customized")
                    .setValue(pokeCustomizeBlackList);
            loader.save(config);
        }
    }
}