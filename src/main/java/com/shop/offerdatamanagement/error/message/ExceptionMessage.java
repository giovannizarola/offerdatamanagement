package com.shop.offerdatamanagement.error.message;

public interface ExceptionMessage {
	
	public static final String EXCEPTION_MESSAGE_START_DATE_NOT_VALID = "A new offer must start today. PLease check start date";
	public static final String EXCEPTION_MESSAGE_END_DATE_NOT_VALID = "A new offer cannot end in the past. Please check end date";
	public static final String EXCEPTION_MESSAGE_STATUS_NOT_VALID = "A new offer must have status ACTIVE";
    public static final String EXCEPTION_MESSAGE_DUPLICATE_DUPLICATE_OFFER = "An offer with same code already exist";
    public static final String EXCEPTION_MESSAGE_DUPLICATE_DUPLICATE_OFFER_FOR_THIS_PRODUCT = "An offer for this product already exist";
	public static final String EXCEPTION_MESSAGE_OFFER_NOT_FOUND = "Offer not found";

}
