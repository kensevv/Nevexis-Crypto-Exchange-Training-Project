package com.nevexis.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQuery;

@Entity
@IdClass(CurrencyPairsId.class)
@NamedQuery(name = "CurrencyPairs.getAllCurrencyPairs", query = "SELECT c FROM CurrencyPairs c")
public class CurrencyPairs {
	@Id
	private String cryptoCode;
	@Id
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CurrencyPairs other = (CurrencyPairs) obj;
		if (cryptoCode == null) {
			if (other.cryptoCode != null)
				return false;
		} else if (!cryptoCode.equals(other.cryptoCode))
			return false;
		if (fiatCode == null) {
			if (other.fiatCode != null)
				return false;
		} else if (!fiatCode.equals(other.fiatCode))
			return false;
		return true;
	}
}