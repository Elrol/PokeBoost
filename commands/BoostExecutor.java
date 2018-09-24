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
import com.github.elrol.minerboost.utils.BattlePointUtils;
import com.github.elrol.minerboost.utils.BoostTextUtils;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import com.pixelmonmod.pixelmon.storage.PlayerStorage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class BoostExecutor implements CommandExecutor {
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
                player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                        "No pokemon in that slot. Choose a slot that has a pokemon."));
                return CommandResult.success();
            }
            NBTTagCompound nbt = ps.get().partyPokemon[slot - 1];
            EntityPixelmon pokemon = (EntityPixelmon) PixelmonEntityList.createEntityFromNBT(nbt,
                    (World) player.getWorld());
            for (String name : PluginInfo.pokeBoostBlacklist) {
                if (pokemon.getSpecies().name().equalsIgnoreCase(name)) {
                    player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED, name + " can not be Boosted."));
                    return CommandResult.success();
                }
            }
            BoostTextUtils.getPokeStats(player, pokemon, slot);
        } else {
            player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED, "Invalid Slot. Use slot 1-6"));
            return CommandResult.success();
        }
        return CommandResult.success();
    }

    public static void confirm(Player player, EntityPixelmon pokemon, int slot, int id, String value, boolean confirm) {
        Optional<PlayerStorage> ps = PixelmonStorage.pokeBallManager.getPlayerStorage((EntityPlayerMP) player);
        int mult = EnumPokemon.legendaries.contains(pokemon.getPokemonName()) ? 2 : 1;
        if (confirm) {
            if (ps == null || ps.get().partyPokemon[slot - 1] == null) {
                player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                        "No pokemon in that slot. Choose a slot that has a pokemon"));
                return;
            }
            NBTTagCompound nbt = ps.get().partyPokemon[slot - 1];
            EntityPixelmon newPokemon = (EntityPixelmon) PixelmonEntityList.createEntityFromNBT(nbt,
                    (World) player.getWorld());
            if (!BoostTextUtils.isSamePoke(pokemon, newPokemon)) {
                player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                        "The pokemon in that slot has changed. Please start a new operation using /boost [slot]"));
                return;
            }
            if (!pokemon.isInBall) {
                player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                        "Return pokemon to pokeball prior to using the Boost System."));
                return;
            }
            if (BattleRegistry.getBattle((EntityPlayer) player) != null) {
                player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                        "The Boost System can not be used while in battle."));
                return;
            }
            for (String name : PluginInfo.pokeBoostBlacklist) {
                if (pokemon.getSpecies().name().equalsIgnoreCase(name)) {
                    player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED, name + " can not be Boosted."));
                    return;
                }
            }
            int totalEvs = 0;
            totalEvs += pokemon.stats.evs.hp;
            totalEvs += pokemon.stats.evs.attack;
            totalEvs += pokemon.stats.evs.defence;
            totalEvs += pokemon.stats.evs.specialAttack;
            totalEvs += pokemon.stats.evs.specialDefence;
            totalEvs += pokemon.stats.evs.speed;

            int bal = Integer.valueOf(BattlePointUtils.getBalance(player).intValue());
            int cost = 0;
            switch (id) {
            case 0:
                int newLevel = Integer.parseInt(value);
                int oldLevel = pokemon.getLvl().getLevel();
                int levelCost = BoostTextUtils.getLevelCost(oldLevel, newLevel) * mult;
                if (BattlePointUtils.getBalance(player) == null) {
                    System.out.print("Error no value was found");
                }
                if (pokemon.getLvl().getLevel() != newLevel) {
                    if (bal >= levelCost) {
                        cost = levelCost;
                        pokemon.getLvl().setLevel(newLevel);
                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.GREEN, "Successfully set your ",
                                TextColors.LIGHT_PURPLE, pokemon.getName(), "'s ", TextColors.GREEN, "level to ",
                                newLevel));
                    } else {
                        int missing = levelCost - bal;
                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                                "Insufficent Funds. You need ", missing, BoostTextUtils.padding, BoostTextUtils.bp,
                                TextColors.RED, " to complete this boost."));
                        return;
                    }
                } else {
                    player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                            "Use /boost [slot], to open a new boost operation"));
                    return;
                }
                break;
            case 1:
                int shinyCost = PluginInfo.shinyCost * mult;
                if (pokemon.getIsShiny() && value.equalsIgnoreCase("false")
                        || !pokemon.getIsShiny() && value.equalsIgnoreCase("true")) {
                    if (bal >= shinyCost) {
                        cost = shinyCost;
                        String text;
                        if (value.equals("true")) {
                            text = "Shiny";
                            pokemon.setIsShiny(true);
                        } else if (value.equals("false")) {
                            text = "not Shiny";
                            pokemon.setIsShiny(false);
                        } else {
                            player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                                    "Use /boost [slot], to open a new boost operation"));
                            return;
                        }

                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.GREEN, "Successfully set your ",
                                TextColors.LIGHT_PURPLE, pokemon.getName(), TextColors.GREEN, " to ", text));
                    } else {
                        int missing = shinyCost - bal;
                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                                "Insufficent Funds. You need ", missing, BoostTextUtils.padding, BoostTextUtils.bp,
                                TextColors.RED, " to complete this boost."));
                        return;
                    }
                } else {
                    player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                            "Use /boost [slot], to open a new boost operation"));
                    return;
                }
                break;
            case 2:
                int natureCost = PluginInfo.natureCost * mult;
                if (!pokemon.getNature().equals(EnumNature.natureFromString(value))) {
                    if (bal >= natureCost) {
                        cost = natureCost;
                        pokemon.setNature(EnumNature.natureFromString(value));
                        player.sendMessage(Text.of(TextColors.GREEN, "Successfully gave your ", TextColors.LIGHT_PURPLE,
                                pokemon.getName(), TextColors.GREEN, " the ", EnumNature.natureFromString(value).name(),
                                " nature"));
                    } else {
                        int missing = natureCost - bal;
                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                                "Insufficent Funds. You need ", missing, BoostTextUtils.padding, BoostTextUtils.bp,
                                TextColors.RED, " to complete this boost."));
                        return;
                    }
                } else {
                    player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                            "Use /boost [slot], to open a new boost operation"));
                    return;
                }
                break;
            case 3:
                boolean valid = false;
                for (String s : pokemon.baseStats.abilities) {
                    if (s != null && s.equalsIgnoreCase(value)) {
                        valid = true;
                    }
                }
                if (valid) {
                    int abilityCost;
                    if (pokemon.baseStats.abilities[2] != null
                            && pokemon.baseStats.abilities[2].equalsIgnoreCase(value)) {
                        abilityCost = PluginInfo.hiddenAbilityCost * mult;
                    } else {
                        abilityCost = PluginInfo.abilityCost * mult;
                    }
                    if (!pokemon.getAbility().equals(AbilityBase.getAbility(value).get())) {
                        if (bal >= abilityCost) {
                            cost = abilityCost;
                            pokemon.setAbility(value);
                            player.sendMessage(Text.of(TextColors.GREEN, "Successfully gave your ",
                                    TextColors.LIGHT_PURPLE, pokemon.getName(), TextColors.GREEN, " the ",
                                    AbilityBase.getAbility(value).get().getName(), " ability"));
                        } else {
                            int missing = abilityCost - bal;
                            player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                                    "Insufficent Funds. You need ", missing, BoostTextUtils.padding, BoostTextUtils.bp,
                                    TextColors.RED, " to complete this boost."));
                            return;
                        }
                    } else {
                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                                "Use /boost [slot], to open a new boost operation"));
                        return;
                    }
                }
                break;
            case 4:
                if (!pokemon.getGrowth().equals(EnumGrowth.growthFromString(value))) {
                    int growthCost = BoostTextUtils.getGrowthCost(EnumGrowth.growthFromString(value)) * mult;
                    if (bal >= growthCost) {
                        cost = growthCost;
                        pokemon.setGrowth(EnumGrowth.growthFromString(value));
                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.GREEN, "Successfully gave your ",
                                TextColors.LIGHT_PURPLE, pokemon.getName(), TextColors.GREEN, " the ", value,
                                " growth"));
                    } else {
                        int missing = growthCost - bal;
                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                                "Insufficent Funds. You need ", missing, BoostTextUtils.padding, BoostTextUtils.bp,
                                TextColors.RED, " to complete this boost."));
                        return;
                    }
                } else {
                    player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                            "Use /boost [slot], to open a new boost operation"));
                    return;
                }
                break;
            case 5:
                cost = BoostTextUtils.getIvCost(pokemon.stats.ivs.HP, Integer.parseInt(value)) * mult;
                if (pokemon.stats.ivs.HP != Integer.parseInt(value)) {
                    if (bal >= cost) {
                        pokemon.stats.ivs.HP = Integer.parseInt(value);
                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.GREEN, "Successfully set your ",
                                TextColors.LIGHT_PURPLE, pokemon.getName(), TextColors.GREEN, " to ", value,
                                " health IVs"));
                    } else {
                        int missing = cost - bal;
                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                                "Insufficent Funds. You need ", missing, BoostTextUtils.padding, BoostTextUtils.bp,
                                TextColors.RED, " to complete this boost."));
                        return;
                    }
                } else {
                    player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                            "Use /boost [slot], to open a new boost operation"));
                    return;
                }
                break;
            case 6:
                cost = BoostTextUtils.getIvCost(pokemon.stats.ivs.Attack, Integer.parseInt(value)) * mult;
                if (pokemon.stats.ivs.Attack != Integer.parseInt(value)) {
                    if (bal >= cost) {
                        pokemon.stats.ivs.Attack = Integer.parseInt(value);
                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.GREEN, "Successfully set your ",
                                TextColors.LIGHT_PURPLE, pokemon.getName(), TextColors.GREEN, " to ", value,
                                " attack IVs"));
                    } else {
                        int missing = cost - bal;
                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                                "Insufficent Funds. You need ", missing, BoostTextUtils.padding, BoostTextUtils.bp,
                                TextColors.RED, " to complete this boost."));
                        return;
                    }
                } else {
                    player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                            "Use /boost [slot], to open a new boost operation"));
                    return;
                }
                break;
            case 7:
                cost = BoostTextUtils.getIvCost(pokemon.stats.ivs.Defence, Integer.parseInt(value)) * mult;
                if (pokemon.stats.ivs.Defence != Integer.parseInt(value)) {
                    if (bal >= cost) {
                        pokemon.stats.ivs.Defence = Integer.parseInt(value);
                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.GREEN, "Successfully set your ",
                                TextColors.LIGHT_PURPLE, pokemon.getName(), TextColors.GREEN, " to ", value,
                                " defense IVs"));
                    } else {
                        int missing = cost - bal;
                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                                "Insufficent Funds. You need ", missing, BoostTextUtils.padding, BoostTextUtils.bp,
                                TextColors.RED, " to complete this boost."));
                        return;
                    }
                } else {
                    player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                            "Use /boost [slot], to open a new boost operation"));
                    return;
                }
                break;
            case 8:
                cost = BoostTextUtils.getIvCost(pokemon.stats.ivs.SpAtt, Integer.parseInt(value)) * mult;
                if (pokemon.stats.ivs.SpAtt != Integer.parseInt(value)) {
                    if (bal >= cost) {
                        pokemon.stats.ivs.SpAtt = Integer.parseInt(value);
                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.GREEN, "Successfully set your ",
                                TextColors.LIGHT_PURPLE, pokemon.getName(), TextColors.GREEN, " to ", value,
                                " special attack IVs"));
                    } else {
                        int missing = cost - bal;
                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                                "Insufficent Funds. You need ", missing, BoostTextUtils.padding, BoostTextUtils.bp,
                                TextColors.RED, " to complete this boost."));
                        return;
                    }
                } else {
                    player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                            "Use /boost [slot], to open a new boost operation"));
                    return;
                }
                break;
            case 9:
                cost = BoostTextUtils.getIvCost(pokemon.stats.ivs.SpDef, Integer.parseInt(value)) * mult;
                if (pokemon.stats.ivs.SpDef != Integer.parseInt(value)) {
                    if (bal >= cost) {
                        pokemon.stats.ivs.SpDef = Integer.parseInt(value);
                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.GREEN, "Successfully set your ",
                                TextColors.LIGHT_PURPLE, pokemon.getName(), TextColors.GREEN, " to ", value,
                                " special defense IVs"));
                    } else {
                        int missing = cost - bal;
                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                                "Insufficent Funds. You need ", missing, BoostTextUtils.padding, BoostTextUtils.bp,
                                TextColors.RED, " to complete this boost."));
                        return;
                    }
                } else {
                    player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                            "Use /boost [slot], to open a new boost operation"));
                    return;
                }
                break;
            case 10:
                cost = BoostTextUtils.getIvCost(pokemon.stats.ivs.Speed, Integer.parseInt(value)) * mult;
                if (pokemon.stats.ivs.Speed != Integer.parseInt(value) || (510 - totalEvs) < Integer.parseInt(value)) {
                    if (bal >= cost) {
                        pokemon.stats.ivs.Speed = Integer.parseInt(value);
                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.GREEN, "Successfully set your ",
                                TextColors.LIGHT_PURPLE, pokemon.getName(), TextColors.GREEN, " to ", value,
                                " speed IVs"));
                    } else {
                        int missing = cost - bal;
                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                                "Insufficent Funds. You need ", missing, BoostTextUtils.padding, BoostTextUtils.bp,
                                TextColors.RED, " to complete this boost."));
                        return;
                    }
                } else {
                    player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                            "Use /boost [slot], to open a new boost operation"));
                    return;
                }
                break;
            case 11:
                cost = (Math.abs(pokemon.stats.evs.hp - Integer.parseInt(value))) * PluginInfo.evCost * mult;
                if (pokemon.stats.evs.hp != Integer.parseInt(value) && (510 - totalEvs) >= Integer.parseInt(value)) {
                    if (bal >= cost) {
                        pokemon.stats.evs.hp = Integer.parseInt(value);
                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.GREEN, "Successfully set your ",
                                TextColors.LIGHT_PURPLE, pokemon.getName(), TextColors.GREEN, " to ", value,
                                " health EVs"));
                    } else {
                        int missing = cost - bal;
                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                                "Insufficent Funds. You need ", missing, BoostTextUtils.padding, BoostTextUtils.bp,
                                TextColors.RED, " to complete this boost."));
                        return;
                    }
                } else {
                    player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                            "Use /boost [slot], to open a new boost operation"));
                    return;
                }
                break;
            case 12:
                cost = (Math.abs(pokemon.stats.evs.attack - Integer.parseInt(value))) * PluginInfo.evCost * mult;
                if (pokemon.stats.evs.attack != Integer.parseInt(value)
                        && (510 - totalEvs) >= Integer.parseInt(value)) {
                    if (bal >= cost) {
                        pokemon.stats.evs.attack = Integer.parseInt(value);
                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.GREEN, "Successfully set your ",
                                TextColors.LIGHT_PURPLE, pokemon.getName(), TextColors.GREEN, " to ", value,
                                " attack EVs"));
                    } else {
                        int missing = cost - bal;
                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                                "Insufficent Funds. You need ", missing, BoostTextUtils.padding, BoostTextUtils.bp,
                                TextColors.RED, " to complete this boost."));
                        return;
                    }
                } else {
                    player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                            "Use /boost [slot], to open a new boost operation"));
                    return;
                }
                break;
            case 13:
                cost = (Math.abs(pokemon.stats.evs.defence - Integer.parseInt(value))) * PluginInfo.evCost * mult;
                if (pokemon.stats.evs.defence != Integer.parseInt(value)
                        && (510 - totalEvs) >= Integer.parseInt(value)) {
                    if (bal >= cost) {
                        pokemon.stats.evs.defence = Integer.parseInt(value);
                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.GREEN, "Successfully set your ",
                                TextColors.LIGHT_PURPLE, pokemon.getName(), TextColors.GREEN, " to ", value,
                                " defense EVs"));
                    } else {
                        int missing = cost - bal;
                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                                "Insufficent Funds. You need ", missing, BoostTextUtils.padding, BoostTextUtils.bp,
                                TextColors.RED, " to complete this boost."));
                        return;
                    }
                } else {
                    player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                            "Use /boost [slot], to open a new boost operation"));
                    return;
                }
                break;
            case 14:
                cost = (Math.abs(pokemon.stats.evs.specialAttack - Integer.parseInt(value))) * PluginInfo.evCost * mult;
                if (pokemon.stats.evs.specialAttack != Integer.parseInt(value)
                        && (510 - totalEvs) >= Integer.parseInt(value)) {
                    if (bal >= cost) {
                        pokemon.stats.evs.specialAttack = Integer.parseInt(value);
                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.GREEN, "Successfully set your ",
                                TextColors.LIGHT_PURPLE, pokemon.getName(), TextColors.GREEN, " to ", value,
                                " special attack EVs"));
                    } else {
                        int missing = cost - bal;
                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                                "Insufficent Funds. You need ", missing, BoostTextUtils.padding, BoostTextUtils.bp,
                                TextColors.RED, " to complete this boost."));
                        return;
                    }
                } else

                {
                    player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                            "Use /boost [slot], to open a new boost operation"));
                    return;
                }
                break;
            case 15:
                cost = (Math.abs(pokemon.stats.evs.specialDefence - Integer.parseInt(value))) * PluginInfo.evCost
                        * mult;
                if (pokemon.stats.evs.specialDefence != Integer.parseInt(value)
                        && (510 - totalEvs) >= Integer.parseInt(value)) {
                    if (bal >= cost) {
                        pokemon.stats.evs.specialDefence = Integer.parseInt(value);
                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.GREEN, "Successfully set your ",
                                TextColors.LIGHT_PURPLE, pokemon.getName(), TextColors.GREEN, " to ", value,
                                " special defense EVs"));
                    } else {
                        int missing = cost - bal;
                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                                "Insufficent Funds. You need ", missing, BoostTextUtils.padding, BoostTextUtils.bp,
                                TextColors.RED, " to complete this boost."));
                        return;
                    }
                } else {
                    player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                            "Use /boost [slot], to open a new boost operation"));
                    return;
                }
                break;
            case 16:
                cost = (Math.abs(pokemon.stats.evs.speed - Integer.parseInt(value))) * PluginInfo.evCost * mult;
                if (pokemon.stats.evs.speed != Integer.parseInt(value) && (510 - totalEvs) >= Integer.parseInt(value)) {
                    if (bal >= cost) {
                        pokemon.stats.evs.speed = Integer.parseInt(value);
                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.GREEN, "Successfully set your ",
                                TextColors.LIGHT_PURPLE, pokemon.getName(), TextColors.GREEN, " to ", value,
                                " speed EVs"));
                    } else {
                        int missing = cost - bal;
                        player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                                "Insufficent Funds. You need ", missing, BoostTextUtils.padding, BoostTextUtils.bp,
                                TextColors.RED, " to complete this boost."));
                        return;
                    }
                } else {
                    player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED,
                            "Use /boost [slot], to open a new boost operation"));
                    return;
                }
                break;
            }
            BattlePointUtils.setBalance(player, bal - cost);
            pokemon.update(EnumUpdateType.values());
        } else {
            player.sendMessage(Text.of(BoostTextUtils.header, TextColors.RED, "Boost Canceled."));
        }
    }
}
