package com.nevexis.entities;

import java.io.Serializable;

public class CurrencyPairsId implements Serializable {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private String cryptoCode;
	@SuppressWarnings("unused")
	private String fiatCode;

	public CurrencyPairsId() {
	}

	public CurrencyPairsId(String cryptoCode, String fiatCode) {
		this.cryptoCode = cryptoCode;
		this.fiatCode = fiatCode;
	}

	public CurrencyPairsId(CurrencyPairs currencyPair) {
		this.cryptoCode = currencyPair.getCryptoCode();
		this.fiatCode = currencyPair.getFiatCode();
	}
}
