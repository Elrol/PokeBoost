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

import com.github.elrol.minerboost.utils.BattlePointUtils;
import com.github.elrol.minerboost.utils.BoostTextUtils;

public class BattlePointsExecutor implements CommandExecutor {

    public BattlePointsExecutor() {
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (BattlePointUtils.getBattlePoints() != null) {
            if (args.<User>getOne(Text.of("player")).isPresent()) {
                User player = args.<User>getOne("player").get();
                int bal = BattlePointUtils.getBalance(player).intValue();
                src.sendMessage(Text.of(BoostTextUtils.header, TextColors.LIGHT_PURPLE, player.getName(),
                        TextColors.GREEN, " has ", bal, BoostTextUtils.padding, BoostTextUtils.bp));
            } else {
                if (src instanceof Player) {
                    Player sender = (Player) src;
                    int bal = BattlePointUtils.getBalance(sender).intValue();
                    sender.sendMessage(Text.of(BoostTextUtils.header, TextColors.GREEN, "You have ", bal,
                            BoostTextUtils.padding, BoostTextUtils.bp));

                } else {
                    src.sendMessage(Text.of(TextColors.RED, "You must be a player to have money"));
                }
            }
        } else {
            System.err.println("BattlePoints not enabled.");
        }
        return CommandResult.success();
    }
}
