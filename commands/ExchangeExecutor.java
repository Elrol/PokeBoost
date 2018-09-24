package com.github.elrol.minerboost.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

import com.github.elrol.minerboost.utils.BoostTextUtils;

public class ExchangeExecutor implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player))
            return CommandResult.success();
        Player player = (Player) src;
        BoostTextUtils.getExchange(player);
        return CommandResult.success();
    }
}
