package com.github.elrol.minerboost.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;

import com.github.elrol.minerboost.utils.BattlePointUtils;

public class BattlePointsSetExecutor implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (BattlePointUtils.getBattlePoints() != null) {
            if (args.<Integer>getOne("points").isPresent()) {
                if (args.<User>getOne("player").isPresent() || !(src instanceof Player)) {
                    User player = args.<User>getOne("player").get();
                    BattlePointUtils.setBalance(src, player, args.<Integer>getOne("points").get());
                } else {
                    BattlePointUtils.setBalance(src, args.<Integer>getOne("points").get());
                }
                return CommandResult.success();
            } else {
                return CommandResult.success();
            }
        } else {
            System.err.println("BattlePoints not enabled.");
            return CommandResult.success();
        }
    }
}
