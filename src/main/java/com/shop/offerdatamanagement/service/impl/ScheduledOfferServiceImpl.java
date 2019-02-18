package com.shop.offerdatamanagement.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.offerdatamanagement.data.entity.Offer;
import com.shop.offerdatamanagement.data.entity.OfferStatus;
import com.shop.offerdatamanagement.data.repository.OfferRepository;
import com.shop.offerdatamanagement.service.ScheduledOfferService;

@Service
@Transactional
public class ScheduledOfferServiceImpl implements ScheduledOfferService{

    private final OfferRepository offerRepository;
    
    public ScheduledOfferServiceImpl(OfferRepository offerRepository){
    	this.offerRepository = offerRepository;
    }
    
	@Override
	@Scheduled(cron = "0 0 3 * * *")
	public void updateOffersStatusIfExpired() {
		List<Offer> stillActiveOffer = offerRepository.findAllActive();
		stillActiveOffer.stream().filter(offer -> offer.getEndDate().isBefore(LocalDate.now()))
				.forEach(offer -> {
					offer.setStatus(OfferStatus.EXPIRED);
					offerRepository.save(offer);
				});
	}

}
