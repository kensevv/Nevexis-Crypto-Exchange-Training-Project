package com.nevexis.market;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nevexis.entities.CurrencyPairs;
import com.nevexis.services.DBService;

@Component
public class Exchanges {

	@Autowired
	private DBService dbService;

	private List<ExchangeCurrency> exchanges = new ArrayList<ExchangeCurrency>();
	private List<CurrencyPairs> dbCurrencyPairs = new ArrayList<CurrencyPairs>();
	
	@PostConstruct
	public void init() {
		dbCurrencyPairs = dbService.getAllCurrencyPairs();
		for (CurrencyPairs pair : dbCurrencyPairs) {
			ExchangeCurrency newExchange = new ExchangeCurrency(dbService, pair);
			newExchange.refreshAll();
			exchanges.add(newExchange);
		}
	}

	public ExchangeCurrency getExchangeByCurrencyPair(CurrencyPairs currency) {
		for (ExchangeCurrency exchange : exchanges) {
			if (exchange.getCurrencyPair().getCryptoCode().equals(currency.getCryptoCode())
					&& exchange.getCurrencyPair().getFiatCode().equals(currency.getFiatCode())) {
				return exchange;
			}
		}
		return null;
	}

	public int getExchangesCount() {
		return exchanges.size();
	}

	public void updateCurrencyPairsExchanges() {
		List<CurrencyPairs> updatedDbCurrencyPairs = dbService.getAllCurrencyPairs();
		updatedDbCurrencyPairs.removeAll(dbCurrencyPairs);
		for(CurrencyPairs pair : updatedDbCurrencyPairs) {
			ExchangeCurrency newExchange = new ExchangeCurrency(dbService, pair);
			newExchange.refreshAll();
			exchanges.add(newExchange);
			dbCurrencyPairs.add(pair);
		}
	}
}