package com.nevexis.services;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.nevexis.entities.CurrencyPairs;
import com.nevexis.entities.Orders;
import com.nevexis.enums.OrderType;
import com.nevexis.enums.StatusEnum;
import com.nevexis.market.Exchanges;

@SpringBootTest
@ActiveProfiles("test")
@Sql("../insert_currency_traders_data.sql")
@Sql("../insert_test_orders.sql")
@Transactional
class OrdersTests extends BasicService {

	@Autowired
	private DBService service;

	@Autowired
	private Exchanges exchanges;

	@BeforeEach
	public void refreshMarkets() {
		exchanges.init();
	}

	@Test
	public void createOrderTest() {
		Orders newOrder = new Orders(service.getTraderById(1l), OrderType.BUY, new BigDecimal(5),
				new CurrencyPairs("BTC", "USD"), new BigDecimal(10), StatusEnum.OPEN);

		service.createOrder(newOrder);

		Orders order = em.find(Orders.class, newOrder.getId());

		assertTrue(order != null);
		assertTrue(order.getAmount().equals(newOrder.getAmount())
				&& order.getCurrencyPair().getCryptoCode().equals(newOrder.getCurrencyPair().getCryptoCode())
				&& order.getCurrencyPair().getFiatCode().equals(newOrder.getCurrencyPair().getFiatCode())
				&& order.getExchangeRate().equals(newOrder.getExchangeRate()) && order.getId().equals(newOrder.getId())
				&& order.getOrderType().equals(newOrder.getOrderType())
				&& order.getRemainingAmount().equals(newOrder.getRemainingAmount())
				&& order.getStatus().equals(newOrder.getStatus())
				&& order.getTimestamp().equals(newOrder.getTimestamp())
				&& order.getTrader().getId().equals(newOrder.getTrader().getId()));
	}

	@Test
	public void cancelOrderTest() {
		Orders order = service.cancelOrder(1l);
		assertTrue(em.find(Orders.class, order.getId()).getStatus().equals(StatusEnum.CANCELLED));
	}

	@Test
	public void getAllOrdersByStatusTest() {
		em.find(Orders.class, 1L).setStatus(StatusEnum.CANCELLED);
		em.find(Orders.class, 2L).setStatus(StatusEnum.CLOSED);
		em.find(Orders.class, 3L).setStatus(StatusEnum.PARTIALLY_EXECUTED);
		
		List<Orders> allOpenOrders = service.getAllOrdersByStatus(StatusEnum.OPEN);
		List<Orders> allCancelledOrders = service.getAllOrdersByStatus(StatusEnum.CANCELLED);
		List<Orders> allPartiallyExecutedOrders = service.getAllOrdersByStatus(StatusEnum.PARTIALLY_EXECUTED);
		List<Orders> allClosedOrders = service.getAllOrdersByStatus(StatusEnum.CLOSED);
		
		assertTrue(allOpenOrders.size() == 5 &&
				allCancelledOrders.size() == 1 &&
				allPartiallyExecutedOrders.size() == 1 &&
				allClosedOrders.size() == 1);
		
		assertEquals(allCancelledOrders.get(0).getId(), 1L);
		assertEquals(allClosedOrders.get(0).getId(), 2L);
		assertEquals(allPartiallyExecutedOrders.get(0).getId(), 3L);
	}

	@Test
	public void getAllOpenOrdersByCurrencyPairAndOrderTypeTest() {

	}
}