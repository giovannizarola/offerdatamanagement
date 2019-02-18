package com.shop.offerdatamanagement.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.shop.offerdatamanagement.data.entity.Offer;
import com.shop.offerdatamanagement.data.entity.OfferStatus;
import com.shop.offerdatamanagement.data.repository.OfferRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScheduledOfferServiceTest {

	@Autowired
	private OfferRepository offerRepository;

	@Autowired
	private ScheduledOfferService scheduledOfferServiceImpl;

	@Test
	@Transactional
	public void updateStatusToExpiredOnlyForOffersWithEndDateBeforThenToday() {
		// create an offer expired yesterday with status still ACTIVE
		Offer oldOffer = new Offer().offerCode("00001").productCode("001").description("30% discout")
				.offerPrice(new BigDecimal(14.6)).startDate(LocalDate.now().minusDays(10))
				.endDate(LocalDate.now().minusDays(1)).status(OfferStatus.ACTIVE);

		offerRepository.save(oldOffer);

		// create a new offer that will expire tomorrow
		Offer newOffer = new Offer().offerCode("00002").productCode("002").description("40% discout")
				.offerPrice(new BigDecimal(20.5)).startDate(LocalDate.now().minusDays(1))
				.endDate(LocalDate.now().plusDays(1)).status(OfferStatus.ACTIVE);

		offerRepository.save(newOffer);

		int databaseSizeAfterCreate = offerRepository.findAll().size();

		scheduledOfferServiceImpl.updateOffersStatusIfExpired();

		List<Offer> offerList = offerRepository.findAllActive();
		assertThat(offerList).hasSize(databaseSizeAfterCreate - 1);
	}
}
