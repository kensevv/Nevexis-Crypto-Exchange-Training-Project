package com.nevexis.configs;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.nevexis.market.Exchanges;
import com.nevexis.services.DBService;

@Configuration
@EnableScheduling
public class CurrencyPairsConfig {

	@Autowired
	private DBService dbService;
	@Autowired
	private Exchanges exchanges;
	
	@PostConstruct
	@Scheduled(fixedDelay = 10000)
	public void checkAndUpdateExchangesForCurrencyPairs() {
		if(exchanges.getExchangesCount() != dbService.getCurrencyPairsCount()) {
			exchanges.updateCurrencyPairsExchanges();
		}
	}
}
