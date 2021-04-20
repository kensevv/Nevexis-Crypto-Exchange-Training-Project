package com.nevexis.comparators;

import java.util.Comparator;

import com.nevexis.entities.Orders;

public class OrdersSellersComparator implements Comparator<Orders> {

	@Override
	public int compare(Orders order1, Orders order2) {
		int exchangeRateCompared = order1.getExchangeRate().compareTo(order2.getExchangeRate());
		if (exchangeRateCompared != 0) {
			return exchangeRateCompared;
		}
		if (order1.getTimestamp().after(order2.getTimestamp())) {
			return 1;
		}
		return -1;
	}
}
