package com.github.elrol.minerboost.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.User;

import com.github.elrol.minerboost.utils.EventPointUtils;

public class EventPointsSetExecutor implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (EventPointUtils.getEventPoints() != null) {
            if (args.<Integer>getOne("points").isPresent()) {
                if (args.<User>getOne("player").isPresent()) {
                    User user = args.<User>getOne("player").get();
                    EventPointUtils.setBalance(src, user, args.<Integer>getOne("points").get());
                } else {
                    EventPointUtils.setBalance(src, args.<Integer>getOne("points").get());
                }
            }
            return CommandResult.success();

        } else {
            System.err.println("EventPoints not enabled.");
            return CommandResult.success();
        }
    }
}
