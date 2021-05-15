package com.nevexis.dtos;


public class CurrencyPairsDTO {
	@SuppressWarnings("unused")
	private String cryptoCode;
	@SuppressWarnings("unused")
	private String fiatCode;
	
	public CurrencyPairsDTO(String cryptoCode, String fiatCode) {
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
