package com.github.elrol.minerboost.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;

import com.github.elrol.minerboost.utils.BattlePointUtils;

public class BattlePointsModifyExecutor implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (BattlePointUtils.getBattlePoints() != null) {
            if (args.<User>getOne("player").isPresent() || !(src instanceof Player)) {
                BattlePointUtils.changeBalance(src, args.<User>getOne("player").get(),
                        args.<Integer>getOne("points").get());
            } else {
                BattlePointUtils.changeBalance(src, args.<Integer>getOne("points").get());
            }
            return CommandResult.success();
        } else {
            System.err.println("BattlePoints not enabled.");
            return CommandResult.success();
        }
    }
}
