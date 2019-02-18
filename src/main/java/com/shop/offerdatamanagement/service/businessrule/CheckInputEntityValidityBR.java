package com.shop.offerdatamanagement.service.businessrule;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.shop.offerdatamanagement.dto.OfferDTO;
import com.shop.offerdatamanagement.dto.OfferDTO.StatusEnum;
import com.shop.offerdatamanagement.error.exception.BadRequestException;
import com.shop.offerdatamanagement.error.message.ExceptionMessage;

@Service
public class CheckInputEntityValidityBR {

	public void checkStartAndEndDateValidity(OfferDTO offerDTO){
		if (! offerDTO.getStartDate().isEqual(LocalDate.now())){
			throw new BadRequestException(offerDTO.getOfferCode(),ExceptionMessage.EXCEPTION_MESSAGE_START_DATE_NOT_VALID);
		}
		if (offerDTO.getEndDate().isBefore(offerDTO.getStartDate())) {
			 throw new BadRequestException(offerDTO.getOfferCode(), ExceptionMessage.EXCEPTION_MESSAGE_END_DATE_NOT_VALID);
		}
	}
	
	public void checkStatusValidity(OfferDTO offerDTO){
		if(offerDTO.getStatus() == null){
			offerDTO.setStatus(StatusEnum.ACTIVE);
		}
		if (offerDTO.getStatus() != null && offerDTO.getStatus().equals(StatusEnum.EXPIRED)){
			throw new BadRequestException(offerDTO.getOfferCode(), ExceptionMessage.EXCEPTION_MESSAGE_STATUS_NOT_VALID);
		}
	}
}
