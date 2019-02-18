package com.shop.offerdatamanagement.service.impl;


import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.offerdatamanagement.data.entity.Offer;
import com.shop.offerdatamanagement.data.repository.OfferRepository;
import com.shop.offerdatamanagement.dto.OfferDTO;
import com.shop.offerdatamanagement.error.exception.DuplicateOfferException;
import com.shop.offerdatamanagement.error.exception.ResourceNotFoundException;
import com.shop.offerdatamanagement.error.message.ExceptionMessage;
import com.shop.offerdatamanagement.service.OfferService;
import com.shop.offerdatamanagement.service.businessrule.CheckInputEntityValidityBR;
import com.shop.offerdatamanagement.service.mapper.OfferMapper;

/**
 * Service Implementation for managing Offer.
 */
@Service
@Transactional
public class OfferServiceImpl implements OfferService {

    private final Logger log = LoggerFactory.getLogger(OfferServiceImpl.class);

    private final OfferRepository offerRepository;

    private final OfferMapper offerMapper;
    
    private final CheckInputEntityValidityBR checkInputEntityValidityBR;

    public OfferServiceImpl(OfferRepository offerRepository, OfferMapper offerMapper, CheckInputEntityValidityBR checkInputEntityValidityBR) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
        this.checkInputEntityValidityBR = checkInputEntityValidityBR;
    }

    /**
     * Save a offer.
     *
     * @param offerDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public OfferDTO save(OfferDTO offerDTO) {
        log.debug("Request to save Offer : {}", offerDTO);
        checkInputEntityValidityBR.checkStartAndEndDateValidity(offerDTO);
        checkInputEntityValidityBR.checkStatusValidity(offerDTO);
        
        /* check if a an offer with this offer code already exist*/
        Optional<Offer> offerReaded = offerRepository.findByOfferCode(offerDTO.getOfferCode());
        offerReaded.ifPresent(offer -> {throw new DuplicateOfferException(offer.getOfferCode(), ExceptionMessage.EXCEPTION_MESSAGE_DUPLICATE_DUPLICATE_OFFER); });
        
        /* check if a an offer for this product code already exist*/
        offerReaded = offerRepository.findByProductCode(offerDTO.getProductCode());
        offerReaded.ifPresent(offer -> {throw new DuplicateOfferException(offer.getOfferCode(), ExceptionMessage.EXCEPTION_MESSAGE_DUPLICATE_DUPLICATE_OFFER_FOR_THIS_PRODUCT); });


        Offer offer = offerMapper.toEntity(offerDTO);
        offer = offerRepository.save(offer);
        return offerMapper.toDto(offer);
    }

    /**
     * Get all the offers.
     *
     * @return the list of Active entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<OfferDTO> findAllActive() {
        log.debug("Request to get all Active Offers");
        return offerRepository.findAllActive().stream()
            .map(offerMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one offer by offerCode.
     *
     * @param offerCode the offerCode of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OfferDTO> findByOfferCode(String offerCode) {
        log.debug("Request to get Offer : {}", offerCode);
        return offerRepository.findByOfferCode(offerCode)
            .map(offerMapper::toDto);
    }

    /**
     * Delete the offer by offerCode.
     *
     * @param offerCode the offerCode of the entity
     */
    @Override
    public void delete(String offerCode) {
        log.debug("Request to delete Offer : {}", offerCode);  
        
        Optional<Offer> offerReaded = offerRepository.findByOfferCode(offerCode);
        if (offerReaded.isPresent()){
        	offerRepository.deleteByOfferCode(offerCode);
        }else{
        	throw new ResourceNotFoundException(offerCode, ExceptionMessage.EXCEPTION_MESSAGE_OFFER_NOT_FOUND);
        }
   
       
    }
	
}
