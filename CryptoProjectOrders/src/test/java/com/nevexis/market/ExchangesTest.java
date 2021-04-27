package com.nevexis.market;

import static org.junit.Assert.assertTrue;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.nevexis.entities.CurrencyPairs;
import com.nevexis.services.BasicService;

@SpringBootTest
@ActiveProfiles("test")
@Sql("../insert_currency_traders_data.sql")
@Sql("../insert_test_orders.sql")
@TestInstance(Lifecycle.PER_CLASS)
@Transactional
class ExchangesTest extends BasicService {
	@Autowired
	private Exchanges exchanges;

	@BeforeEach
	public void refreshMarkets() {
		exchanges.init();
	}

	@Test
	public void testIfExchangesAreLoaded() {
		assertTrue(exchanges != null);
		assertTrue(exchanges.getExchangesCount() == 6);
	}

	@Test
	public void testIfGetExchangeByCurrencyPairsWorks() {
		ExchangeCurrency exchange = exchanges.getExchangeByCurrencyPair(new CurrencyPairs("BTC", "USD"));

		assertTrue(exchange != null);
		assertTrue(exchange.getBuyersQueue() != null && exchange.getBuyersQueue().size() == 2);
		assertTrue(exchange.getSellersQueue() != null && exchange.getSellersQueue().size() == 2);
	}
	
	@Test
	public void testIfUpdateCurrencyPairsExchangesUpdatesWhenCurrencyPairIsAdded() {
		em.persist(new CurrencyPairs("TestCrypto", "TestFiat"));
		exchanges.updateCurrencyPairsExchanges();
		
		assertTrue(exchanges != null);
		assertTrue(exchanges.getExchangesCount() == 7);
	}
}
