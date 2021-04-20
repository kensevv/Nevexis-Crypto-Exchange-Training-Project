package com.nevexis.bank;

import org.springframework.stereotype.Service;

import com.nevexis.entities.Traders;
import com.nevexis.entities.Trades;

@Service
public class FakeBank implements Bank {
	public boolean transfer(Traders trader, Trades newTrade) {
		return true;
	}
}
