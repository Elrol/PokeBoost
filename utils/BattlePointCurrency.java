package com.github.elrol.minerboost.utils;

import java.math.BigDecimal;

import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.text.Text;

import com.github.elrol.minerboost.libs.PluginInfo;

public class BattlePointCurrency implements Currency{

    @Override
	public String getId() {
		return PluginInfo.pluginId + ":battlepoints";
	}

	@Override
	public String getName() {
		return "BattlePoints";
	}

	@Override
	public Text getDisplayName() {
		return PluginInfo.bpSingular;
	}

	@Override
	public Text getPluralDisplayName() {
		return PluginInfo.bpPlural;
	}

	@Override
	public Text getSymbol() {
		return PluginInfo.bpSymbol;
	}

	@Override
	public Text format(BigDecimal amount, int numFractionDigits) {
		int balance = amount.intValue();
		return Text.of(balance, getSymbol());
	}

	@Override
	public int getDefaultFractionDigits() {
		return 0;
	}

	@Override
	public boolean isDefault() {
		return false;
	}

}
