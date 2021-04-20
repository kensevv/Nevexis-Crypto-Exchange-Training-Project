package com.nevexis.bank;

import com.nevexis.entities.Traders;
import com.nevexis.entities.Trades;

public interface Bank {
	public boolean transfer(Traders trader, Trades trade);
}