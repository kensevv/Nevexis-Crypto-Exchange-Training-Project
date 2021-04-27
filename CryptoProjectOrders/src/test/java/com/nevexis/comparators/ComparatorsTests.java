package com.nevexis.comparators;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;

import com.nevexis.entities.Orders;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class ComparatorsTests {
	private final OrdersBuyersComparator buyersComparator = new OrdersBuyersComparator();
	private final OrdersSellersComparator sellersComparator = new OrdersSellersComparator();

	private Orders order1;
	private Orders order2;
	private Orders order3;

	@BeforeAll
	public void setup() {
		order1 = new Orders();
		order1.setExchangeRate(new BigDecimal(5));
		order1.setTimestamp(new Timestamp(System.currentTimeMillis()));

		order2 = new Orders();
		order2.setExchangeRate(new BigDecimal(10));
		order2.setTimestamp(new Timestamp(System.currentTimeMillis() / 10));

		order3 = new Orders();
		order3.setExchangeRate(new BigDecimal(5));
		order3.setTimestamp(new Timestamp(System.currentTimeMillis() * 10));

	}

	@Test
	public void ordersBuyersComparatorTest() {
		assertEquals(buyersComparator.compare(order1, order2), 1);
		assertEquals(buyersComparator.compare(order2, order1), -1);
		assertEquals(buyersComparator.compare(order2, order3), -1);
		// if exchanges are equal, compared by timestamp
		assertEquals(buyersComparator.compare(order1, order3), -1);
	}

	@Test
	public void ordersSellersComparatorTest() {
		assertEquals(sellersComparator.compare(order1, order2), -1);
		assertEquals(sellersComparator.compare(order2, order1), 1);
		assertEquals(sellersComparator.compare(order2, order3), 1);
		assertEquals(sellersComparator.compare(order1, order3), -1);
	}
}
