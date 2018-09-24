package com.github.elrol.minerboost.utils;

import java.math.BigDecimal;

import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.text.Text;

import com.github.elrol.minerboost.libs.PluginInfo;

public class EventPointCurrency implements Currency{

	@Override
	public String getId() {
		return PluginInfo.pluginId + ":eventpoints";
	}

	@Override
	public String getName() {
		return "EventPoints";
	}

	@Override
	public Text getDisplayName() {
		return PluginInfo.epSingular;
	}

	@Override
	public Text getPluralDisplayName() {
		return PluginInfo.epPlural;
	}

	@Override
	public Text getSymbol() {
		return PluginInfo.epSymbol;
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
