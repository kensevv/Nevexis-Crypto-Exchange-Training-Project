package com.nevexis.market;

import java.util.PriorityQueue;

import com.nevexis.comparators.OrdersBuyersComparator;
import com.nevexis.comparators.OrdersSellersComparator;
import com.nevexis.entities.CurrencyPairs;
import com.nevexis.entities.Orders;
import com.nevexis.enums.OrderType;
import com.nevexis.services.DBService;

public class ExchangeCurrency {
	private DBService dbService;

	private CurrencyPairs currencyPair;

	private PriorityQueue<Orders> sellersQueue = new PriorityQueue<Orders>(10, new OrdersSellersComparator());
	private PriorityQueue<Orders> buyersQueue = new PriorityQueue<Orders>(10, new OrdersBuyersComparator());

	public ExchangeCurrency(DBService dbService, CurrencyPairs currencyPair) {
		this.dbService = dbService;
		this.currencyPair = currencyPair;
		
		refreshAll();
	}

	synchronized public void refreshSellers() {
		sellersQueue.clear();
		sellersQueue.addAll(dbService.getAllOpenOrdersByCurrencyPairAndOrderType(currencyPair, OrderType.SELL));
	}

	synchronized public void refreshBuyers() {
		buyersQueue.clear();
		buyersQueue.addAll(dbService.getAllOpenOrdersByCurrencyPairAndOrderType(currencyPair, OrderType.BUY));
	}

	public void refreshAll() {
		refreshSellers();
		refreshBuyers();
	}

	public CurrencyPairs getCurrencyPair() {
		return currencyPair;
	}

	public PriorityQueue<Orders> getSellersQueue() {
		return sellersQueue;
	}

	public PriorityQueue<Orders> getBuyersQueue() {
		return buyersQueue;
	}
}