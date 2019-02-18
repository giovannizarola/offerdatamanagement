package com.shop.offerdatamanagement.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shop.offerdatamanagement.data.entity.Offer;


/**
 * Spring Data  repository for the Offer entity.
 */
@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

	 @Query("SELECT o FROM Offer o where o.offerCode = :offerCode and o.status='ACTIVE'") 
	 Optional<Offer> findByOfferCode(@Param("offerCode") String offerCode);
	 
	 @Query("SELECT o FROM Offer o where o.productCode = :productCode and o.status='ACTIVE'") 
	 Optional<Offer> findByProductCode(@Param("productCode") String productCode);
	 
	 @Query("SELECT o FROM Offer o where o.status='ACTIVE'") 
	 List<Offer> findAllActive();
	 
	 void deleteByOfferCode(@Param("offerCode") String offerCode);
}
