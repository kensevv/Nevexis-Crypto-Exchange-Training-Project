package com.nevexis.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nevexis.dtos.CurrencyPairsDTO;
import com.nevexis.entities.CurrencyPairs;
import com.nevexis.entities.Orders;
import com.nevexis.enums.OrderExecuteType;
import com.nevexis.enums.OrderType;
import com.nevexis.enums.StatusEnum;
import com.nevexis.services.DBService;

@RestController
public class Controller {
	@Autowired
	private DBService dbService;

	@PostMapping("/{trader}/create/order")
	public Orders createNewOrder(@PathVariable("trader") Long traderID, @RequestParam OrderType orderType,
			@RequestBody CurrencyPairsDTO currencyPairDTO, @RequestParam BigDecimal exchangeRate,
			@RequestParam BigDecimal amount, @RequestParam OrderExecuteType executeType, @RequestParam Integer leverage) {

		return dbService.createOrder(new Orders(dbService.getTraderById(traderID), orderType, exchangeRate,
				dbService.findCurrencyPair(new CurrencyPairs(currencyPairDTO)), amount, StatusEnum.OPEN, executeType, leverage));
	}

	@PostMapping("/cancel/{orderId}")
	public Orders cancelOrder(@PathVariable Long orderId) {
		return dbService.cancelOrder(orderId);
	}

	@GetMapping("/listOrders/{status}")
	public List<Orders> findAllOrdersByStatus(@PathVariable StatusEnum status) {
		return dbService.getAllOrdersByStatus(status);
	}


	@GetMapping("/listOrders/type/{orderType}")
	public List<Orders> findAllOrdersByOrderType(@PathVariable OrderType orderType){
		return dbService.getAllOrdersByType(orderType);
	}

	@GetMapping("/listOrders/user/{traderId}")
	public List<Orders> findAllOrdersByTraders(@PathVariable Long traderId){
		return dbService.getAllOrdersByTrader(traderId);
	}

	@GetMapping("/listOrders/crypto/{cryptoCode}")
	public List<Orders> findAllOrdersByCryptoCode(@PathVariable String cryptoCode){
		return  dbService.getAllOrdersByCrypto(cryptoCode);
	}
}
