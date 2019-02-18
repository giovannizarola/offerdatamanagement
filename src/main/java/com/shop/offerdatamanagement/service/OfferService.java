package com.shop.offerdatamanagement.service;


import java.util.List;
import java.util.Optional;

import com.shop.offerdatamanagement.dto.OfferDTO;

/**
 * Service Interface for managing Offer.
 */
public interface OfferService {

    /**
     * Save a offer.
     *
     * @param offerDTO the entity to save
     * @return the persisted entity
     */
    OfferDTO save(OfferDTO offerDTO);

    /**
     * Get all the active offers.
     *
     * @return the list of entities
     */
    List<OfferDTO> findAllActive();


    /**
     * Get the "offerCode" offer.
     *
     * @param offerCode the offerCode of the entity
     * @return the entity
     */
    Optional<OfferDTO> findByOfferCode(String offerCode);

    /**
     * Delete the "offerCode" offer.
     *
     * @param offerCode the offerCode of the entity
     */
    void delete(String offerCode);
    
}
