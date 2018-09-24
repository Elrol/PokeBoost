package com.github.elrol.minerboost.utils;

import java.math.BigDecimal;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.github.elrol.minerboost.MinerBoost;
import com.github.elrol.minerboost.libs.PluginInfo;


public class EventPointUtils {

    public static void changeBalance(CommandSource sender, User user, Integer points) {
    	Optional<EconomyService> economyService = Sponge.getServiceManager().provide(EconomyService.class);
        if(!economyService.isPresent()) {
        	sender.sendMessage(Text.of("Economy not found, Please get a plugin that supports the SpongeEconomy API"));
        	return;
        }
    	Optional<UniqueAccount> account = economyService.get().getOrCreateAccount(user.getUniqueId());
        BigDecimal bal = account.get().getBalance(getEventPoints());
        BigDecimal newBal = bal.add(BigDecimal.valueOf(points));
        setBalance(sender, user, newBal.intValueExact());
    }

    public static void changeBalance(CommandSource sender, Integer points) {
        if (sender instanceof Player)
            changeBalance(sender, (Player) sender, points);
        else
            sender.sendMessage(Text.of(TextColors.RED, "You must have a players name"));
    }

    public static void setBalance(CommandSource sender, User user, Integer points) {
    	Optional<EconomyService> economyService = Sponge.getServiceManager().provide(EconomyService.class);
        if(!economyService.isPresent()) {
        	sender.sendMessage(Text.of("Economy not found, Please get a plugin that supports the SpongeEconomy API"));
        	return;
        }
    	Optional<UniqueAccount> account = economyService.get().getOrCreateAccount(user.getUniqueId());
        BigDecimal newBal = BigDecimal.valueOf(points);

        account.get().setBalance(getEventPoints(), newBal, Cause.of(EventContext.empty(), MinerBoost.getInstance()));
        if (sender instanceof Player) {
            sender.sendMessage(Text.of(BoostTextUtils.customizeHeader, TextColors.GREEN, "Set ",
                    TextColors.LIGHT_PURPLE, user.getName(), "'s ", BoostTextUtils.eventPoints, TextColors.GREEN,
                    " to " + newBal + " ", BoostTextUtils.ep));
            if (sender != user) {
                if (user.isOnline())
                    user.getPlayer().get().sendMessage(Text.of(BoostTextUtils.customizeHeader, TextColors.LIGHT_PURPLE,
                            sender.getName(), TextColors.GREEN, " has set your ", BoostTextUtils.eventPoints,
                            TextColors.GREEN, " to ", points, BoostTextUtils.padding, BoostTextUtils.ep));
            }
        } else {
            if (user.isOnline())
            	user.getPlayer().get().sendMessage(Text.of(TextColors.LIGHT_PURPLE, "Console", TextColors.GREEN,
                        " has set your ", BoostTextUtils.eventPoints, TextColors.GREEN, " to ", points,
                        BoostTextUtils.padding, BoostTextUtils.ep));
        }
    }

    public static void setBalance(CommandSource sender, Integer points) {
        if (sender instanceof Player)
            setBalance(sender, (Player) sender, points);
        else
            sender.sendMessage(Text.of(TextColors.RED, "You must have a players name"));
    }

    public static BigDecimal getBalance(User user) {
    	Optional<EconomyService> economyService = Sponge.getServiceManager().provide(EconomyService.class);
        if(!economyService.isPresent()) {
        	return BigDecimal.valueOf(-1);
        }
    	Optional<UniqueAccount> account = economyService.get().getOrCreateAccount(user.getUniqueId());
        BigDecimal bal = account.get().getBalance(getEventPoints());
        return bal;
    }

    public static Currency getEventPoints() {
    	EconomyService economyService = Sponge.getServiceManager().provide(EconomyService.class).get();
    	for (Currency c : economyService.getCurrencies()) {
            if (c.getDisplayName().toPlain().equalsIgnoreCase(PluginInfo.epSingular.toString())) {
                return c;
            }
        }
    	System.out.println("EventPoints not found, making new one.");
        return new EventPointCurrency();
    }

}
