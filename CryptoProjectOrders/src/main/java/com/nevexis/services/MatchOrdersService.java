package com.nevexis.services;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nevexis.bank.FakeBank;
import com.nevexis.entities.CurrencyPairs;
import com.nevexis.entities.Orders;
import com.nevexis.entities.Trades;
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

	public void matchOrders(CurrencyPairs currencyPair) {
		ExchangeCurrency currentExchange = exchanges.getExchangeByCurrencyPair(currencyPair);
		currentExchange.refreshAll();

		Orders prioritySELLOrder = currentExchange.getSellersQueue().peek();
		Orders priorityBUYOrder = currentExchange.getBuyersQueue().peek();
		// end of recursion conditions
		if (null == priorityBUYOrder || null == prioritySELLOrder) {
			return;
		}
		
		int compareExchanges = prioritySELLOrder.getExchangeRate().compareTo(priorityBUYOrder.getExchangeRate());
		
		if (compareExchanges == 1) { // no match
			return;
		}
		
		// compare timestamps of the matching orders to determine the priority exchange
		// rate that will be used for the transaction
		BigDecimal priorityExchangeRate;
		int compareTimestamps = prioritySELLOrder.getTimestamp().compareTo(priorityBUYOrder.getTimestamp());
		
		if (compareTimestamps == -1) {
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
			executeTransaction(priorityExchangeRate, prioritySELLOrder, executedAmount);
			executeTransaction(priorityExchangeRate, priorityBUYOrder, executedAmount);
			break;
		case 0:
			executedAmount = prioritySELLOrder.getRemainingAmount();
			prioritySELLOrder.setStatus(StatusEnum.CLOSED);
			prioritySELLOrder.setRemainingAmount(new BigDecimal(0));
			priorityBUYOrder.setStatus(StatusEnum.CLOSED);
			priorityBUYOrder.setRemainingAmount(new BigDecimal(0));
			executeTransaction(priorityExchangeRate, prioritySELLOrder, executedAmount);
			executeTransaction(priorityExchangeRate, priorityBUYOrder, executedAmount);
			break;
		case 1:
			executedAmount = priorityBUYOrder.getRemainingAmount();
			prioritySELLOrder.setRemainingAmount(
					prioritySELLOrder.getRemainingAmount().subtract(priorityBUYOrder.getRemainingAmount()));
			prioritySELLOrder.setStatus(StatusEnum.PARTIALLY_EXECUTED);
			priorityBUYOrder.setStatus(StatusEnum.CLOSED);
			priorityBUYOrder.setRemainingAmount(new BigDecimal(0));
			executeTransaction(priorityExchangeRate, priorityBUYOrder, executedAmount);
			executeTransaction(priorityExchangeRate, prioritySELLOrder, executedAmount);
			break;
		}
		matchOrders(currencyPair);
	}

	private void executeTransaction(BigDecimal priorityExchangeRate, Orders order,
			BigDecimal executedAmount) {
		Trades newTrade = new Trades(new Timestamp(System.currentTimeMillis()), order, order.getCurrencyPair(),
				executedAmount, order.getOrderType() , priorityExchangeRate);
		dbService.updateStatusAndAmount(order);
		dbService.addTrade(newTrade);
		bank.transfer(order.getTrader(), newTrade); // ?
	}
}