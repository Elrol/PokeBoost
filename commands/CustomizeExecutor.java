package com.github.elrol.minerboost.commands;

import java.util.Optional;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.github.elrol.minerboost.libs.PluginInfo;
import com.github.elrol.minerboost.utils.BoostTextUtils;
import com.github.elrol.minerboost.utils.EventPointUtils;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import com.pixelmonmod.pixelmon.storage.PlayerStorage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class CustomizeExecutor implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player))
            return CommandResult.success();
        Player player = (Player) src;
        int slot = 0;
        Optional<PlayerStorage> ps = PixelmonStorage.pokeBallManager.getPlayerStorage((EntityPlayerMP) player);
        if (args.<Integer>getOne("slot").get() >= 1 && args.<Integer>getOne("slot").get() <= 6) {
            slot = args.<Integer>getOne("slot").get();
            if (ps == null || ps.get().partyPokemon[slot - 1] == null) {
                player.sendMessage(Text.of(BoostTextUtils.customizeHeader, TextColors.RED,
                        "No pokemon in that slot. Choose a slot that has a pokemon."));
                return CommandResult.success();
            }
            NBTTagCompound nbt = ps.get().partyPokemon[slot - 1];
            EntityPixelmon pokemon = (EntityPixelmon) PixelmonEntityList.createEntityFromNBT(nbt,
                    (World) player.getWorld());
            for (String name : PluginInfo.pokeCustomizeBlacklist) {
                if (pokemon.getSpecies().name().equals(name)) {
                    player.sendMessage(
                            Text.of(BoostTextUtils.customizeHeader, TextColors.RED, name + " can not be Boosted."));
                    return CommandResult.success();
                }
            }
            BoostTextUtils.getCustomize(player, pokemon, slot);
        } else {
            player.sendMessage(Text.of(BoostTextUtils.customizeHeader, TextColors.RED, "Invalid Slot. Use slot 1-6"));
            return CommandResult.success();
        }
        return CommandResult.success();
    }

    public static void confirm(Player player, EntityPixelmon pokemon, int slot, int id, String value, boolean confirm) {
        Optional<PlayerStorage> ps = PixelmonStorage.pokeBallManager.getPlayerStorage((EntityPlayerMP) player);
        int mult = EnumPokemon.legendaries.contains(pokemon.getPokemonName()) ? 2 : 1;
        if (confirm) {
            if (ps == null || ps.get().partyPokemon[slot - 1] == null) {
                player.sendMessage(Text.of(BoostTextUtils.customizeHeader, TextColors.RED,
                        "No pokemon in that slot. Choose a slot that has a pokemon"));
                return;
            }
            NBTTagCompound nbt = ps.get().partyPokemon[slot - 1];
            EntityPixelmon newPokemon = (EntityPixelmon) PixelmonEntityList.createEntityFromNBT(nbt,
                    (World) player.getWorld());
            if (!BoostTextUtils.isSamePoke(pokemon, newPokemon)) {
                player.sendMessage(Text.of(BoostTextUtils.customizeHeader, TextColors.RED,
                        "The pokemon in that slot has changed. Please start a new operation using /customizes [slot]"));
                return;
            }
            if (!newPokemon.isInBall) {
                player.sendMessage(Text.of(BoostTextUtils.customizeHeader, TextColors.RED,
                        "Return pokemon to pokeball prior to using the Customize System."));
                return;
            }
            if (BattleRegistry.getBattle((EntityPlayer) player) != null) {
                player.sendMessage(Text.of(BoostTextUtils.customizeHeader, TextColors.RED,
                        "The Customize System can not be used while in battle."));
                return;
            }
            for (String name : PluginInfo.pokeCustomizeBlacklist) {
                if (pokemon.getSpecies().name().equals(name)) {
                    player.sendMessage(
                            Text.of(BoostTextUtils.customizeHeader, TextColors.RED, name + " can not be Boosted."));
                    return;
                }
            }
            int bal = Integer.valueOf(EventPointUtils.getBalance(player).intValue());
            int cost = 0;
            switch (id) {
            case 0:
                int shinyCost = PluginInfo.shinyCustomizeCost * mult;
                if (value.equalsIgnoreCase("false"))
                    shinyCost /= 2;
                if (newPokemon.getIsShiny() && value.equalsIgnoreCase("false")
                        || !newPokemon.getIsShiny() && value.equalsIgnoreCase("true")) {
                    if (bal >= shinyCost) {
                        cost = shinyCost;
                        String text;
                        if (value.equals("true")) {
                            text = "Shiny";
                            newPokemon.setIsShiny(true);
                        } else if (value.equals("false")) {
                            text = "not Shiny";
                            newPokemon.setIsShiny(false);
                        } else {
                            player.sendMessage(Text.of(BoostTextUtils.customizeHeader, TextColors.RED,
                                    "Use /customize [slot], to open a new customize operation"));
                            return;
                        }

                        player.sendMessage(
                                Text.of(BoostTextUtils.customizeHeader, TextColors.GREEN, "Successfully set your ",
                                        TextColors.LIGHT_PURPLE, newPokemon.getName(), TextColors.GREEN, " to ", text));
                    } else {
                        int missing = shinyCost - bal;
                        player.sendMessage(Text.of(BoostTextUtils.customizeHeader, TextColors.RED,
                                "Insufficent Funds. You need ", missing, BoostTextUtils.padding, BoostTextUtils.ep,
                                TextColors.RED, " to complete this customize."));
                        return;
                    }
                } else {
                    player.sendMessage(Text.of(BoostTextUtils.customizeHeader, TextColors.RED,
                            "Use /customize [slot], to open a new customize operation"));
                    return;
                }
                break;
            case 1:
                if (!newPokemon.getGrowth().equals(EnumGrowth.growthFromString(value))) {
                    int growthCost = BoostTextUtils.getGrowthCustomizeCost(EnumGrowth.growthFromString(value)) * mult;
                    if (bal >= growthCost) {
                        cost = growthCost;
                        newPokemon.setGrowth(EnumGrowth.growthFromString(value));
                        player.sendMessage(Text.of(BoostTextUtils.customizeHeader, TextColors.GREEN,
                                "Successfully gave your ", TextColors.LIGHT_PURPLE, newPokemon.getName(),
                                TextColors.GREEN, " the ", value, " growth"));
                    } else {
                        int missing = growthCost - bal;
                        player.sendMessage(Text.of(BoostTextUtils.customizeHeader, TextColors.RED,
                                "Insufficent Funds. You need ", missing, BoostTextUtils.padding, BoostTextUtils.ep,
                                TextColors.RED, " to complete this customize."));
                        return;
                    }
                } else {
                    player.sendMessage(Text.of(BoostTextUtils.customizeHeader, TextColors.RED,
                            "Use /customize [slot], to open a new customize operation"));
                    return;
                }
                break;
            case 2:
                if (BoostTextUtils.getPokeballFromString(value) != null
                        && !newPokemon.caughtBall.equals(BoostTextUtils.getPokeballFromString(value))) {
                    int ballCost = BoostTextUtils.getPokeballCost(BoostTextUtils.getPokeballFromString(value)) * mult;
                    if (bal >= ballCost) {
                        cost = ballCost;
                        newPokemon.caughtBall = BoostTextUtils.getPokeballFromString(value);
                        player.sendMessage(Text.of(BoostTextUtils.customizeHeader, TextColors.GREEN,
                                "Successfully put your ", TextColors.LIGHT_PURPLE, newPokemon.getName(),
                                TextColors.GREEN, " into a ", value));
                    } else {
                        int missing = ballCost - bal;
                        player.sendMessage(Text.of(BoostTextUtils.customizeHeader, TextColors.RED,
                                "Insufficent Funds. You need ", missing, BoostTextUtils.padding, BoostTextUtils.ep,
                                TextColors.RED, " to complete this customize."));
                        return;
                    }
                } else {
                    player.sendMessage(Text.of(BoostTextUtils.customizeHeader, TextColors.RED,
                            "Use /customize [slot], to open a new customize operation"));
                    return;
                }
                break;
            }
            EventPointUtils.setBalance(player, bal - cost);
            newPokemon.update(EnumUpdateType.values());
        } else {
            player.sendMessage(Text.of(BoostTextUtils.customizeHeader, TextColors.RED, "Customize Canceled."));
        }
    }
}
