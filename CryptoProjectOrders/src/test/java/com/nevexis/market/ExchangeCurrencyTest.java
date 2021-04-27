package com.nevexis.market;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.nevexis.entities.CurrencyPairs;
import com.nevexis.entities.Orders;
import com.nevexis.enums.OrderType;
import com.nevexis.enums.StatusEnum;
import com.nevexis.services.BasicService;
import com.nevexis.services.DBService;

@SpringBootTest
@ActiveProfiles("test")
@Sql("../insert_currency_traders_data.sql")
@Sql("../insert_test_orders.sql")
@Transactional
class ExchangeCurrencyTest extends BasicService {

	@Autowired
	private DBService dbService;

	@Test
	public void testIfBuyersPriorityQueueLoadsProperlyAndSorted() {
		ExchangeCurrency btcExchange = new ExchangeCurrency(dbService, new CurrencyPairs("BTC", "USD"));

		assertTrue(btcExchange != null);
		assertTrue(btcExchange.getBuyersQueue() != null);
		assertTrue(btcExchange.getBuyersQueue().size() == 2);
		assertTrue(btcExchange.getBuyersQueue().poll().getId().equals(1L));
		assertTrue(btcExchange.getBuyersQueue().poll().getId().equals(2L));
	}

	@Test
	public void testIfSellersPriorityQueueLoadsProperlyAndSorted() {
		ExchangeCurrency btcExchange = new ExchangeCurrency(dbService, new CurrencyPairs("BTC", "USD"));

		assertTrue(btcExchange != null);
		assertTrue(btcExchange.getSellersQueue() != null);
		assertTrue(btcExchange.getSellersQueue().size() == 2);
		assertTrue(btcExchange.getSellersQueue().poll().getId().equals(3L));
		assertTrue(btcExchange.getSellersQueue().poll().getId().equals(4L));

	}

	@Test
	public void testPriorityQueuesRefreshers() {
		ExchangeCurrency btcExchange = new ExchangeCurrency(dbService, new CurrencyPairs("BTC", "USD"));
		
		assertTrue(btcExchange != null && btcExchange.getBuyersQueue() != null && btcExchange.getSellersQueue() != null);
		assertTrue(btcExchange.getBuyersQueue().size() == 2 && btcExchange.getSellersQueue().size() == 2);
		
		Orders newBuyOrder = new Orders(dbService.getTraderById(1l), OrderType.BUY, new BigDecimal(5),
				new CurrencyPairs("BTC", "USD"), new BigDecimal(10), StatusEnum.OPEN);
		Orders newSellOrder = new Orders(dbService.getTraderById(1l), OrderType.SELL, new BigDecimal(5),
				new CurrencyPairs("BTC", "USD"), new BigDecimal(10), StatusEnum.OPEN);
		
		em.persist(newBuyOrder);
		em.persist(newSellOrder);
		btcExchange.refreshAll();
		
		assertTrue(btcExchange != null && btcExchange.getBuyersQueue() != null && btcExchange.getSellersQueue() != null);
		assertTrue(btcExchange.getBuyersQueue().size() == 3);
		assertTrue(btcExchange.getSellersQueue().size() == 3);
	}
}
