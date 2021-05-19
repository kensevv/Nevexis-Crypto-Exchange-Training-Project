package com.nevexis.services;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nevexis.bank.FakeBank;
import com.nevexis.entities.Orders;
import com.nevexis.entities.Trades;
import com.nevexis.enums.OrderExecuteType;
import com.nevexis.enums.OrderType;
import com.nevexis.enums.StatusEnum;
import com.nevexis.market.ExchangeCurrency;
import com.nevexis.market.Exchanges;

@Service
public class MatchOrdersService {
	@Autowired
	private Exchanges exchanges;
	@Autowired
	private FakeBank bank;
	@Autowired
	private DBService dbService;

	public void matchOrders(Orders newOrder) {
		ExchangeCurrency currentExchange = exchanges.getExchangeByCurrencyPair(newOrder.getCurrencyPair());
		currentExchange.refreshAll();

		Orders prioritySELLOrder = currentExchange.getSellersQueue().peek();
		Orders priorityBUYOrder = currentExchange.getBuyersQueue().peek();

		// recursion end conditions
		if (null == priorityBUYOrder || null == prioritySELLOrder) {
			return;
		}

		if (newOrder.getExecuteType().equals(OrderExecuteType.MARKET) 
				&& !newOrder.getStatus().equals(StatusEnum.CLOSED)) {
			adjustMarketOrder(newOrder, prioritySELLOrder, priorityBUYOrder);
		}
		
		int compareExchanges = prioritySELLOrder.getExchangeRate().compareTo(priorityBUYOrder.getExchangeRate());
		if (compareExchanges == 1) { // no match
			return;
		}

		matchPriorityOrders(prioritySELLOrder, priorityBUYOrder);
		matchOrders(newOrder);
	}

	private void matchPriorityOrders(Orders prioritySELLOrder, Orders priorityBUYOrder) {
		// compare timestamps of the matching orders to determine the priority exchange
		// rate that will be used for the transaction
		BigDecimal priorityExchangeRate;
		if (prioritySELLOrder.getTimestamp().compareTo(priorityBUYOrder.getTimestamp()) == -1) {
			priorityExchangeRate = prioritySELLOrder.getExchangeRate();
		} else {
			priorityExchangeRate = priorityBUYOrder.getExchangeRate();
		}

		int compareRemainingAmounts = prioritySELLOrder.getRemainingAmount()
				.compareTo(priorityBUYOrder.getRemainingAmount());

		BigDecimal executedAmount;
		switch (compareRemainingAmounts) {
		case -1:
			executedAmount = prioritySELLOrder.getRemainingAmount();
			priorityBUYOrder.setRemainingAmount(
					priorityBUYOrder.getRemainingAmount().subtract(prioritySELLOrder.getRemainingAmount()));
			priorityBUYOrder.setStatus(StatusEnum.PARTIALLY_EXECUTED);
			prioritySELLOrder.setStatus(StatusEnum.CLOSED);
			prioritySELLOrder.setRemainingAmount(new BigDecimal(0));
			executeTransaction(priorityExchangeRate, prioritySELLOrder, priorityBUYOrder, executedAmount);
			break;
		case 0:
			executedAmount = prioritySELLOrder.getRemainingAmount();
			prioritySELLOrder.setStatus(StatusEnum.CLOSED);
			prioritySELLOrder.setRemainingAmount(new BigDecimal(0));
			priorityBUYOrder.setStatus(StatusEnum.CLOSED);
			priorityBUYOrder.setRemainingAmount(new BigDecimal(0));
			executeTransaction(priorityExchangeRate, prioritySELLOrder, priorityBUYOrder, executedAmount);
			break;
		case 1:
			executedAmount = priorityBUYOrder.getRemainingAmount();
			prioritySELLOrder.setRemainingAmount(
					prioritySELLOrder.getRemainingAmount().subtract(priorityBUYOrder.getRemainingAmount()));
			prioritySELLOrder.setStatus(StatusEnum.PARTIALLY_EXECUTED);
			priorityBUYOrder.setStatus(StatusEnum.CLOSED);
			priorityBUYOrder.setRemainingAmount(new BigDecimal(0));
			executeTransaction(priorityExchangeRate, prioritySELLOrder, priorityBUYOrder, executedAmount);
			break;
		}
	}

	private void executeTransaction(BigDecimal priorityExchangeRate, Orders prioritySELLOrder, Orders priorityBUYOrder, BigDecimal executedAmount) {
		Trades sellTrade = new Trades(new Timestamp(System.currentTimeMillis()), prioritySELLOrder, prioritySELLOrder.getCurrencyPair(),
				executedAmount, prioritySELLOrder.getOrderType(), priorityExchangeRate);
		Trades buyTrade = new Trades(new Timestamp(System.currentTimeMillis()), priorityBUYOrder, priorityBUYOrder.getCurrencyPair(),
				executedAmount, priorityBUYOrder.getOrderType(), priorityExchangeRate);
		
		dbService.addTrades(sellTrade, buyTrade);
		// ?
		bank.transfer(prioritySELLOrder.getTrader(), sellTrade);
		bank.transfer(priorityBUYOrder.getTrader(), buyTrade);
	}

	private void adjustMarketOrder(Orders marketOrder, Orders prioritySELLOrder, Orders priorityBUYOrder) {
		if (marketOrder.getOrderType().equals(OrderType.BUY)) {
			priorityBUYOrder = marketOrder;
			priorityBUYOrder.setExchangeRate(prioritySELLOrder.getExchangeRate());
			
		} else if (marketOrder.getOrderType().equals(OrderType.SELL)) {
			prioritySELLOrder = marketOrder;
			prioritySELLOrder.setExchangeRate(priorityBUYOrder.getExchangeRate());
		}
	}
}