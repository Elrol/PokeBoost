package com.github.elrol.minerboost.libs;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.github.elrol.minerboost.MinerBoost;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class PluginInfo {

    private static final String[] infoIds = new String[] { "info", "ivs", "evs", "moves" };
    private static final String[] statIds = new String[] { "level", "shiny", "nature", "ability", "growth", "iv_health",
            "iv_attack", "iv_defence", "iv_special_attack", "iv_special_defence", "iv_speed", "ev_health", "ev_attack",
            "ev_defence", "ev_special_attack", "ev_special_defence", "ev_speed", "move1", "move2", "move3", "move4" };

    public static final String pluginName = "MinerBoost";
    public static final String pluginId = "minerboost";
    public static final String pluginVersion = "3.1";

    public static Text bpSingular = Text.of(TextColors.BLUE, "Battle", TextColors.AQUA, "Point");
    public static Text bpPlural = Text.of(TextColors.BLUE, "Battle", TextColors.AQUA, "Points");
    public static Text bpSymbol = Text.of(TextColors.BLUE, "B", TextColors.AQUA, "P");

    public static Text epSingular = Text.of(TextColors.GOLD, "E", TextColors.YELLOW, "P");
    public static Text epPlural = Text.of(TextColors.GOLD, "E", TextColors.YELLOW, "P");
    public static Text epSymbol = Text.of(TextColors.GOLD, "E", TextColors.YELLOW, "P");

    public static int levelCost;
    public static boolean canDecrease;
    public static int shinyCost;
    public static int natureCost;
    public static int abilityCost;
    public static int hiddenAbilityCost;
    public static int growthCost;
    public static int ivCost;
    public static int evCost;

    public static int shinyCustomizeCost;
    public static int growthCustomizeCost;
    public static int pokeballCustomizeCost;
    public static int premiumPokeballCustomizeCost;

    public static List<String> premiumPokeballs;
    public static int pdConversion;
    public static int pcConversion;
    public static int scConversion;
    public static List<String> pokeBoostBlacklist;
    public static List<String> pokeCustomizeBlacklist;

    public static void registerInfo() {
        CommentedConfigurationNode config = MinerBoost.getInstance().getConfigManager().getConfig();
        levelCost = config.getNode("Costs", "BattlePoints", "Level").getInt();
        canDecrease = config.getNode("General", "CanDecrease").getBoolean();
        shinyCost = config.getNode("Costs", "BattlePoints", "Shiny").getInt();
        natureCost = config.getNode("Costs", "BattlePoints", "Nature").getInt();
        abilityCost = config.getNode("Costs", "BattlePoints", "Ability").getInt();
        hiddenAbilityCost = config.getNode("Costs", "BattlePoints", "HiddenAbility").getInt();
        growthCost = config.getNode("Costs", "BattlePoints", "Growth").getInt();
        ivCost = config.getNode("Costs", "BattlePoints", "Ivs").getInt();
        evCost = config.getNode("Costs", "BattlePoints", "Evs").getInt();

        shinyCustomizeCost = config.getNode("Costs", "EventPoints", "Shiny").getInt();
        growthCustomizeCost = config.getNode("Costs", "EventPoints", "Growth").getInt();
        pokeballCustomizeCost = config.getNode("Costs", "EventPoints", "Pokeball").getInt();
        premiumPokeballCustomizeCost = config.getNode("Costs", "EventPoints", "Premium Pokeball").getInt();
        premiumPokeballs = config.getNode("General", "Premium Pokeballs").getList(stringTransformer);
        pdConversion = config.getNode("General", "Pokedollar Conversion").getInt();
        pcConversion = config.getNode("General", "PokeCrate Conversion").getInt();
        scConversion = config.getNode("General", "ShinyCrate Conversion").getInt();

        pokeBoostBlacklist = config.getNode("General", "Boost Blacklist").getList(stringTransformer);
        pokeCustomizeBlacklist = config.getNode("General", "Customize Blacklist").getList(stringTransformer);
    }

    public static List<String> getInfoList() {
        List<String> list = new ArrayList<String>();
        for (String id : infoIds) {
            list.add(id);
        }
        return list;
    }

    public static List<String> getStatList() {
        List<String> list = new ArrayList<String>();
        for (String id : statIds) {
            list.add(id);
        }
        return list;
    }

    static Function<Object, String> stringTransformer = new Function<Object, String>() {
        @Override
        public String apply(Object input) {
            if (input instanceof String) {
                return (String) input;
            } else {
                return null;
            }
        }
    };

}
