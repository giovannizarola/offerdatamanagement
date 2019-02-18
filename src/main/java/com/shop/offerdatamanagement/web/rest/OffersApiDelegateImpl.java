package com.shop.offerdatamanagement.web.rest;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.shop.offerdatamanagement.dto.OfferDTO;
import com.shop.offerdatamanagement.service.OfferService;
import com.shop.offerdatamanagement.web.rest.util.HeaderUtil;

@Service
public class OffersApiDelegateImpl implements OffersApiDelegate {

	private static final String ENTITY_NAME = "Offer";
	private final Logger log = LoggerFactory.getLogger(OffersApiDelegateImpl.class);

	private final OfferService offerService;

	public OffersApiDelegateImpl(OfferService offerService) {
		this.offerService = offerService;
	}

	@Override
	public ResponseEntity<OfferDTO> createOffer(OfferDTO offerDTO) {
		log.debug("REST request to save Offer : {}", offerDTO);
		
		OfferDTO result = offerService.save(offerDTO);
		return new ResponseEntity<>(result,HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getOfferCode()), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<Void> deleteOffer(String offerCode) {
		log.debug("REST request to delete Offer : {}", offerCode);
        offerService.delete(offerCode);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, offerCode)).build();
	}

	@Override
	public ResponseEntity<List<OfferDTO>> getAllOffers() {
		log.debug("REST request to get all Offers");
		List<OfferDTO> list = offerService.findAllActive();
		return  (list.isEmpty()) ?  new ResponseEntity<>(list,HttpStatus.NOT_FOUND) : new ResponseEntity<>(list, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<OfferDTO> getOffer(String offerCode) {
		Optional<OfferDTO> offerDTO = offerService.findByOfferCode(offerCode);
		return offerDTO.map(response -> ResponseEntity.ok().headers(null).body(response))
	            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

}
