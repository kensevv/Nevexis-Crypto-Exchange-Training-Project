package com.nevexis.ohlc;

import java.math.BigDecimal;

public class Ohlc {
	private String currencyName;		
	private BigDecimal open;
	private BigDecimal high;
	private BigDecimal low;
	private BigDecimal close;
	
	public Ohlc(String currencyName, BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close) {
		this.currencyName = currencyName;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
	}
}
