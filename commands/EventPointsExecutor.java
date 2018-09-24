package com.github.elrol.minerboost.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.github.elrol.minerboost.utils.BoostTextUtils;
import com.github.elrol.minerboost.utils.EventPointUtils;

public class EventPointsExecutor implements CommandExecutor {

    public EventPointsExecutor() {
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (EventPointUtils.getEventPoints() != null) {
            if (args.<User>getOne(Text.of("player")).isPresent()) {
                User user = args.<User>getOne("player").get();
                int bal = EventPointUtils.getBalance(user).intValue();
                src.sendMessage(Text.of(BoostTextUtils.customizeHeader, TextColors.LIGHT_PURPLE, user.getName(),
                        TextColors.GREEN, " has ", bal, BoostTextUtils.padding, BoostTextUtils.ep));
            } else {
                if (src instanceof Player) {
                    Player sender = (Player) src;
                    int bal = EventPointUtils.getBalance(sender).intValue();
                    sender.sendMessage(Text.of(BoostTextUtils.customizeHeader, TextColors.GREEN, "You have ", bal,
                            BoostTextUtils.padding, BoostTextUtils.ep));

                } else {
                    src.sendMessage(Text.of(TextColors.RED, "You must be a player to have money"));
                }
            }
        } else {
            System.err.println("EventPoints not enabled.");
        }
        return CommandResult.success();
    }
}
