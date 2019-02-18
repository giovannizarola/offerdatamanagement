package com.shop.offerdatamanagement.service.mapper;


import org.mapstruct.Mapper;

import com.shop.offerdatamanagement.data.entity.Offer;
import com.shop.offerdatamanagement.dto.OfferDTO;

/**
 * Mapper for the entity Offer and its DTO OfferDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OfferMapper extends EntityMapper<OfferDTO, Offer> {



    default Offer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Offer offer = new Offer();
        offer.setId(id);
        return offer;
    }
}
