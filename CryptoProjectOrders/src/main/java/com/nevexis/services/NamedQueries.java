package com.nevexis.services;

public class NamedQueries {
	//orders
	public static final String getAllOrdersByStatus = "Orders.getAllOrdersByStatus";
	public static final String getAllOpenOrdersByCurrencyPairAndOrderType = "Orders.getAllOpenOrdersByCurrencyPairAndOrderType";
	public static final String getAllOrdersByTrader = "Orders.getAllOrdersByTrader";
	public static final String getAllOrdersByType = "Orders.getAllOrdersByType";
	public static final String getAllOrdersByCrypto = "Orders.getAllOrdersByCrypto";
	//currencyPairs
	public static final String getAllCurrencyPairs = "CurrencyPairs.getAllCurrencyPairs";
	//Trades
	public static final String getAllTradesByCurrencyPair = "Trades.getAllTradesByCurrencyPair";
	public static final String getAllTradesByTrader = "Trades.getAllTradesByTrader";
}
