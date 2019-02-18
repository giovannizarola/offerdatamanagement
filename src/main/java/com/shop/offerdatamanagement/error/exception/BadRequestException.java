package com.shop.offerdatamanagement.error.exception;

public class BadRequestException extends RuntimeException {

	private final String offerCode;

    public BadRequestException(String offerCode, String message) {
        super(message);
        this.offerCode = offerCode;
    }

	public String getOfferCode() {
		return offerCode;
	}
}
