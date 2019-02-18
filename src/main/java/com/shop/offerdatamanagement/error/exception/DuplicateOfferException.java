package com.shop.offerdatamanagement.error.exception;

public class DuplicateOfferException extends RuntimeException{

	private final String offerCode;

    public DuplicateOfferException(String offerCode, String message) {
        super(message);
        this.offerCode = offerCode;
    }

	public String getOfferCode() {
		return offerCode;
	}


}
