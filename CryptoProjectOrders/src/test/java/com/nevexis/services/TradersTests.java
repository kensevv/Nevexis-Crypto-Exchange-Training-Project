package com.nevexis.services;

//import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.nevexis.entities.Traders;

@SpringBootTest
@ActiveProfiles("test")
@Sql("../insert_data.sql")
@Transactional
public class TradersTests extends BasicService{

	@Autowired
	private DBService service;
	
	@Sql(statements = "INSERT INTO traders (id) VALUES(1)")
	@Test
	public void getTraderByIdTest() {
		Traders getDbTrader = service.getTraderById(1l);
		assertEquals(getDbTrader.getId(), 1L);
		
		em.remove(getDbTrader);
		assertTrue(service.getTraderById(1L) == null);
	}
}