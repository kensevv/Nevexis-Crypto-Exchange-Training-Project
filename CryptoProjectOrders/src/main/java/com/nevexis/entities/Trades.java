package com.nevexis.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

import com.nevexis.enums.OrderType;

@NamedQuery(name = "Trades.getAllTradesByCurrencyPair", query = "SELECT t FROM Trades t Join t.currencyPair cp WHERE cp.cryptoCode = :cryptoCode AND cp.fiatCode = :fiatCode ORDER BY t.timestamp desc")
@Entity
@NamedQuery(name = "Trades.getAllTradesByTrader", query = "SELECT t FROM Trades t WHERE t.order.trader.id = :id")
public class Trades extends BaseEntity {
	private Timestamp timestamp;

	@ManyToOne
	private Orders order;

	@ManyToOne
	private CurrencyPairs currencyPair;

	@Column(nullable = false, precision = 19, scale = 10)
	private BigDecimal amount;

	@Column(name = "order_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private OrderType orderType;

	@Column(nullable = false, precision = 19, scale = 10)
	private BigDecimal exchangeRate;

	public Trades(Timestamp timestamp, Orders order, CurrencyPairs currencyPair, BigDecimal amount, OrderType orderType,
			BigDecimal exchangeRate) {
		this.timestamp = timestamp;
		this.order = order;
		this.currencyPair = currencyPair;
		this.amount = amount;
		this.orderType = orderType;
		this.exchangeRate = exchangeRate;
	}

	public Trades(){}
	public Timestamp getTimestamp() {
		return timestamp;
	}

	public Orders getOrder() {
		return order;
	}

	public CurrencyPairs getCurrencyPair() {
		return currencyPair;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public OrderType getOrderType() {
		return orderType;
	}
}