package com.nevexis.services;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.nevexis.entities.CurrencyPairs;

@SpringBootTest
@ActiveProfiles("test")
@Sql("../insert_currency_traders_data.sql")
@Transactional
public class CurrencyPairsTests extends BasicService {

	@Autowired
	DBService service;

	@Test
	public void getAllCurrencyPairsTest() {
		List<CurrencyPairs> allCurrencyPairs = service.getAllCurrencyPairs();

		assertTrue(allCurrencyPairs.size() == 6);

		assertTrue(allCurrencyPairs.contains(new CurrencyPairs("BTC", "USD")));
		assertTrue(allCurrencyPairs.contains(new CurrencyPairs("ADA", "USD")));
		assertTrue(allCurrencyPairs.contains(new CurrencyPairs("DOGE", "USD")));
		assertTrue(allCurrencyPairs.contains(new CurrencyPairs("LTC", "USD")));
		assertTrue(allCurrencyPairs.contains(new CurrencyPairs("XRP", "USD")));
		assertTrue(allCurrencyPairs.contains(new CurrencyPairs("ETH", "USD")));
	}

	@Test
	public void getCurrencyPairsCountTest() {
		Long pairsCount = service.getCurrencyPairsCount();
		assertEquals(pairsCount, 6);
	}

	@Test
	public void findCurrencyPairTest() {
		CurrencyPairs currencyPair = new CurrencyPairs("BTC", "USD");
		assertEquals( service.findCurrencyPair(currencyPair), currencyPair);

		currencyPair = new CurrencyPairs("ADA", "USD");
		assertEquals(service.findCurrencyPair(currencyPair), currencyPair);

		CurrencyPairs newPair = new CurrencyPairs("TestCrypto", "TestFiat");
		em.persist(newPair);
		currencyPair = service.findCurrencyPair(newPair);
		assertEquals(service.findCurrencyPair(currencyPair), currencyPair);

		em.remove(newPair);
		currencyPair = service.findCurrencyPair(newPair);
		assertEquals(null, currencyPair);
	}
}
