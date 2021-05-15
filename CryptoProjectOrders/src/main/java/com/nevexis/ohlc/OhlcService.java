package com.nevexis.ohlc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nevexis.entities.CurrencyPairs;
import com.nevexis.entities.Trades;
import com.nevexis.services.DBService;

@Service
public class OhlcService {
	@Autowired
	private static DBService dbService;
	
	private List<Trades> allTrades;
	private CurrencyPairs currencyPair;
	private int period;
	
	public String getOhlc(CurrencyPairs currencyPair, int period) {
		//TODO
		this.currencyPair = currencyPair;
		this.period = period;
		this.allTrades = dbService.getAllTradesByCurrencyPair(currencyPair);
		
		List<Ohlc> ohlcResult = new ArrayList<Ohlc>(500);
		
		int periodStartIndex = 0;
		while (periodStartIndex < allTrades.size()) {
			int currentPeriodEndIndex = getNextPeriodEndIndex(periodStartIndex);
			ohlcResult.add(getPeriodOhlc(periodStartIndex, currentPeriodEndIndex));
			
		}
		
		return null; // TODO
	}
	//TODO
	private int getNextPeriodEndIndex(int startIndex){
		return 0;
	}
	private Ohlc getPeriodOhlc(int periodStartIndex, int periodEndIndex) {
		return null;
	}	
}
