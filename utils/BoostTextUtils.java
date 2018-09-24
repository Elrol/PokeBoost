package com.github.elrol.minerboost.utils;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Text.Builder;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import com.github.elrol.minerboost.commands.BoostExecutor;
import com.github.elrol.minerboost.commands.CustomizeExecutor;
import com.github.elrol.minerboost.libs.PluginInfo;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;

public class BoostTextUtils {

    public static Text battlePoints = Text.of(TextColors.BLUE, "Battle", TextColors.AQUA, "Points");
    public static Text bp = Text.of(TextColors.BLUE, "B", TextColors.AQUA, "P");
    public static Text padding = Text.of(" ");
    public static Text header = Text.of(TextColors.YELLOW, TextStyles.BOLD, "[", TextColors.GRAY, "BOOST",
            TextColors.YELLOW, "] ", TextStyles.RESET);
    public static Text customizeHeader = Text.of(TextColors.YELLOW, TextStyles.BOLD, "[", TextColors.GRAY, "CUSTOM",
            TextColors.YELLOW, "] ", TextStyles.RESET);
    public static Text exchangeHeader = Text.of(TextColors.YELLOW, TextStyles.BOLD, "[", TextColors.GRAY, "EXCHANGE",
            TextColors.YELLOW, "] ", TextStyles.RESET);
    public static Text eventPoints = Text.of(TextColors.GOLD, "Event", TextColors.YELLOW, "Points");
    public static Text ep = Text.of(TextColors.GOLD, "E", TextColors.YELLOW, "P");

    public static Text cost(int cost) {
        return Text.of(TextColors.DARK_PURPLE, "[", TextColors.LIGHT_PURPLE, "Cost: ", cost, TextColors.DARK_PURPLE,
                "]");
    }

    public static void getCustomize(Player player, EntityPixelmon pokemon, int slot) {
        String name = pokemon.getName();
        Builder builder = Text.builder();
        builder.append(Text.of(customizeHeader, TextColors.LIGHT_PURPLE, name, padding));
        Text shiny = Text.builder().append(Text.of(TextColors.GRAY, "[", TextColors.RED, "Shiny", TextColors.GRAY, "]"))
                .onClick(TextActions.executeCallback(src -> getCustomizeShiny(player, pokemon, slot)))
                .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to set the shiny status of a pokemon")))
                .build();
        Text growth = Text.builder()
                .append(Text.of(TextColors.GRAY, "[", TextColors.GREEN, "Growth", TextColors.GRAY, "]"))
                .onClick(TextActions.executeCallback(src -> getCustomizeGrowth(player, pokemon, slot)))
                .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to set the growth of a pokemon")))
                .build();
        Text pokeball = Text.builder()
                .append(Text.of(TextColors.GRAY, "[", TextColors.BLUE, "Pokeball", TextColors.GRAY, "]"))
                .onClick(TextActions.executeCallback(src -> getPokeball(player, pokemon, slot)))
                .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to set the pokeball of a pokemon")))
                .build();
        builder.append(shiny, padding, growth, padding, pokeball);
        player.sendMessage(builder.build());

    }

    public static void getExchange(Player player) {
        Builder builder = Text.builder();
        builder.append(Text.of(exchangeHeader, TextColors.LIGHT_PURPLE, "Exhange ", eventPoints,
                TextColors.LIGHT_PURPLE, " for what? "));
        Text pd = Text.builder()
                .append(Text.of(TextColors.GRAY, "[", TextColors.GOLD, "PokeDollars", TextColors.GRAY, "]"))
                .onClick(TextActions.executeCallback(src -> getPdExchange((Player) src))).build();
        Text pc = Text.builder()
                .append(Text.of(TextColors.GRAY, "[", TextColors.GOLD, "PokeCrate keys", TextColors.GRAY, "]"))
                .onClick(TextActions.executeCallback(src -> getPcExchange((Player) src))).build();
        Text sc = Text.builder()
                .append(Text.of(TextColors.GRAY, "[", TextColors.GOLD, "ShinyCrate keys", TextColors.GRAY, "]"))
                .onClick(TextActions.executeCallback(src -> getScExchange((Player) src))).build();
        builder.append(pd, padding, pc, padding, sc);
        player.sendMessage(builder.build());

    }

    public static void getScExchange(Player player) {
        int bal = Integer.valueOf(EventPointUtils.getBalance(player).intValueExact());
        int cost = PluginInfo.scConversion;
        Builder builder = Text.builder();
        builder.append(Text.of(exchangeHeader, TextColors.LIGHT_PURPLE, "How many ", TextColors.GOLD, "ShinyCrate Keys",
                TextColors.LIGHT_PURPLE, " do you want to buy?: "));
        if (bal >= cost) {
            builder.append(Text.builder().append(Text.of(TextColors.GREEN, "[1]", padding))
                    .onClick(TextActions.executeCallback(src -> exchangeShinyCrate((Player) src, 1)))
                    .onHover(TextActions
                            .showText(Text.of(TextColors.AQUA, "Exchange " + cost + " EP for " + " 1 ShinyCrate Key")))
                    .build());
        } else {
            builder.append(Text.builder().append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[1]", padding))
                    .onHover(TextActions
                            .showText(Text.of(TextColors.RED, "You dont have enough Event Points [" + cost + "]")))
                    .build());
        }
        if (bal >= cost) {
            builder.append(Text.builder().append(Text.of(TextColors.GREEN, "[5]", padding))
                    .onClick(TextActions.executeCallback(src -> exchangeShinyCrate((Player) src, 5)))
                    .onHover(TextActions.showText(
                            Text.of(TextColors.AQUA, "Exchange " + (cost * 5) + " EP for " + " 5 ShinyCrate Keys")))
                    .build());
        } else {
            builder.append(Text.builder().append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[5]", padding))
                    .onHover(TextActions.showText(
                            Text.of(TextColors.RED, "You dont have enough Event Points [" + (cost * 5) + "]")))
                    .build());
        }
        if (bal >= cost) {
            builder.append(Text.builder().append(Text.of(TextColors.GREEN, "[10]", padding))
                    .onClick(TextActions.executeCallback(src -> exchangeShinyCrate((Player) src, 10)))
                    .onHover(TextActions.showText(
                            Text.of(TextColors.AQUA, "Exchange " + (cost * 10) + " EP for " + " 10 ShinyCrate Keys")))
                    .build());
        } else {
            builder.append(Text.builder().append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[10]", padding))
                    .onHover(TextActions.showText(
                            Text.of(TextColors.RED, "You dont have enough Event Points [" + (cost * 10) + "]")))
                    .build());
        }
        player.sendMessage(builder.build());
    }

    public static void getPcExchange(Player player) {
        int bal = Integer.valueOf(EventPointUtils.getBalance(player).intValueExact());
        int cost = PluginInfo.pcConversion;
        Builder builder = Text.builder();
        builder.append(Text.of(exchangeHeader, TextColors.LIGHT_PURPLE, "How many ", TextColors.GOLD, "PokeCrate Keys",
                TextColors.LIGHT_PURPLE, " do you want to buy?: "));
        if (bal >= cost) {
            builder.append(Text.builder().append(Text.of(TextColors.GREEN, "[1]", padding))
                    .onClick(TextActions.executeCallback(src -> exchangePokeCrate((Player) src, 1)))
                    .onHover(TextActions
                            .showText(Text.of(TextColors.AQUA, "Exchange " + cost + " EP for " + " 1 PokeCrate Key")))
                    .build());
        } else {
            builder.append(Text.builder().append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[1]", padding))
                    .onHover(TextActions
                            .showText(Text.of(TextColors.RED, "You dont have enough Event Points [" + cost + "]")))
                    .build());
        }
        if (bal >= cost) {
            builder.append(Text.builder().append(Text.of(TextColors.GREEN, "[5]", padding))
                    .onClick(TextActions.executeCallback(src -> exchangePokeCrate((Player) src, 5)))
                    .onHover(TextActions.showText(
                            Text.of(TextColors.AQUA, "Exchange " + (cost * 5) + " EP for " + " 5 PokeCrate Keys")))
                    .build());
        } else {
            builder.append(Text.builder().append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[5]", padding))
                    .onHover(TextActions.showText(
                            Text.of(TextColors.RED, "You dont have enough Event Points [" + (cost * 5) + "]")))
                    .build());
        }
        if (bal >= cost) {
            builder.append(Text.builder().append(Text.of(TextColors.GREEN, "[10]", padding))
                    .onClick(TextActions.executeCallback(src -> exchangePokeCrate((Player) src, 10)))
                    .onHover(TextActions.showText(
                            Text.of(TextColors.AQUA, "Exchange " + (cost * 10) + " EP for " + " 10 PokeCrate Keys")))
                    .build());
        } else {
            builder.append(Text.builder().append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[10]", padding))
                    .onHover(TextActions.showText(
                            Text.of(TextColors.RED, "You dont have enough Event Points [" + (cost * 10) + "]")))
                    .build());
        }
        player.sendMessage(builder.build());
    }

    public static void getPdExchange(Player player) {
        int bal = Integer.valueOf(EventPointUtils.getBalance(player).intValueExact());
        Builder builder = Text.builder();
        builder.append(Text.of(exchangeHeader, TextColors.LIGHT_PURPLE, "How many ", eventPoints,
                TextColors.LIGHT_PURPLE, " to exchange: "));
        if (bal >= 1) {
            builder.append(Text.builder().append(Text.of(TextColors.GREEN, "[1]", padding))
                    .onClick(TextActions.executeCallback(src -> exchangePokeDollar((Player) src, 1)))
                    .onHover(TextActions.showText(
                            Text.of(TextColors.AQUA, "Exchange 1 EP for " + PluginInfo.pdConversion + " PokeDollars")))
                    .build());
        } else {
            builder.append(Text.builder().append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[1]", padding))
                    .onHover(TextActions.showText(Text.of(TextColors.RED, "You dont have enough Event Points")))
                    .build());
        }
        if (bal >= 5) {
            builder.append(Text.builder().append(Text.of(TextColors.GREEN, "[5]", padding))
                    .onClick(TextActions.executeCallback(src -> exchangePokeDollar((Player) src, 5)))
                    .onHover(TextActions.showText(Text.of(TextColors.AQUA,
                            "Exchange 5 EP for " + (PluginInfo.pdConversion * 5) + " PokeDollars")))
                    .build());
        } else {
            builder.append(Text.builder().append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[5]", padding))
                    .onHover(TextActions.showText(Text.of(TextColors.RED, "You dont have enough Event Points")))
                    .build());
        }
        if (bal >= 10) {
            builder.append(Text.builder().append(Text.of(TextColors.GREEN, "[10]", padding))
                    .onClick(TextActions.executeCallback(src -> exchangePokeDollar((Player) src, 10)))
                    .onHover(TextActions.showText(Text.of(TextColors.AQUA,
                            "Exchange 10 EP for " + (PluginInfo.pdConversion * 10) + " PokeDollars")))
                    .build());
        } else {
            builder.append(Text.builder().append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[10]", padding))
                    .onHover(TextActions.showText(Text.of(TextColors.RED, "You dont have enough Event Points")))
                    .build());
        }
        if (bal >= 50) {
            builder.append(Text.builder().append(Text.of(TextColors.GREEN, "[50]", padding))
                    .onClick(TextActions.executeCallback(src -> exchangePokeDollar((Player) src, 50)))
                    .onHover(TextActions.showText(Text.of(TextColors.AQUA,
                            "Exchange 50 EP for " + (PluginInfo.pdConversion * 50) + " PokeDollars")))
                    .build());
        } else {
            builder.append(Text.builder().append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[50]", padding))
                    .onHover(TextActions.showText(Text.of(TextColors.RED, "You dont have enough Event Points")))
                    .build());
        }
        if (bal >= 100) {
            builder.append(
                    Text.builder().append(Text.of(TextColors.GREEN, "[100]", padding))
                            .onClick(TextActions.executeCallback(src -> exchangePokeDollar((Player) src, 100)))
                            .onHover(TextActions.showText(Text.of(TextColors.AQUA,
                                    "Exchange 100 EP for " + (PluginInfo.pdConversion * 100) + " PokeDollars")))
                            .build());
        } else {
            builder.append(Text.builder().append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[100]", padding))
                    .onHover(TextActions.showText(Text.of(TextColors.RED, "You dont have enough Event Points")))
                    .build());
        }
        if (bal >= 1000) {
            builder.append(
                    Text.builder().append(Text.of(TextColors.GREEN, "[1000]", padding))
                            .onClick(TextActions.executeCallback(src -> exchangePokeDollar((Player) src, 1000)))
                            .onHover(TextActions.showText(Text.of(TextColors.AQUA,
                                    "Exchange 1000 EP for " + (PluginInfo.pdConversion * 1000) + " PokeDollars")))
                            .build());
        } else {
            builder.append(Text.builder().append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[1000]", padding))
                    .onHover(TextActions.showText(Text.of(TextColors.RED, "You dont have enough Event Points")))
                    .build());
        }
        if (bal >= 1) {
            builder.append(Text.builder().append(Text.of(TextColors.GREEN, "[Max]", padding))
                    .onClick(TextActions.executeCallback(src -> exchangePokeDollar((Player) src, bal)))
                    .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Exchange ", bal,
                            " EP for " + (PluginInfo.pdConversion * bal) + " PokeDollars")))
                    .build());
        } else {
            builder.append(
                    Text.builder().append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[" + bal + "]", padding))
                            .onHover(TextActions.showText(Text.of(TextColors.RED, "You dont have enough Event Points")))
                            .build());
        }
        player.sendMessage(builder.build());
    }

    public static void getPokeStats(Player player, EntityPixelmon pokemon, int slot) {
        String name = pokemon.getName();
        Builder builder = Text.builder();

        Builder infoBuilder = Text.builder();
        infoBuilder.append(Text.of(TextColors.AQUA, TextStyles.UNDERLINE, "General"), Text.NEW_LINE);
        infoBuilder.append(Text.of(TextColors.GREEN, "Level: ", pokemon.getLvl().getLevel(), Text.NEW_LINE));
        infoBuilder.append(Text.of(TextColors.LIGHT_PURPLE, "Shiny: ", pokemon.getIsShiny(), Text.NEW_LINE));
        infoBuilder.append(Text.of(TextColors.RED, "Nature: ", pokemon.getNature().name(), Text.NEW_LINE));
        infoBuilder.append(Text.of(TextColors.GOLD, "Ability: ", pokemon.getAbility().getName(), Text.NEW_LINE));
        infoBuilder.append(Text.of(TextColors.LIGHT_PURPLE, "Growth: ", pokemon.getGrowth().name()));

        Builder evsBuilder = Text.builder();
        evsBuilder.append(Text.of(TextColors.AQUA, TextStyles.UNDERLINE, "EVs"), Text.NEW_LINE);
        evsBuilder.append(Text.of(TextColors.GREEN, "HP: ", pokemon.stats.evs.hp, Text.NEW_LINE));
        evsBuilder.append(Text.of(TextColors.RED, "ATK: ", pokemon.stats.evs.attack, Text.NEW_LINE));
        evsBuilder.append(Text.of(TextColors.GOLD, "DEF: ", pokemon.stats.evs.defence, Text.NEW_LINE));
        evsBuilder.append(Text.of(TextColors.LIGHT_PURPLE, "SATK: ", pokemon.stats.evs.specialAttack, Text.NEW_LINE));
        evsBuilder.append(Text.of(TextColors.YELLOW, "SDEF: ", pokemon.stats.evs.specialDefence, Text.NEW_LINE));
        evsBuilder.append(Text.of(TextColors.DARK_AQUA, "SPD: ", pokemon.stats.evs.speed));

        Builder ivsBuilder = Text.builder();
        ivsBuilder.append(Text.of(TextColors.AQUA, TextStyles.UNDERLINE, "IVs"), Text.NEW_LINE);
        ivsBuilder.append(Text.of(TextColors.GREEN, "HP: ", pokemon.stats.ivs.HP, Text.NEW_LINE));
        ivsBuilder.append(Text.of(TextColors.RED, "ATK: ", pokemon.stats.ivs.Attack, Text.NEW_LINE));
        ivsBuilder.append(Text.of(TextColors.GOLD, "DEF: ", pokemon.stats.ivs.Defence, Text.NEW_LINE));
        ivsBuilder.append(Text.of(TextColors.LIGHT_PURPLE, "SATK: ", pokemon.stats.ivs.SpAtt, Text.NEW_LINE));
        ivsBuilder.append(Text.of(TextColors.YELLOW, "SDEF: ", pokemon.stats.ivs.SpDef, Text.NEW_LINE));
        ivsBuilder.append(Text.of(TextColors.DARK_AQUA, "SPD: ", pokemon.stats.ivs.Speed));

        Text info = Text.builder()
                .append(Text.of(TextColors.GRAY, "[", TextColors.YELLOW, "General", TextColors.GRAY, "]"))
                .onClick(TextActions.executeCallback(src -> getInfo((Player) src, pokemon, slot)))
                .onHover(TextActions.showText(infoBuilder.build())).build();
        Text ivs = Text.builder().append(Text.of(TextColors.GRAY, "[", TextColors.RED, "Ivs", TextColors.GRAY, "]"))
                .onClick(TextActions.executeCallback(src -> getIvs((Player) src, pokemon, slot)))
                .onHover(TextActions.showText(ivsBuilder.build())).build();
        Text evs = Text.builder().append(Text.of(TextColors.GRAY, "[", TextColors.GREEN, "Evs", TextColors.GRAY, "]"))
                .onClick(TextActions.executeCallback(src -> getEvs((Player) src, pokemon, slot)))
                .onHover(TextActions.showText(evsBuilder.build())).build();

        builder.append(Text.of(header, TextColors.LIGHT_PURPLE, name, padding));

        if (pokemon.isEgg) {
            builder.append(Text.of(TextColors.LIGHT_PURPLE, " Egg"));
        }
        builder.append(Text.of(info, padding));
        builder.append(Text.of(ivs, padding));
        if (!pokemon.isEgg)
            builder.append(Text.of(evs, padding));
        player.sendMessage(builder.build());
        return;
    }

    public static void getInfo(Player player, EntityPixelmon pokemon, int slot) {
        Builder builder = Text.builder();
        builder.append(Text.of(TextColors.YELLOW, TextStyles.BOLD, "[", TextColors.GRAY, "GENERAL ========",
                TextColors.YELLOW, "]", Text.NEW_LINE));
        builder.append(
                Text.builder().append(Text.of(TextColors.GREEN, "Level: ", pokemon.getLvl().getLevel()))
                        .onClick(TextActions.executeCallback(src -> getLevel((Player) src, pokemon, slot)))
                        .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to change the level"))).build(),
                Text.NEW_LINE);
        builder.append(Text.builder()
                .append(Text.of(TextColors.LIGHT_PURPLE, "Shiny: ",
                        pokemon.getIsShiny() ? Text.of(TextColors.GREEN, "Yes") : Text.of(TextColors.RED, "No")))
                .onClick(TextActions.executeCallback(src -> getShiny((Player) src, pokemon, slot)))
                .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to change the shiny status"))).build(),
                Text.NEW_LINE);
        builder.append(
                Text.builder().append(Text.of(TextColors.RED, "Nature: ", pokemon.getNature().name()))
                        .onClick(TextActions.executeCallback(src -> getNature((Player) src, pokemon, slot)))
                        .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to change the nature"))).build(),
                Text.NEW_LINE);
        builder.append(
                Text.builder().append(Text.of(TextColors.GOLD, "Ability: ", pokemon.getAbility().getName()))
                        .onClick(TextActions.executeCallback(src -> getAbility((Player) src, pokemon, slot)))
                        .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to change the ability"))).build(),
                Text.NEW_LINE);
        builder.append(
                Text.builder().append(Text.of(Text.of(TextColors.LIGHT_PURPLE, "Growth: ", pokemon.getGrowth().name())))
                        .onClick(TextActions.executeCallback(src -> getGrowth((Player) src, pokemon, slot)))
                        .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to change the growth"))).build());
        builder.append(Text.of(Text.NEW_LINE, TextColors.YELLOW, TextStyles.BOLD, "[", TextColors.GRAY,
                "================", TextColors.YELLOW, "]"));
        player.sendMessage(builder.build());
        return;
    }

    public static void getIvs(Player player, EntityPixelmon pokemon, int slot) {
        Builder builder = Text.builder();
        builder.append(Text.of(TextColors.YELLOW, TextStyles.BOLD, "[", TextColors.GRAY, "IVS ============",
                TextColors.YELLOW, "]", Text.NEW_LINE));
        builder.append(
                Text.builder().append(Text.of(TextColors.GREEN, "HP: ", pokemon.stats.ivs.HP))
                        .onClick(TextActions.executeCallback(src -> getIv((Player) src, pokemon, slot, 0)))
                        .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to change the HP IV"))).build(),
                Text.NEW_LINE);
        builder.append(Text.builder().append(Text.of(TextColors.RED, "Atk: ", pokemon.stats.ivs.Attack))
                .onClick(TextActions.executeCallback(src -> getIv((Player) src, pokemon, slot, 1)))
                .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to change the Attack IV"))).build(),
                Text.NEW_LINE);
        builder.append(Text.builder().append(Text.of(TextColors.GOLD, "Def: ", pokemon.stats.ivs.Defence))
                .onClick(TextActions.executeCallback(src -> getIv((Player) src, pokemon, slot, 2)))
                .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to change the Defense IV"))).build(),
                Text.NEW_LINE);
        builder.append(Text.builder().append(Text.of(TextColors.LIGHT_PURPLE, "SpAtk: ", pokemon.stats.ivs.SpAtt))
                .onClick(TextActions.executeCallback(src -> getIv((Player) src, pokemon, slot, 3)))
                .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to change the Special Attack IV")))
                .build(), Text.NEW_LINE);
        builder.append(
                Text.builder().append(Text.of(TextColors.YELLOW, "SpDef: ", pokemon.stats.ivs.SpDef))
                        .onClick(TextActions.executeCallback(src -> getIv((Player) src, pokemon, slot, 4)))
                        .onHover(TextActions
                                .showText(Text.of(TextColors.AQUA, "Click to change the Special Defense IV")))
                        .build(),
                Text.NEW_LINE);
        builder.append(Text.builder().append(Text.of(TextColors.DARK_AQUA, "Spd: ", pokemon.stats.ivs.Speed))
                .onClick(TextActions.executeCallback(src -> getIv((Player) src, pokemon, slot, 5)))
                .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to change the Speed IV"))).build());
        builder.append(Text.of(Text.NEW_LINE, TextColors.YELLOW, TextStyles.BOLD, "[", TextColors.GRAY,
                "================", TextColors.YELLOW, "]"));
        player.sendMessage(builder.build());
        return;
    }

    public static void getEvs(Player player, EntityPixelmon pokemon, int slot) {
        Builder builder = Text.builder();
        builder.append(Text.of(TextColors.YELLOW, TextStyles.BOLD, "[", TextColors.GRAY, "EVS ============",
                TextColors.YELLOW, "]", Text.NEW_LINE));
        builder.append(
                Text.builder().append(Text.of(TextColors.GREEN, "HP: ", pokemon.stats.evs.hp))
                        .onClick(TextActions.executeCallback(src -> getEv((Player) src, pokemon, slot, 0)))
                        .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to change the HP EV"))).build(),
                Text.NEW_LINE);
        builder.append(Text.builder().append(Text.of(TextColors.RED, "Atk: ", pokemon.stats.evs.attack))
                .onClick(TextActions.executeCallback(src -> getEv((Player) src, pokemon, slot, 1)))
                .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to change the Attack EV"))).build(),
                Text.NEW_LINE);
        builder.append(Text.builder().append(Text.of(TextColors.GOLD, "Def: ", pokemon.stats.evs.defence))
                .onClick(TextActions.executeCallback(src -> getEv((Player) src, pokemon, slot, 2)))
                .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to change the Defense EV"))).build(),
                Text.NEW_LINE);
        builder.append(Text.builder()
                .append(Text.of(TextColors.LIGHT_PURPLE, "SpAtk: ", pokemon.stats.evs.specialAttack))
                .onClick(TextActions.executeCallback(src -> getEv((Player) src, pokemon, slot, 3)))
                .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to change the Special Attack EV")))
                .build(), Text.NEW_LINE);
        builder.append(
                Text.builder().append(Text.of(TextColors.YELLOW, "SpDef: ", pokemon.stats.evs.specialDefence))
                        .onClick(TextActions.executeCallback(src -> getEv((Player) src, pokemon, slot, 4)))
                        .onHover(TextActions
                                .showText(Text.of(TextColors.AQUA, "Click to change the Special Defense EV")))
                        .build(),
                Text.NEW_LINE);
        builder.append(Text.builder().append(Text.of(TextColors.DARK_AQUA, "Spd: ", pokemon.stats.evs.speed))
                .onClick(TextActions.executeCallback(src -> getEv((Player) src, pokemon, slot, 5)))
                .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to change the Speed EV"))).build());
        builder.append(Text.of(Text.NEW_LINE, TextColors.YELLOW, TextStyles.BOLD, "[", TextColors.GRAY,
                "================", TextColors.YELLOW, "]"));
        player.sendMessage(builder.build());
        return;
    }

    // Info
    public static void getLevel(Player player, EntityPixelmon pokemon, int slot) {
        Builder builder = Text.builder();
        int level = pokemon.getLvl().getLevel();
        int mult = EnumPokemon.legendaries.contains(pokemon.getPokemonName()) ? 2 : 1;
        builder.append(Text.of(header));
        if (PluginInfo.canDecrease) {
            if (level > 1) {
                builder.append(Text.builder().append(Text.of(TextColors.RED, "[Min]", padding))
                        .onClick(TextActions.executeCallback(src -> getConfirmation(player, pokemon, slot, 0, "" + 1,
                                1 - pokemon.getLvl().getLevel())))
                        .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Sets the pokemon's level to 1 ",
                                cost(getLevelCost(level, 1) * mult))))
                        .build());
            } else {
                builder.append(Text.builder()
                        .append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[Min]", padding))
                        .onHover(TextActions.showText(Text.of(TextColors.RED, "Pokemon is already Level 1"))).build());
            }
            if (level > 10) {
                builder.append(Text.builder().append(Text.of(TextColors.RED, "[-10]", padding))
                        .onClick(TextActions.executeCallback(src -> getConfirmation(player, pokemon, slot, 0,
                                "" + (pokemon.getLvl().getLevel() - 10), -10)))
                        .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Decreases the pokemon's level by 10 ",
                                cost(getLevelCost(level, level - 10) * mult))))
                        .build());
            } else {
                builder.append(
                        Text.builder().append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[-10]", padding))
                                .onHover(TextActions
                                        .showText(Text.of(TextColors.RED, "Pokemon's level can't decrease 10 levels")))
                                .build());
            }
            if (level > 5) {
                builder.append(Text.builder().append(Text.of(TextColors.RED, "[-5]", padding))
                        .onClick(TextActions.executeCallback(src -> getConfirmation(player, pokemon, slot, 0,
                                "" + (pokemon.getLvl().getLevel() - 5), -5)))
                        .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Decreases the pokemon's level by 5 ",
                                cost(getLevelCost(level, level - 5) * mult))))
                        .build());
            } else {
                builder.append(
                        Text.builder().append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[-5]", padding))
                                .onHover(TextActions
                                        .showText(Text.of(TextColors.RED, "Pokemon's level can't decrease 5 levels")))
                                .build());
            }
            if (level > 1) {
                builder.append(Text.builder().append(Text.of(TextColors.RED, "[-1]", padding))
                        .onClick(TextActions.executeCallback(src -> getConfirmation(player, pokemon, slot, 0,
                                "" + (pokemon.getLvl().getLevel() - 1), -1)))
                        .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Decreases the pokemon's level by 1 ",
                                cost(getLevelCost(level, level - 1) * mult))))
                        .build());
            } else {
                builder.append(Text.builder()
                        .append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[-1]", padding))
                        .onHover(
                                TextActions.showText(Text.of(TextColors.RED, "Pokemon's level can't decrease 1 level")))
                        .build());
            }
        } else {
            builder.append(Text.builder().append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[Min]", padding))
                    .onHover(TextActions.showText(Text.of(TextColors.RED, "Pokemon is already Level 1"))).build());
            builder.append(Text.builder().append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[-10]", padding))
                    .onHover(TextActions.showText(Text.of(TextColors.RED, "Pokemon's level can't decrease 10 levels")))
                    .build());
            builder.append(Text.builder().append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[-5]", padding))
                    .onHover(TextActions.showText(Text.of(TextColors.RED, "Pokemon's level can't decrease 5 levels")))
                    .build());
            builder.append(Text.builder().append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[-1]", padding))
                    .onHover(TextActions.showText(Text.of(TextColors.RED, "Pokemon's level can't decrease 1 level")))
                    .build());
        }
        builder.append(Text.of(TextStyles.BOLD, TextColors.LIGHT_PURPLE, "Level: ", TextStyles.BOLD, level,
                TextStyles.RESET, padding));
        if (level <= 99) {
            builder.append(Text.builder().append(Text.of(TextColors.GREEN, "[+1]", padding))
                    .onClick(TextActions.executeCallback(src -> getConfirmation(player, pokemon, slot, 0,
                            "" + (pokemon.getLvl().getLevel() + 1), 1)))
                    .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Increases the pokemon's level by 1 ",
                            cost(getLevelCost(level, level + 1) * mult))))
                    .build());
        } else {
            builder.append(Text.builder().append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[+1]", padding))
                    .onHover(TextActions.showText(Text.of(TextColors.RED, "Pokemon's level can't increase 1 level")))
                    .build());
        }
        if (level <= 95) {
            builder.append(Text.builder().append(Text.of(TextColors.GREEN, "[+5]", padding))
                    .onClick(TextActions.executeCallback(src -> getConfirmation(player, pokemon, slot, 0,
                            "" + (pokemon.getLvl().getLevel() + 5), 5)))
                    .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Increases the pokemon's level by 5 ",
                            cost(getLevelCost(level, level + 5) * mult))))
                    .build());
        } else {
            builder.append(Text.builder().append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[+5]", padding))
                    .onHover(TextActions.showText(Text.of(TextColors.RED, "Pokemon's level can't increase 5 levels")))
                    .build());
        }
        if (level <= 90) {
            builder.append(Text.builder().append(Text.of(TextColors.GREEN, "[+10]", padding))
                    .onClick(TextActions.executeCallback(src -> getConfirmation(player, pokemon, slot, 0,
                            "" + (pokemon.getLvl().getLevel() + 10), 10)))
                    .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Increases the pokemon's level by 10 ",
                            cost(getLevelCost(level, level + 10) * mult))))
                    .build());
        } else {
            builder.append(Text.builder().append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[+10]", padding))
                    .onHover(TextActions.showText(Text.of(TextColors.RED, "Pokemon's level can't increase 10 levels")))
                    .build());
        }
        if (level < 100) {
            builder.append(Text.builder().append(Text.of(TextColors.GREEN, "[Max]", padding))
                    .onClick(TextActions.executeCallback(src -> getConfirmation(player, pokemon, slot, 0, "" + 100,
                            100 - pokemon.getLvl().getLevel())))
                    .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Sets the pokemon's level to 100 ",
                            cost(getLevelCost(level, 100) * mult))))
                    .build());
        } else {
            builder.append(Text.builder().append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[Max]", padding))
                    .onHover(TextActions.showText(Text.of(TextColors.RED, "Pokemon's level is already 100"))).build());
        }
        player.sendMessage(builder.build());
        return;
    }

    public static void getShiny(Player player, EntityPixelmon pokemon, int slot) {
        int mult = EnumPokemon.legendaries.contains(pokemon.getPokemonName()) ? 2 : 1;
        Builder builder = Text.builder();
        builder.append(Text.of(header, TextStyles.BOLD, TextColors.LIGHT_PURPLE, "Shiny: ", TextStyles.RESET));
        if (pokemon.getIsShiny()) {
            builder.append(Text.builder().append(Text.of(TextColors.RED, "[No]", padding))
                    .onClick(TextActions
                            .executeCallback(src -> getConfirmation((Player) src, pokemon, slot, 1, "false", 0)))
                    .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Sets the pokemon to no longer be shiny ",
                            cost(Math.round((PluginInfo.shinyCost * mult) / 2)))))
                    .build(), padding);
            builder.append(Text.builder().append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[Yes]", padding))
                    .onHover(TextActions.showText(Text.of(TextColors.RED, "Pokemon is already shiny"))).build());
        } else {
            builder.append(Text.builder().append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[No]", padding))
                    .onHover(TextActions.showText(Text.of(TextColors.RED, "Pokemon is already normal"))).build());
            builder.append(Text.builder().append(Text.of(TextColors.GREEN, "[Yes]"))
                    .onClick(TextActions
                            .executeCallback(src -> getConfirmation((Player) src, pokemon, slot, 1, "true", 0)))
                    .onHover(TextActions.showText(
                            Text.of(TextColors.AQUA, "Sets the pokemon to shiny ", cost(PluginInfo.shinyCost * mult))))
                    .build(), padding);
        }
        player.sendMessage(builder.build());
        return;
    }

    public static void getCustomizeShiny(Player player, EntityPixelmon pokemon, int slot) {
        int mult = EnumPokemon.legendaries.contains(pokemon.getPokemonName()) ? 2 : 1;
        Builder builder = Text.builder();
        builder.append(Text.of(customizeHeader, TextStyles.BOLD, TextColors.GOLD, "Shiny: ", TextStyles.RESET));
        if (pokemon.getIsShiny()) {
            builder.append(
                    Text.builder().append(Text.of(TextColors.RED, "[No]", padding))
                            .onClick(TextActions.executeCallback(
                                    src -> getCustomizeConfirmation((Player) src, pokemon, slot, 0, "false")))
                            .onHover(TextActions
                                    .showText(Text.of(TextColors.AQUA, "Sets the pokemon to no longer be shiny ",
                                            cost(Math.round((PluginInfo.shinyCustomizeCost * mult) / 2)))))
                            .build(),
                    padding);
            builder.append(Text.builder().append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[Yes]", padding))
                    .onHover(TextActions.showText(Text.of(TextColors.RED, "Pokemon is already shiny"))).build());
        } else {
            builder.append(Text.builder().append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[No]", padding))
                    .onHover(TextActions.showText(Text.of(TextColors.RED, "Pokemon is already normal"))).build());
            builder.append(Text.builder().append(Text.of(TextColors.GREEN, "[Yes]"))
                    .onClick(TextActions
                            .executeCallback(src -> getCustomizeConfirmation((Player) src, pokemon, slot, 0, "true")))
                    .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Sets the pokemon to shiny ",
                            cost(PluginInfo.shinyCustomizeCost * mult))))
                    .build(), padding);
        }
        player.sendMessage(builder.build());
        return;
    }

    public static void getNature(Player player, EntityPixelmon pokemon, int slot) {
        int mult = EnumPokemon.legendaries.contains(pokemon.getPokemonName()) ? 2 : 1;
        Builder builder = Text.builder();
        builder.append(
                Text.of(header, TextStyles.BOLD, TextColors.LIGHT_PURPLE, "Nature: ", TextStyles.RESET, Text.NEW_LINE));
        int i = 0;
        for (EnumNature nature : EnumNature.values()) {
            Text stats;
            if (nature.increasedStat == StatsType.None) {
                stats = Text.of(TextColors.WHITE, "No stat change");
            } else {
                stats = Text.of(TextColors.GREEN, nature.increasedStat.getLocalizedName(), TextColors.WHITE, "/",
                        TextColors.RED, nature.decreasedStat.getLocalizedName());
            }
            if (!pokemon.getNature().equals(nature)) {
                builder.append(
                        Text.builder()
                                .append(Text.of(TextColors.DARK_PURPLE, "[", i == 0 ? TextColors.RED
                                        : (i == 1 ? TextColors.YELLOW : (i == 2 ? TextColors.GREEN : TextColors.BLUE)),
                                        nature.getLocalizedName(), TextColors.DARK_PURPLE, "]",
                                        i == 3 ? Text.NEW_LINE : padding))
                                .onClick(TextActions.executeCallback(
                                        src -> getConfirmation((Player) src, pokemon, slot, 2, nature.name(), 0)))
                                .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Sets your nature to ",
                                        TextColors.BLUE, nature.getLocalizedName(), padding,
                                        cost(PluginInfo.natureCost * mult), Text.NEW_LINE, stats)))
                                .build());
            } else {
                builder.append(Text.builder()
                        .append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[", nature.getLocalizedName(), "]",
                                padding))
                        .onHover(TextActions.showText(Text.of(TextColors.RED,
                                "Pokemon already has the " + nature.getLocalizedName() + " nature")))
                        .build());
            }
            if (i < 3) {
                i++;
            } else {
                i = 0;
            }
        }
        player.sendMessage(builder.build());
        return;
    }

    public static void getAbility(Player player, EntityPixelmon pokemon, int slot) {
        int mult = EnumPokemon.legendaries.contains(pokemon.getPokemonName()) ? 2 : 1;
        String[] abilities = pokemon.baseStats.abilities;
        Builder builder = Text.builder();
        builder.append(Text.of(header, TextStyles.BOLD, TextColors.LIGHT_PURPLE, "Ability: ", TextStyles.RESET));
        for (int i = 0; i < abilities.length; i++) {
            final int j = i;
            if (abilities[i] != null) {
                if (!pokemon.getAbility().getName().equalsIgnoreCase(abilities[i])) {
                    Text ability = Text.of(TextColors.DARK_PURPLE, "[", TextColors.AQUA,
                            abilities[i].substring(0, 1).toUpperCase() + abilities[i].substring(1),
                            TextColors.DARK_PURPLE, "]");
                    builder.append(Text.builder().append(Text.of(ability, padding))
                            .onClick(TextActions.executeCallback(
                                    src -> getConfirmation((Player) src, pokemon, slot, 3, abilities[j], 0)))
                            .onHover(TextActions.showText(Text.of(TextColors.AQUA,
                                    "Sets your ability to " + abilities[i],
                                    cost((i == 2 ? PluginInfo.hiddenAbilityCost : PluginInfo.abilityCost) * mult))))
                            .build());
                } else {
                    builder.append(Text.builder().append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[",
                            abilities[i].substring(0, 1).toUpperCase() + abilities[i].substring(1), "]", padding))
                            .onHover(TextActions.showText(Text.of(TextColors.RED, "Pokemon already has that ability")))
                            .build());
                }
            }
        }
        player.sendMessage(builder.build());
        return;
    }

    public static void getGrowth(Player player, EntityPixelmon pokemon, int slot) {
        int mult = EnumPokemon.legendaries.contains(pokemon.getPokemonName()) ? 2 : 1;
        Builder builder = Text.builder();
        builder.append(Text.of(header, TextStyles.BOLD, TextColors.LIGHT_PURPLE, "Growth: ", TextStyles.RESET));
        int span = 0;
        for (EnumGrowth growth : EnumGrowth.values()) {
            if (!pokemon.getGrowth().equals(growth)) {
                builder.append(Text.builder()
                        .append(Text.of(TextColors.DARK_PURPLE, "[", TextColors.AQUA, growth.name(),
                                TextColors.DARK_PURPLE, "]", padding))
                        .onClick(TextActions.executeCallback(
                                src -> getConfirmation((Player) src, pokemon, slot, 4, growth.name(), 0)))
                        .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Sets the growth to ",
                                growth.getLocalizedName(), padding, cost(getGrowthCost(growth) * mult))))
                        .build());
            } else {
                builder.append(Text.builder()
                        .append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[", growth.getLocalizedName(), "]",
                                padding))
                        .onHover(TextActions.showText(Text.of(TextColors.RED,
                                "Pokemon already growth is already " + growth.getLocalizedName())))
                        .build());
            }
            if (span == 2) {
                builder.append(Text.NEW_LINE);
                span = 0;
            } else {
                span++;
            }
        }
        player.sendMessage(builder.build());
        return;
    }

    public static void getCustomizeGrowth(Player player, EntityPixelmon pokemon, int slot) {
        int mult = EnumPokemon.legendaries.contains(pokemon.getPokemonName()) ? 2 : 1;
        Builder builder = Text.builder();
        builder.append(
                Text.of(customizeHeader, TextStyles.BOLD, TextColors.LIGHT_PURPLE, "Growth: ", TextStyles.RESET));
        int span = 0;
        for (EnumGrowth growth : EnumGrowth.values()) {
            if (!pokemon.getGrowth().equals(growth)) {
                builder.append(Text.builder()
                        .append(Text.of(TextColors.DARK_PURPLE, "[", TextColors.AQUA, growth.name(),
                                TextColors.DARK_PURPLE, "]", padding))
                        .onClick(TextActions.executeCallback(
                                src -> getCustomizeConfirmation((Player) src, pokemon, slot, 1, growth.name())))
                        .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Sets the growth to ",
                                growth.getLocalizedName(), padding, cost(getGrowthCustomizeCost(growth) * mult))))
                        .build());
            } else {
                builder.append(Text.builder()
                        .append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[", growth.getLocalizedName(), "]",
                                padding))
                        .onHover(TextActions.showText(Text.of(TextColors.RED,
                                "Pokemon already growth is already " + growth.getLocalizedName())))
                        .build());
            }
            if (span == 2) {
                builder.append(Text.NEW_LINE);
                span = 0;
            } else {
                span++;
            }
        }
        player.sendMessage(builder.build());
        return;
    }

    public static void getIv(Player player, EntityPixelmon pokemon, int slot, int ivId) {
        int mult = EnumPokemon.legendaries.contains(pokemon.getPokemonName()) ? 2 : 1;
        Builder builder = Text.builder();
        String stat = "";
        int iv = -1;
        switch (ivId) {
        case 0:
            stat = "Hp IV";
            iv = pokemon.stats.ivs.HP;
            break;
        case 1:
            stat = "Attack IV";
            iv = pokemon.stats.ivs.Attack;
            break;
        case 2:
            stat = "Defense IV";
            iv = pokemon.stats.ivs.Defence;
            break;
        case 3:
            stat = "Special Attack IV";
            iv = pokemon.stats.ivs.SpAtt;
            break;
        case 4:
            stat = "Special Defense IV";
            iv = pokemon.stats.ivs.SpDef;
            break;
        case 5:
            stat = "Speed IV";
            iv = pokemon.stats.ivs.Speed;
            break;
        }
        final int ivFinal = iv;
        Text dec10 = iv >= 10 && PluginInfo.canDecrease ? Text.builder().append(Text.of(TextColors.RED, "[-10] "))
                .onClick(TextActions.executeCallback(
                        src -> getConfirmation((Player) src, pokemon, slot, ivId + 5, "" + (ivFinal - 10), -10)))
                .onHover(TextActions.showText(
                        Text.of(TextColors.AQUA, "Decreases the Iv by 10 ", cost(getIvCost(iv, iv - 10) * mult))))
                .build() : Text.of(TextColors.DARK_GRAY, TextStyles.STRIKETHROUGH, "[-10] ");
        Text dec5 = iv >= 5 && PluginInfo.canDecrease ? Text.builder().append(Text.of(TextColors.RED, "[-5] "))
                .onClick(TextActions.executeCallback(
                        src -> getConfirmation((Player) src, pokemon, slot, ivId + 5, "" + (ivFinal - 5), -5)))
                .onHover(TextActions.showText(
                        Text.of(TextColors.AQUA, "Decreases the Iv by 5 ", cost(getIvCost(iv, iv - 5) * mult))))
                .build() : Text.of(TextColors.DARK_GRAY, TextStyles.STRIKETHROUGH, "[-5] ");
        Text dec1 = iv >= 1 && PluginInfo.canDecrease ? Text.builder().append(Text.of(TextColors.RED, "[-1] "))
                .onClick(TextActions.executeCallback(
                        src -> getConfirmation((Player) src, pokemon, slot, ivId + 5, "" + (ivFinal - 1), -1)))
                .onHover(TextActions.showText(
                        Text.of(TextColors.AQUA, "Decreases the Iv by 1 ", cost(getIvCost(iv, iv - 1) * mult))))
                .build() : Text.of(TextColors.DARK_GRAY, TextStyles.STRIKETHROUGH, "[-1] ");
        Text inc10 = iv <= 21
                ? Text.builder().append(Text.of(TextColors.GREEN, "[+10] "))
                        .onClick(TextActions.executeCallback(
                                src -> getConfirmation((Player) src, pokemon, slot, ivId + 5, "" + (ivFinal + 10), 10)))
                        .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Increases the Iv by 10 ",
                                cost(getIvCost(iv, iv + 10) * mult))))
                        .build()
                : Text.of(TextColors.DARK_GRAY, TextStyles.STRIKETHROUGH, "[+10] ");
        Text inc5 = iv <= 26 ? Text.builder().append(Text.of(TextColors.GREEN, "[+5] "))
                .onClick(TextActions.executeCallback(
                        src -> getConfirmation((Player) src, pokemon, slot, ivId + 5, "" + (ivFinal + 5), 5)))
                .onHover(TextActions.showText(
                        Text.of(TextColors.AQUA, "Increases the Iv by 5 ", cost(getIvCost(iv, iv + 5) * mult))))
                .build() : Text.of(TextColors.DARK_GRAY, TextStyles.STRIKETHROUGH, "[+5] ");
        Text inc1 = iv <= 30 ? Text.builder().append(Text.of(TextColors.GREEN, "[+1] "))
                .onClick(TextActions.executeCallback(
                        src -> getConfirmation((Player) src, pokemon, slot, ivId + 5, "" + (ivFinal + 1), 1)))
                .onHover(TextActions.showText(
                        Text.of(TextColors.AQUA, "Increases the Iv by 1 ", cost(getIvCost(iv, iv + 1) * mult))))
                .build() : Text.of(TextColors.DARK_GRAY, TextStyles.STRIKETHROUGH, "[+1] ");
        Text min = iv > 0 && PluginInfo.canDecrease ? Text.builder().append(Text.of(TextColors.RED, "[Min] "))
                .onClick(TextActions.executeCallback(
                        src -> getConfirmation((Player) src, pokemon, slot, ivId + 5, "" + 0, -1 * ivFinal)))
                .onHover(TextActions
                        .showText(Text.of(TextColors.AQUA, "Sets the Iv to 0 ", cost(getIvCost(iv, 0) * mult))))
                .build() : Text.of(TextColors.DARK_GRAY, TextStyles.STRIKETHROUGH, "[Min] ");
        Text max = iv < 31
                ? Text.builder().append(Text.of(TextColors.GREEN, "[Max] "))
                        .onClick(TextActions.executeCallback(
                                src -> getConfirmation((Player) src, pokemon, slot, ivId + 5, "" + 31, 31 - ivFinal)))
                        .onHover(TextActions.showText(
                                Text.of(TextColors.AQUA, "Sets the Iv to 31 ", cost(getIvCost(iv, 31) * mult))))
                        .build()
                : Text.of(TextColors.DARK_GRAY, TextStyles.STRIKETHROUGH, "[Max] ");
        builder.append(Text.of(header, min, dec10, dec5, dec1, TextColors.LIGHT_PURPLE, stat + ": " + iv + "/31 ", inc1,
                inc5, inc10, max));
        player.sendMessage(builder.build());
        return;
    }

    public static void getEv(Player player, EntityPixelmon pokemon, int slot, int evId) {
        int mult = EnumPokemon.legendaries.contains(pokemon.getPokemonName()) ? 2 : 1;
        Builder builder = Text.builder();
        String stat = "";
        int ev = -1;
        int total = 0;
        switch (evId) {
        case 0:
            stat = "Hp EV";
            ev = pokemon.stats.evs.hp;
            break;
        case 1:
            stat = "Attack EV";
            ev = pokemon.stats.evs.attack;
            break;
        case 2:
            stat = "Defense EV";
            ev = pokemon.stats.evs.defence;
            break;
        case 3:
            stat = "Special Attack EV";
            ev = pokemon.stats.evs.specialAttack;
            break;
        case 4:
            stat = "Special Defense EV";
            ev = pokemon.stats.evs.specialDefence;
            break;
        case 5:
            stat = "Speed EV";
            ev = pokemon.stats.evs.speed;
            break;
        }
        total += pokemon.stats.evs.hp;
        total += pokemon.stats.evs.attack;
        total += pokemon.stats.evs.defence;
        total += pokemon.stats.evs.specialAttack;
        total += pokemon.stats.evs.specialDefence;
        total += pokemon.stats.evs.speed;
        final int evFinal = ev;
        Text dec50 = ev >= 50 && PluginInfo.canDecrease ? Text.builder().append(Text.of(TextColors.RED, "[-50] "))
                .onClick(TextActions.executeCallback(
                        src -> getConfirmation((Player) src, pokemon, slot, evId + 11, "" + (evFinal - 50), -50)))
                .onHover(TextActions.showText(
                        Text.of(TextColors.AQUA, "Decreases the Ev by 50 ", cost(50 * PluginInfo.evCost * mult))))
                .build() : Text.of(TextColors.DARK_GRAY, TextStyles.STRIKETHROUGH, "[-50] ");
        Text dec10 = ev >= 10 && PluginInfo.canDecrease ? Text.builder().append(Text.of(TextColors.RED, "[-10] "))
                .onClick(TextActions.executeCallback(
                        src -> getConfirmation((Player) src, pokemon, slot, evId + 11, "" + (evFinal - 10), -10)))
                .onHover(TextActions.showText(
                        Text.of(TextColors.AQUA, "Decreases the Ev by 10 ", cost(10 * PluginInfo.evCost * mult))))
                .build() : Text.of(TextColors.DARK_GRAY, TextStyles.STRIKETHROUGH, "[-10] ");
        Text dec5 = ev >= 5 && PluginInfo.canDecrease ? Text.builder().append(Text.of(TextColors.RED, "[-5] "))
                .onClick(TextActions.executeCallback(
                        src -> getConfirmation((Player) src, pokemon, slot, evId + 11, "" + (evFinal - 5), -5)))
                .onHover(TextActions.showText(
                        Text.of(TextColors.AQUA, "Decreases the Ev by 5 ", cost(5 * PluginInfo.evCost * mult))))
                .build() : Text.of(TextColors.DARK_GRAY, TextStyles.STRIKETHROUGH, "[-5] ");
        Text dec1 = ev >= 1 && PluginInfo.canDecrease ? Text.builder().append(Text.of(TextColors.RED, "[-1] "))
                .onClick(TextActions.executeCallback(
                        src -> getConfirmation((Player) src, pokemon, slot, evId + 11, "" + (evFinal - 1), -1)))
                .onHover(TextActions
                        .showText(Text.of(TextColors.AQUA, "Decreases the Ev by 1 ", cost(PluginInfo.evCost * mult))))
                .build() : Text.of(TextColors.DARK_GRAY, TextStyles.STRIKETHROUGH, "[-1] ");

        Text inc50 = ev <= 205 && total <= 460 ? Text.builder().append(Text.of(TextColors.GREEN, "[+50] "))
                .onClick(TextActions.executeCallback(
                        src -> getConfirmation((Player) src, pokemon, slot, evId + 11, "" + (evFinal + 50), 50)))
                .onHover(TextActions.showText(
                        Text.of(TextColors.AQUA, "Increases the Ev by 50 ", cost(50 * PluginInfo.evCost * mult))))
                .build() : Text.of(TextColors.DARK_GRAY, TextStyles.STRIKETHROUGH, "[+50] ");

        Text inc10 = ev <= 245 && total <= 500 ? Text.builder().append(Text.of(TextColors.GREEN, "[+10] "))
                .onClick(TextActions.executeCallback(
                        src -> getConfirmation((Player) src, pokemon, slot, evId + 11, "" + (evFinal + 10), 10)))
                .onHover(TextActions.showText(
                        Text.of(TextColors.AQUA, "Increases the Ev by 10 ", cost(10 * PluginInfo.evCost * mult))))
                .build() : Text.of(TextColors.DARK_GRAY, TextStyles.STRIKETHROUGH, "[+10] ");

        Text inc5 = ev <= 250 && total <= 505 ? Text.builder().append(Text.of(TextColors.GREEN, "[+5] "))
                .onClick(TextActions.executeCallback(
                        src -> getConfirmation((Player) src, pokemon, slot, evId + 11, "" + (evFinal + 5), 5)))
                .onHover(TextActions.showText(
                        Text.of(TextColors.AQUA, "Increases the Ev by 5 ", cost(5 * PluginInfo.evCost * mult))))
                .build() : Text.of(TextColors.DARK_GRAY, TextStyles.STRIKETHROUGH, "[+5] ");

        Text inc1 = ev <= 254 && total <= 509 ? Text.builder().append(Text.of(TextColors.GREEN, "[+1] "))
                .onClick(TextActions.executeCallback(
                        src -> getConfirmation((Player) src, pokemon, slot, evId + 11, "" + (evFinal + 1), 1)))
                .onHover(TextActions
                        .showText(Text.of(TextColors.AQUA, "Increases the Ev by 1 ", cost(PluginInfo.evCost * mult))))
                .build() : Text.of(TextColors.DARK_GRAY, TextStyles.STRIKETHROUGH, "[+1] ");

        Text min = ev != 0 && PluginInfo.canDecrease ? Text.builder().append(Text.of(TextColors.RED, "[Min] "))
                .onClick(TextActions.executeCallback(
                        src -> getConfirmation((Player) src, pokemon, slot, evId + 11, "" + 0, -1 * evFinal)))
                .onHover(TextActions
                        .showText(Text.of(TextColors.AQUA, "Sets the Ev to 0 ", cost(ev * PluginInfo.evCost * mult))))
                .build() : Text.of(TextColors.DARK_GRAY, TextStyles.STRIKETHROUGH, "[Min] ");

        Text max = ev != 252 && total <= (510 - (252 - ev)) ? Text.builder().append(Text.of(TextColors.GREEN, "[Max] "))
                .onClick(TextActions.executeCallback(
                        src -> getConfirmation((Player) src, pokemon, slot, evId + 11, "" + 252, 252 - evFinal)))
                .onHover(TextActions.showText(
                        Text.of(TextColors.AQUA, "Sets the Ev to 252 ", cost((252 - ev) * PluginInfo.evCost * mult))))
                .build() : Text.of(TextColors.DARK_GRAY, TextStyles.STRIKETHROUGH, "[Max] ");
        builder.append(Text.of(header, min, dec50, dec10, dec5, dec1, TextColors.LIGHT_PURPLE,
                stat + ": " + ev + "/252(", total, "/510) ", inc1, inc5, inc10, inc50, max));
        player.sendMessage(builder.build());
        return;
    }

    public static void getPokeball(Player player, EntityPixelmon pokemon, int slot) {
        int mult = EnumPokemon.legendaries.contains(pokemon.getPokemonName()) ? 2 : 1;
        Builder builder = Text.builder();
        builder.append(Text.of(customizeHeader, TextStyles.BOLD, TextColors.LIGHT_PURPLE, "Pokeball: ",
                TextStyles.RESET, Text.NEW_LINE));
        int i = 0;
        for (EnumPokeballs pokeball : EnumPokeballs.values()) {
            if (!pokemon.caughtBall.equals(pokeball)) {
                builder.append(Text.builder()
                        .append(Text.of(TextColors.DARK_PURPLE, "[",
                                i == 0 ? TextColors.RED
                                        : (i == 1 ? TextColors.YELLOW : (i == 2 ? TextColors.GREEN : TextColors.BLUE)),
                                pokeball.name(), TextColors.DARK_PURPLE, "]", padding))
                        .onClick(TextActions.executeCallback(
                                src -> getCustomizeConfirmation((Player) src, pokemon, slot, 2, pokeball.name())))
                        .onHover(TextActions.showText(Text.of(TextColors.AQUA, "Sets the pokeball to ", TextColors.BLUE,
                                pokeball.name(), padding, cost(getPokeballCost(pokeball) * mult))))
                        .build());
            } else {
                builder.append(Text.builder()
                        .append(Text.of(TextColors.GRAY, TextStyles.STRIKETHROUGH, "[", pokeball.name(), "]", padding))
                        .onHover(TextActions
                                .showText(Text.of(TextColors.RED, "Pokemon is already in a " + pokeball.name())))
                        .build());
            }
            if (i < 3) {
                i++;
            } else {
                i = 0;
                builder.append(Text.NEW_LINE);
            }
        }
        player.sendMessage(builder.build());
        return;
    }

    public static void getConfirmation(Player player, EntityPixelmon pokemon, int slot, int id, String value,
            int incr) {
        String[] s = PluginInfo.getStatList().get(id).split("_");
        String oldStat = "";
        int cost = 0;
        int totalEvs = 0;
        totalEvs += pokemon.stats.evs.hp;
        totalEvs += pokemon.stats.evs.attack;
        totalEvs += pokemon.stats.evs.defence;
        totalEvs += pokemon.stats.evs.specialAttack;
        totalEvs += pokemon.stats.evs.specialDefence;
        totalEvs += pokemon.stats.evs.speed;

        switch (id) {
        case 0:
            oldStat = "" + pokemon.getLvl().getLevel();
            cost = getLevelCost(pokemon.getLvl().getLevel(), Integer.parseInt(value));
            break;
        case 1:
            oldStat = pokemon.getIsShiny() ? "true" : "false";
            cost = PluginInfo.shinyCost;
            break;
        case 2:
            oldStat = pokemon.getNature().name();
            cost = PluginInfo.natureCost;
            break;
        case 3:
            oldStat = pokemon.getAbility().getName();
            cost = pokemon.baseStats.abilities[2] != null && pokemon.baseStats.abilities[2].equalsIgnoreCase(value)
                    ? PluginInfo.hiddenAbilityCost : PluginInfo.abilityCost;
            break;
        case 4:
            oldStat = pokemon.getGrowth().name();
            cost = getGrowthCost(EnumGrowth.growthFromString(value));
            break;
        case 5:
            oldStat = "" + pokemon.stats.ivs.HP;
            cost = getIvCost(pokemon.stats.ivs.HP, Integer.parseInt(value));
            break;
        case 6:
            oldStat = "" + pokemon.stats.ivs.Attack;
            cost = getIvCost(pokemon.stats.ivs.Attack, Integer.parseInt(value));
            break;
        case 7:
            oldStat = "" + pokemon.stats.ivs.Defence;
            cost = getIvCost(pokemon.stats.ivs.Defence, Integer.parseInt(value));
            break;
        case 8:
            oldStat = "" + pokemon.stats.ivs.SpAtt;
            cost = getIvCost(pokemon.stats.ivs.SpAtt, Integer.parseInt(value));
            break;
        case 9:
            oldStat = "" + pokemon.stats.ivs.SpDef;
            cost = getIvCost(pokemon.stats.ivs.SpDef, Integer.parseInt(value));
            break;
        case 10:
            oldStat = "" + pokemon.stats.ivs.Speed;
            cost = getIvCost(pokemon.stats.ivs.Speed, Integer.parseInt(value));
            break;
        case 11:
            oldStat = "" + pokemon.stats.evs.hp;
            cost = (Math.abs(pokemon.stats.evs.hp - Integer.parseInt(value))) * PluginInfo.evCost;
            break;
        case 12:
            oldStat = "" + pokemon.stats.evs.attack;
            cost = (Math.abs(pokemon.stats.evs.attack - Integer.parseInt(value))) * PluginInfo.evCost;
            break;
        case 13:
            oldStat = "" + pokemon.stats.evs.defence;
            cost = (Math.abs(pokemon.stats.evs.defence - Integer.parseInt(value))) * PluginInfo.evCost;
            break;
        case 14:
            oldStat = "" + pokemon.stats.evs.specialAttack;
            cost = (Math.abs(pokemon.stats.evs.specialAttack - Integer.parseInt(value))) * PluginInfo.evCost;
            break;
        case 15:
            oldStat = "" + pokemon.stats.evs.specialDefence;
            cost = (Math.abs(pokemon.stats.evs.specialDefence - Integer.parseInt(value))) * PluginInfo.evCost;
            break;
        case 16:
            oldStat = "" + pokemon.stats.evs.speed;
            cost = (Math.abs(pokemon.stats.evs.speed - Integer.parseInt(value))) * PluginInfo.evCost;
            break;

        }

        if (EnumPokemon.legendaries.contains(pokemon.getPokemonName())) {
            cost *= 2;
        }
        if (oldStat.equalsIgnoreCase(value)) {
            player.sendMessage(Text.of(header, TextColors.RED, "Use /boost [slot], to open a new boost operation"));
            return;
        }
        if (id >= 11 && id <= 16 && (510 - totalEvs) <= Integer.parseInt(value)) {
            player.sendMessage(Text.of(header, TextColors.RED, "Use /boost [slot], to open a new boost operation"));
            return;
        }
        if (id < 1 || id > 4) {
            if (Integer.parseInt(oldStat) + incr != Integer.parseInt(value)) {
                player.sendMessage(Text.of(header, TextColors.RED, "Use /boost [slot], to open a new boost operation"));
                return;
            }
        }
        if (BattlePointUtils.getBalance(player).intValueExact() < cost) {
            int missing = cost - BattlePointUtils.getBalance(player).intValueExact();
            player.sendMessage(Text.of(header, TextColors.RED, "Insufficent Funds. You need ", missing, padding, bp,
                    TextColors.RED, " to complete this boost."));
            return;
        }
        Builder builder = Text.builder();
        builder.append(header);
        builder.append(Text.of(TextColors.LIGHT_PURPLE, "Are you sure you want to change the "));
        for (String seg : s) {
            builder.append(Text.of(TextColors.DARK_PURPLE, seg.substring(0, 1).toUpperCase() + seg.substring(1) + " "));
        }
        Text finalCost = Text.of(cost, padding, bp);
        builder.append(
                Text.of(TextColors.LIGHT_PURPLE, "from ", TextColors.DARK_PURPLE, oldStat.substring(0, 1).toUpperCase(),
                        oldStat.substring(1) + " ", TextColors.LIGHT_PURPLE, "to ", TextColors.DARK_PURPLE, value,
                        TextColors.LIGHT_PURPLE, " for ", finalCost, TextColors.LIGHT_PURPLE, "?", padding));
        Text confirm = Text.builder().append(Text.of(TextColors.GREEN, "[Confirm]", padding))
                .onClick(TextActions
                        .executeCallback(src -> BoostExecutor.confirm((Player) src, pokemon, slot, id, value, true)))
                .build();
        Text cancel = Text.builder().append(Text.of(TextColors.RED, "[Cancel]"))
                .onClick(TextActions.executeCallback(src -> BoostExecutor.confirm((Player) src, pokemon, slot,
                        Integer.parseInt(PluginInfo.getStatList().get(id)), value, false)))
                .build();
        builder.append(confirm, padding, cancel);
        player.sendMessage(builder.build());
        return;
    }

    public static int getIvCost(int oldIv, int newIv) {
        int change = newIv - oldIv;
        int cost = 0;
        if (change < 0) {
            return Math.round(((Math.abs(change) * PluginInfo.ivCost) / 2) + 0.5F);
        }
        for (int a = oldIv; a < newIv; a++) {
            int oldCost = cost;
            if (a < 20) {
                cost = oldCost + PluginInfo.ivCost;
            } else {
                cost = Math.round(oldCost + (PluginInfo.ivCost * ((a - 20) * 0.5F)) + 0.5F);
            }
        }
        return cost;
    }

    public static void getCustomizeConfirmation(Player player, EntityPixelmon pokemon, int slot, int id, String value) {

        String oldStat = "";
        int cost = 0;
        String customize = "";
        switch (id) {
        case 0:
            oldStat = String.valueOf(pokemon.getIsShiny());
            if (value.equals("true")) {
                cost = PluginInfo.shinyCustomizeCost;
            } else {
                cost = PluginInfo.shinyCustomizeCost / 2;
            }
            customize = "Shiny";
            break;
        case 1:
            oldStat = pokemon.getGrowth().getLocalizedName();
            cost = getGrowthCustomizeCost(EnumGrowth.growthFromString(value));
            customize = "Growth";
            break;
        case 2:
            oldStat = pokemon.caughtBall.name();
            cost = getPokeballCost(getPokeballFromString(value));
            customize = "Pokeball";
            break;
        }
        if (EnumPokemon.legendaries.contains(pokemon.getPokemonName())) {
            cost *= 2;
        }
        if (oldStat.equalsIgnoreCase(value)) {
            player.sendMessage(Text.of(customizeHeader, TextColors.RED,
                    "Use /customize [slot], to open a new customize operation"));
            return;
        }
        if (EventPointUtils.getBalance(player).intValueExact() < cost) {
            int missing = cost - EventPointUtils.getBalance(player).intValueExact();
            player.sendMessage(Text.of(customizeHeader, TextColors.RED, "Insufficent Funds. You need ", missing,
                    padding, ep, TextColors.RED, " to complete this customize."));
            return;
        }
        Builder builder = Text.builder();
        builder.append(customizeHeader);
        builder.append(Text.of(TextColors.LIGHT_PURPLE, "Are you sure you want to change the "));
        builder.append(Text.of(TextColors.DARK_PURPLE, customize, padding));
        Text finalCost = Text.of(cost, padding, ep);
        builder.append(
                Text.of(TextColors.LIGHT_PURPLE, "from ", TextColors.DARK_PURPLE, oldStat.substring(0, 1).toUpperCase(),
                        oldStat.substring(1) + " ", TextColors.LIGHT_PURPLE, "to ", TextColors.DARK_PURPLE, value,
                        TextColors.LIGHT_PURPLE, " for ", finalCost, TextColors.LIGHT_PURPLE, "?", padding));
        Text confirm = Text.builder().append(Text.of(TextColors.GREEN, "[Confirm]", padding)).onClick(TextActions
                .executeCallback(src -> CustomizeExecutor.confirm((Player) src, pokemon, slot, id, value, true)))
                .build();
        Text cancel = Text.builder().append(Text.of(TextColors.RED, "[Cancel]")).onClick(TextActions
                .executeCallback(src -> CustomizeExecutor.confirm((Player) src, pokemon, slot, id, value, false)))
                .build();
        builder.append(confirm, padding, cancel);
        player.sendMessage(builder.build());
        return;
    }

    public static int getLevelCost(int oldLevel, int newLevel) {
        int change = newLevel - oldLevel;
        int cost = 0;
        if (change < 0) {
            cost = (Math.abs(change) * PluginInfo.levelCost);
            return cost;
        }
        for (int a = oldLevel; a < newLevel; a++) {
            if (a < 20) {
                cost += PluginInfo.levelCost;
            } else if (a < 40) {
                cost += PluginInfo.levelCost * 2;
            } else if (a < 60) {
                cost += PluginInfo.levelCost * 3;
            } else if (a < 80) {
                cost += PluginInfo.levelCost * 4;
            } else {
                cost += PluginInfo.levelCost * 5;
            }
        }
        return cost;
    }

    public static int getGrowthCost(EnumGrowth newGrowth) {
        if (EnumGrowth.Small.equals(newGrowth) || EnumGrowth.Ordinary.equals(newGrowth)
                || EnumGrowth.Huge.equals(newGrowth))
            return PluginInfo.growthCost;
        if (EnumGrowth.Runt.equals(newGrowth) || EnumGrowth.Giant.equals(newGrowth))
            return PluginInfo.growthCost * 2;
        if (EnumGrowth.Pygmy.equals(newGrowth) || EnumGrowth.Enormous.equals(newGrowth))
            return PluginInfo.growthCost * 5;
        if (EnumGrowth.Microscopic.equals(newGrowth) || EnumGrowth.Ginormous.equals(newGrowth))
            return PluginInfo.growthCost * 10;
        return 0;
    }

    public static int getGrowthCustomizeCost(EnumGrowth newGrowth) {
        if (EnumGrowth.Small.equals(newGrowth) || EnumGrowth.Ordinary.equals(newGrowth)
                || EnumGrowth.Huge.equals(newGrowth))
            return PluginInfo.growthCustomizeCost;
        if (EnumGrowth.Runt.equals(newGrowth) || EnumGrowth.Giant.equals(newGrowth))
            return PluginInfo.growthCustomizeCost * 2;
        if (EnumGrowth.Pygmy.equals(newGrowth) || EnumGrowth.Enormous.equals(newGrowth))
            return PluginInfo.growthCustomizeCost * 5;
        if (EnumGrowth.Microscopic.equals(newGrowth) || EnumGrowth.Ginormous.equals(newGrowth))
            return PluginInfo.growthCustomizeCost * 10;
        return 0;
    }

    public static int getPokeballCost(EnumPokeballs pokeball) {
        for (String ball : PluginInfo.premiumPokeballs) {
            for (EnumPokeballs poke : EnumPokeballs.values()) {
                if (poke.name().equals(ball) && pokeball.equals(poke)) {
                    return PluginInfo.premiumPokeballCustomizeCost;
                }
            }
        }
        return PluginInfo.pokeballCustomizeCost;
    }

    public static EnumPokeballs getPokeballFromString(String value) {
        for (EnumPokeballs pokeball : EnumPokeballs.values()) {
            if (pokeball.name().equalsIgnoreCase(value)) {
                return pokeball;
            }
        }
        return null;
    }

    public static void exchangePokeDollar(Player player, int epCon) {
        if (Integer.valueOf(EventPointUtils.getBalance(player).intValueExact()) >= epCon) {
            EventPointUtils.changeBalance(player, -epCon);
            int pokeDollars = PluginInfo.pdConversion * epCon;
            Sponge.getCommandManager().process(Sponge.getServer().getConsole(),
                    "eco add " + player.getName() + " " + pokeDollars);
            player.sendMessage(Text.of(customizeHeader, TextColors.GREEN, "Successfully exchanged ", TextColors.WHITE,
                    epCon, padding, ep, TextColors.GREEN, " into ", pokeDollars));
        }
    }

    public static void exchangePokeCrate(Player player, int qty) {
        if (Integer.valueOf(EventPointUtils.getBalance(player).intValueExact()) >= PluginInfo.pcConversion * qty) {
            EventPointUtils.changeBalance(player, -(PluginInfo.pcConversion * qty));
            Sponge.getCommandManager().process(Sponge.getServer().getConsole(),
                    "keysadjust " + player.getName() + " pokecrate " + qty);
            if (qty > 1) {
                player.sendMessage(Text.of(customizeHeader, TextColors.GREEN, "Successfully exchanged ",
                        TextColors.WHITE, (PluginInfo.pcConversion * qty), padding, ep, TextColors.GREEN, " into ",
                        TextColors.WHITE, qty, TextColors.GOLD, " PokeCrate Keys"));
            } else {
                player.sendMessage(Text.of(customizeHeader, TextColors.GREEN, "Successfully exchanged ",
                        TextColors.WHITE, (PluginInfo.pcConversion * qty), padding, ep, TextColors.GREEN, " into ",
                        TextColors.WHITE, qty, TextColors.GOLD, " PokeCrate Keys"));
            }
        }
    }

    public static void exchangeShinyCrate(Player player, int qty) {
        if (Integer.valueOf(EventPointUtils.getBalance(player).intValueExact()) >= PluginInfo.scConversion * qty) {
            EventPointUtils.changeBalance(player, -(PluginInfo.scConversion * qty));
            Sponge.getCommandManager().process(Sponge.getServer().getConsole(),
                    "keysadjust " + player.getName() + " shinycrate " + qty);
            if (qty > 1) {
                player.sendMessage(Text.of(customizeHeader, TextColors.GREEN, "Successfully exchanged ",
                        TextColors.WHITE, (PluginInfo.scConversion * qty), padding, ep, TextColors.GREEN, " into ",
                        TextColors.WHITE, qty, TextColors.GOLD, " ShinyCrate Keys"));
            } else {
                player.sendMessage(Text.of(customizeHeader, TextColors.GREEN, "Successfully exchanged ",
                        TextColors.WHITE, (PluginInfo.scConversion * qty), padding, ep, TextColors.GREEN, " into ",
                        TextColors.WHITE, qty, TextColors.GOLD, " ShinyCrate Key"));
            }
        }
    }

    public static boolean isSamePoke(EntityPixelmon oldPoke, EntityPixelmon newPoke) {
        if (oldPoke.stats.attack != newPoke.stats.attack) {
            System.out.println("Attack is not the same");
            return false;
        }
        if (oldPoke.stats.defence != newPoke.stats.defence) {
            System.out.println("Defense is not the same");
            return false;
        }
        if (oldPoke.stats.hp != newPoke.stats.hp) {
            System.out.println("HP is not the same");
            return false;
        }
        if (oldPoke.stats.specialAttack != newPoke.stats.specialAttack) {
            System.out.println("Special Attack is not the same");
            return false;
        }
        if (oldPoke.stats.specialDefence != newPoke.stats.specialDefence) {
            System.out.println("Special Defense is not the same");
            return false;
        }
        if (oldPoke.stats.speed != newPoke.stats.speed) {
            System.out.println("Speed is not the same");
            return false;
        }
        if (!oldPoke.getSpecies().equals(newPoke.getSpecies())) {
            System.out.println("Species is not the same");
            return false;
        }
        if (!oldPoke.getGrowth().name().equals(newPoke.getGrowth().name())) {
            System.out.println("Growths are not the same");
            return false;
        }
        if (!oldPoke.getAbility().getName().equals(newPoke.getAbility().getName())) {
            System.out.println("Abilities are not the same");
            return false;
        }
        for (int a = 0; a < 6; a++) {
            if (oldPoke.stats.ivs.getArray()[a] != newPoke.stats.ivs.getArray()[a]) {
                System.out.println("Ivs are not the same");
                return false;
            }
        }
        return true;
    }

}
