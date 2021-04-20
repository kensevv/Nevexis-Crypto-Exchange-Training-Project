package com.nevexis.dtos;

import java.math.BigDecimal;

import com.nevexis.entities.CurrencyPairs;
import com.nevexis.entities.Traders;
import com.nevexis.entities.Trades;

public class OrderTransactionDTO {
	@SuppressWarnings("unused")
	private Traders trader;
	@SuppressWarnings("unused")
	private BigDecimal exchangeRate;
	@SuppressWarnings("unused")
	private CurrencyPairs currencyPair;
	@SuppressWarnings("unused")
	private BigDecimal amount;

	public OrderTransactionDTO(Traders trader, Trades trade) {
		this.trader = trader;
		this.exchangeRate = trade.getExchangeRate();
		this.currencyPair = trade.getCurrencyPair();
		this.amount = trade.getAmount();
	}
}