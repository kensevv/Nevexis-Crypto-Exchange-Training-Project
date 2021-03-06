package com.nevexis.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.nevexis.enums.OrderExecuteType;
import com.nevexis.enums.OrderType;
import com.nevexis.enums.StatusEnum;
import com.nevexis.services.NamedQueries;

@Entity
@NamedQuery(name = NamedQueries.getAllOrdersByStatus, query = "SELECT o FROM Orders o WHERE o.status = :status")
@NamedQuery(name = NamedQueries.getAllOrdersByType, query = "SELECT o FROM Orders o WHERE o.orderType= :type")
@NamedQuery(name = NamedQueries.getAllOrdersByTrader, query = "SELECT o FROM Orders o WHERE o.trader.id = :id")
@NamedQuery(name = NamedQueries.getAllOrdersByCrypto, query = "SELECT o FROM Orders o WHERE o.currencyPair.cryptoCode = :code")
@NamedQuery(name = NamedQueries.getAllOpenOrdersByCurrencyPairAndOrderType, query = "SELECT o FROM Orders o Join o.currencyPair cp WHERE o.status IN ('OPEN','PARTIALLY_EXECUTED') AND o.orderType = :orderType AND cp.cryptoCode = :cryptoCode AND cp.fiatCode = :fiatCode")
@Table(indexes = @Index(columnList = "timestamp"))
public class Orders extends BaseEntity {

	@Column(nullable = false)
	private Timestamp timestamp;

	@ManyToOne(fetch = FetchType.EAGER)
	private Traders trader;

	@Column(name = "order_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private OrderType orderType;

	@Column(precision = 19, scale = 10)
	private BigDecimal exchangeRate;

	@ManyToOne
	private CurrencyPairs currencyPair;

	@Column(name = "source_amount", nullable = false, precision = 19, scale = 10)
	private BigDecimal amount;

	@Column(name = "remaining_amount", nullable = false, precision = 19, scale = 10)
	private BigDecimal remainingAmount;

	@Column(length = 20, columnDefinition = "varchar(20) default 'OPEN'", nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusEnum status;

	@Column(length = 20, columnDefinition = "varchar(20) default 'LIMIT'", nullable = false)
	@Enumerated(EnumType.STRING)
	private OrderExecuteType executeType;

	@Column
	private Integer leverage;

	public Orders() {
	}

	public Orders(Traders trader, OrderType orderType, BigDecimal exchangeRate, CurrencyPairs currencyPair,
			BigDecimal amount, StatusEnum status, OrderExecuteType executeType, Integer leverage) {
		this.timestamp = new Timestamp(System.currentTimeMillis());
		this.setTrader(trader);
		this.setOrderType(orderType);
		this.setExchangeRate(exchangeRate);
		this.setCurrencyPair(currencyPair);
		this.setAmount(amount);
		this.setRemainingAmount(amount);
		this.setStatus(status);
		this.setExecuteType(executeType);
		this.setLeverage(leverage);
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public Traders getTrader() {
		return trader;
	}

	public void setTrader(Traders trader) {
		this.trader = trader;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public CurrencyPairs getCurrencyPair() {
		return currencyPair;
	}

	public void setCurrencyPair(CurrencyPairs currencyPair) {
		this.currencyPair = currencyPair;
	}

	public BigDecimal getRemainingAmount() {
		return remainingAmount;
	}

	public void setRemainingAmount(BigDecimal remainingAmount) {
		this.remainingAmount = remainingAmount;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public OrderExecuteType getExecuteType() {
		return executeType;
	}

	public void setExecuteType(OrderExecuteType executeType) {
		this.executeType = executeType;
	}

	public Integer getLeverage() {
		return leverage;
	}

	public void setLeverage(Integer leverage) {
		this.leverage = leverage;
	}
}