package com.shop.offerdatamanagement.error.exception;

public class ResourceNotFoundException extends RuntimeException {

	private final String offerCode;

    public ResourceNotFoundException(String offerCode, String message) {
        super(message);
        this.offerCode = offerCode;
    }

	public String getOfferCode() {
		return offerCode;
	}

}