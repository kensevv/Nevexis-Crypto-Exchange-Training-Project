package com.nevexis.entities;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name = "CurrencyPairs.getAllCurrencyPairs", query = "SELECT c FROM CurrencyPairs c")
public class CurrencyPairs extends BaseEntity {
	private String cryptoCode;
	private String fiatCode;

	public CurrencyPairs() {
	}

	public CurrencyPairs(String cryptoCode, String fiatCode) {
		this.cryptoCode = cryptoCode;
		this.fiatCode = fiatCode;
	}

	public String getCryptoCode() {
		return cryptoCode;
	}

	public String getFiatCode() {
		return fiatCode;
	}
}