package com.shop.offerdatamanagement.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.shop.offerdatamanagement.data.entity.Offer;
import com.shop.offerdatamanagement.data.repository.OfferRepository;
import com.shop.offerdatamanagement.dto.OfferDTO;
import com.shop.offerdatamanagement.dto.OfferDTO.StatusEnum;
import com.shop.offerdatamanagement.error.exception.BadRequestException;
import com.shop.offerdatamanagement.error.exception.DuplicateOfferException;
import com.shop.offerdatamanagement.error.exception.ResourceNotFoundException;
import com.shop.offerdatamanagement.service.mapper.OfferMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OfferServiceTest {

	@Autowired
	private OfferRepository offerRepository;

	@Autowired
	private OfferService offerServiceToTest;

	@Autowired
	OfferMapper offerMapper;

	private OfferDTO offerDTO;

    private static final String DEFAULT_OFFER_CODE = "00001";
	
	private static final String DEFAULT_OFFER_CODE_UPDATED = "00002";

	private static final String DEFAULT_PRODUCT_CODE = "00001";

	private static final String DEFAULT_DESCRIPTION = "This Product is 30% Discout for 10 days";

	private static final BigDecimal DEFAULT_OFFER_PRICE = new BigDecimal(10);

	private static final LocalDate DEFAULT_START_DATE = LocalDate.now();

	private static final LocalDate DEFAULT_END_DATE = LocalDate.now().plusDays(10);

	private static final StatusEnum DEFAULT_STATUS = StatusEnum.ACTIVE;
	
	@Before
	public void initTest() {
		offerDTO = createOfferDTO();
	}

	public static OfferDTO createOfferDTO() {
		OfferDTO offerDTO = new OfferDTO().offerCode(DEFAULT_OFFER_CODE).productCode(DEFAULT_PRODUCT_CODE).description(DEFAULT_DESCRIPTION)
				.offerPrice(DEFAULT_OFFER_PRICE).startDate(DEFAULT_START_DATE).endDate(DEFAULT_END_DATE)
				.status(DEFAULT_STATUS);
		return offerDTO;
	}

	
	@Transactional
	@Test
	public void createOffer_whenOfferCodeNotDuplicate_IsSaved() {
		int databaseSizeBeforeCreate = offerRepository.findAllActive().size();
		offerServiceToTest.save(offerDTO);

		List<Offer> offerList = offerRepository.findAllActive();
		assertThat(offerList).hasSize(databaseSizeBeforeCreate + 1);
	}

	@Transactional
	@Test(expected = DuplicateOfferException.class)
	public void createOffer_WithOfferCodeThatAlreadyExist_MustFail() {
		// create a new product
		offerServiceToTest.save(offerDTO);

		// create an Offer with same offer come of the previous one 
		offerServiceToTest.save(offerDTO);
	}
	
	@Transactional
	@Test(expected = DuplicateOfferException.class)
	public void createOffer_ForAProductThatAlreadyHasAnActiveOffer_MustFail() {
		// create a new product
		offerServiceToTest.save(offerDTO);
		
		offerDTO.setOfferCode(DEFAULT_OFFER_CODE_UPDATED);
		
		// create an Offer with the same product code of the previous one 
		offerServiceToTest.save(offerDTO);

	}
	
	@Transactional
	@Test(expected = BadRequestException.class)
	public void createOffer_WithStartDateNotEqualToToday_MustFail() {
	
		// set the Start Date to Yesterday
		offerDTO.setStartDate(LocalDate.now().minusDays(1));
		offerServiceToTest.save(offerDTO);
	}
	
	@Transactional
	@Test(expected = BadRequestException.class)
	public void createOffer_WithEndDateBeforeStartDate_MustFailIf() {
		
		offerDTO.setStartDate(LocalDate.now());
		offerDTO.setEndDate(LocalDate.now().minusDays(1));

		offerServiceToTest.save(offerDTO);
	}
	
	@Transactional
	@Test(expected = BadRequestException.class)
	public void createOffer_WithStatusDifferentThanActive_MustFail() {
		
		// set status to Expired
		offerDTO.setStatus(StatusEnum.EXPIRED);
		
		offerServiceToTest.save(offerDTO);
	}
	
	@Test
    @Transactional
    public void getAllOffers_WhenOfferExist_ReturnSuccess() throws Exception {
		int databaseSizeBeforeCreate = offerRepository.findAllActive().size();

        // Initialize the database
        offerRepository.saveAndFlush(offerMapper.toEntity(offerDTO));

        // Get all the offerList
        List<OfferDTO> offerDTOList = offerServiceToTest.findAllActive();
        assertThat(offerDTOList).hasSize(databaseSizeBeforeCreate + 1);
    }
	
	@Test
    @Transactional
    public void getAllOffers_WhenThereIsNoOffer_ReturnEmpty() throws Exception {
        // Get all the offerList
        List<OfferDTO> offerDTOList = offerServiceToTest.findAllActive();
        assertThat(offerDTOList).hasSize(0);
    }
	
    @Test
    @Transactional
    public void getOfferByCode_WhenOfferExist_ReturnSuccess() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offerMapper.toEntity(offerDTO));

        // Get the offer
        Optional<OfferDTO> newOfferDTO = offerServiceToTest.findByOfferCode(offerDTO.getOfferCode());
        assertThat(newOfferDTO.isPresent()).isEqualTo(true);
        assertThat(newOfferDTO.get().getOfferCode()).isEqualTo(offerDTO.getOfferCode());
    }
    
    @Test
    @Transactional
    public void getOfferByCode_WhenOfferNotExist_ReturnEmpty() throws Exception {
        // Get the offer
    	Optional<OfferDTO> newOfferDTO = offerServiceToTest.findByOfferCode("not-exist");
        assertThat(newOfferDTO.isPresent()).isEqualTo(false);
    }
    
    @Test
    @Transactional
    public void deleteOffer_WhenOfferExist_ReturnSuccess() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offerMapper.toEntity(offerDTO));
        int databaseSizeAfterCreation = offerRepository.findAllActive().size();
        
        // Delete the offer
        offerServiceToTest.delete(offerDTO.getOfferCode());
        int databaseSizeAfterDeletion = offerRepository.findAllActive().size();
        
        assertThat(databaseSizeAfterDeletion).isEqualTo(databaseSizeAfterCreation - 1 );
    }
    
    @Test(expected = ResourceNotFoundException.class)
    @Transactional
    public void deleteOffer_WhenOfferNotExist_MustFail() throws Exception {
        // Delete the offer
        offerServiceToTest.delete("not-exist");
    }

}
