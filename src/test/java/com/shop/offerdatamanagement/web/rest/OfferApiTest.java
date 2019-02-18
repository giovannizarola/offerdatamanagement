package com.shop.offerdatamanagement.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.offerdatamanagement.data.entity.Offer;
import com.shop.offerdatamanagement.data.entity.OfferStatus;
import com.shop.offerdatamanagement.data.repository.OfferRepository;
import com.shop.offerdatamanagement.dto.OfferDTO;
import com.shop.offerdatamanagement.dto.OfferDTO.StatusEnum;
import com.shop.offerdatamanagement.error.handler.GlobalExceptionHandler;
import com.shop.offerdatamanagement.service.mapper.OfferMapper;


@RunWith(SpringRunner.class)
@SpringBootTest
public class OfferApiTest {

	private static final String DEFAULT_OFFER_CODE = "00001";
	
	private static final String DEFAULT_OFFER_CODE_UPDATED = "00002";

	private static final String DEFAULT_PRODUCT_CODE = "00001";

	private static final String DEFAULT_DESCRIPTION = "This Product is 30% Discout for 10 days";

	private static final BigDecimal DEFAULT_OFFER_PRICE = new BigDecimal(10);

	private static final LocalDate DEFAULT_START_DATE = LocalDate.now();

	private static final LocalDate DEFAULT_END_DATE = LocalDate.now().plusDays(10);

	private static final StatusEnum DEFAULT_STATUS = StatusEnum.ACTIVE;

	@Autowired
	private OfferRepository offerRepository;

	@Autowired
	private OffersApiDelegate offersApiDelegate;

	@Autowired
	ObjectMapper objectMapper;

    @Autowired
    private OfferMapper offerMapper;
    
	@Autowired
	GlobalExceptionHandler globalExceptionHandler;

	private MockMvc restOfferMockMvc;

	private OfferDTO offerDTO;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final OffersApi offerResource = new OffersApiController(offersApiDelegate);
		this.restOfferMockMvc = MockMvcBuilders.standaloneSetup(offerResource)
				.setControllerAdvice(globalExceptionHandler).build();
	}

	@Before
	public void initTest() {
		offerDTO = createOfferDTO();
	}

	public static OfferDTO createOfferDTO() {
		OfferDTO offerDTO = new OfferDTO().offerCode(DEFAULT_OFFER_CODE).productCode(DEFAULT_PRODUCT_CODE)
				.description(DEFAULT_DESCRIPTION).offerPrice(DEFAULT_OFFER_PRICE).startDate(DEFAULT_START_DATE)
				.endDate(DEFAULT_END_DATE).status(DEFAULT_STATUS);
		return offerDTO;
	}

	@Test
	@Transactional
	public void createOffer() throws Exception {
		int databaseSizeBeforeCreate = offerRepository.findAllActive().size();

		// Create the Offer
		restOfferMockMvc.perform(
				post("/v1/offers").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsBytes(offerDTO)))
				.andExpect(status().isCreated());

		// Validate the Offer in the database
		List<Offer> offerList = offerRepository.findAllActive();
		assertThat(offerList).hasSize(databaseSizeBeforeCreate + 1);
		Offer testOffer = offerList.get(offerList.size() - 1);
		assertThat(testOffer.getOfferCode()).isEqualTo(DEFAULT_OFFER_CODE);
		assertThat(testOffer.getProductCode()).isEqualTo(DEFAULT_PRODUCT_CODE);
		assertThat(testOffer.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
		assertThat(testOffer.getOfferPrice()).isEqualTo(DEFAULT_OFFER_PRICE);
		assertThat(testOffer.getStartDate()).isEqualTo(DEFAULT_START_DATE);
		assertThat(testOffer.getEndDate()).isEqualTo(DEFAULT_END_DATE);
		assertThat(testOffer.getStatus()).isEqualTo(OfferStatus.ACTIVE);
	}

	@Test
	@Transactional
	public void checkOfferCode_IsRequired_MustFailWhenIsNull() throws Exception {
		int databaseSizeBeforeTest = offerRepository.findAllActive().size();
		// set the field null
		offerDTO.setOfferCode(null);

		restOfferMockMvc.perform(
				post("/v1/offers").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsBytes(offerDTO)))
				.andExpect(status().isBadRequest());

		List<Offer> offerList = offerRepository.findAllActive();
		assertThat(offerList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkProductCode_IsRequired_MustFailWhenIsNull() throws Exception {
		int databaseSizeBeforeTest = offerRepository.findAllActive().size();
		// set the field null
		offerDTO.setProductCode(null);

		restOfferMockMvc.perform(
				post("/v1/offers").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsBytes(offerDTO)))
				.andExpect(status().isBadRequest());

		List<Offer> offerList = offerRepository.findAllActive();
		assertThat(offerList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkDescription_IsRequired_MustFailWhenIsNull() throws Exception {
		int databaseSizeBeforeTest = offerRepository.findAllActive().size();
		// set the field null
		offerDTO.setDescription(null);

		restOfferMockMvc.perform(
				post("/v1/offers").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsBytes(offerDTO)))
				.andExpect(status().isBadRequest());

		List<Offer> offerList = offerRepository.findAllActive();
		assertThat(offerList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkOfferPrice_IsRequired_MustFailWhenIsNull() throws Exception {
		int databaseSizeBeforeTest = offerRepository.findAllActive().size();
		// set the field null
		offerDTO.setOfferPrice(null);

		restOfferMockMvc.perform(
				post("/v1/offers").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsBytes(offerDTO)))
				.andExpect(status().isBadRequest());

		List<Offer> offerList = offerRepository.findAllActive();
		assertThat(offerList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkStartDateIsRequired_MustFailWhenIsNull() throws Exception {
		int databaseSizeBeforeTest = offerRepository.findAllActive().size();
		// set the field null
		offerDTO.setStartDate(null);

		restOfferMockMvc.perform(
				post("/v1/offers").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsBytes(offerDTO)))
				.andExpect(status().isBadRequest());

		List<Offer> offerList = offerRepository.findAllActive();
		assertThat(offerList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkStartDateIsEqualToToday_MustFailIfIsNot() throws Exception {
		int databaseSizeBeforeTest = offerRepository.findAllActive().size();
		
		// set the Start Date to Yesterday
		offerDTO.setStartDate(LocalDate.now().minusDays(1));

		restOfferMockMvc.perform(
				post("/v1/offers").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsBytes(offerDTO)))
				.andExpect(status().isBadRequest());

		// set the Start Date to Tomorrow
		offerDTO.setStartDate(LocalDate.now().plusDays(1));
		restOfferMockMvc.perform(
				post("/v1/offers").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsBytes(offerDTO)))
				.andExpect(status().isBadRequest());
		
		List<Offer> offerList = offerRepository.findAllActive();
		assertThat(offerList).hasSize(databaseSizeBeforeTest);
	}
	
	@Test
	@Transactional
	public void checkEndDate_IsRequired_MustFailWhenIsNull() throws Exception {
		int databaseSizeBeforeTest = offerRepository.findAllActive().size();
		// set the field null
		offerDTO.setEndDate(null);

		restOfferMockMvc.perform(
				post("/v1/offers").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsBytes(offerDTO)))
				.andExpect(status().isBadRequest());

		List<Offer> offerList = offerRepository.findAllActive();
		assertThat(offerList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkEndDateAfterThenStartDate_MustFailIfIsNot() throws Exception {
		int databaseSizeBeforeTest = offerRepository.findAllActive().size();
		
		offerDTO.setStartDate(LocalDate.now());
		offerDTO.setEndDate(LocalDate.now().minusDays(1));

		restOfferMockMvc.perform(
				post("/v1/offers").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsBytes(offerDTO)))
				.andExpect(status().isBadRequest());

		List<Offer> offerList = offerRepository.findAllActive();
		assertThat(offerList).hasSize(databaseSizeBeforeTest);
	}
	
	@Test
	@Transactional
	public void createOfferWithOfferCodeThatAlreadyExist_MustFail() throws Exception {
		restOfferMockMvc.perform(
				post("/v1/offers").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsBytes(offerDTO)))
				.andExpect(status().isCreated());

		int databaseSizeAfterFirstCreation = offerRepository.findAllActive().size();

		// An entity with offer code equal to the offer code of an existing
		// offer cannot be created. Offercode is unique
		restOfferMockMvc.perform(
				post("/v1/offers").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsBytes(offerDTO)))
				.andExpect(status().isConflict());

		// Validate the Offer in the database
		List<Offer> offerList = offerRepository.findAllActive();
		assertThat(offerList).hasSize(databaseSizeAfterFirstCreation);
	}

	@Test
	@Transactional
	public void createOfferWithProductCodeThatAlreadyExist_MustFail() throws Exception {
		restOfferMockMvc.perform(
				post("/v1/offers").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsBytes(offerDTO)))
				.andExpect(status().isCreated());

		int databaseSizeAfterFirstCreation = offerRepository.findAllActive().size();
		
		offerDTO.setOfferCode(DEFAULT_OFFER_CODE_UPDATED);
		// An entity with product code equal to the product code of an existing and active
		// offer cannot be created. Product code is unique for active offers
		restOfferMockMvc.perform(
				post("/v1/offers").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsBytes(offerDTO)))
				.andExpect(status().isConflict());

		// Validate the Offer in the database
		List<Offer> offerList = offerRepository.findAllActive();
		assertThat(offerList).hasSize(databaseSizeAfterFirstCreation);
	}
	
	@Test
	@Transactional
	public void createOfferWithStatusDifferentThanActive_MustFail() throws Exception {
		int databaseSizeBeforeCreate = offerRepository.findAllActive().size();

		offerDTO.setStatus(StatusEnum.EXPIRED);
		restOfferMockMvc.perform(
				post("/v1/offers").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsBytes(offerDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Offer in the database
		List<Offer> offerList = offerRepository.findAllActive();
		assertThat(offerList).hasSize(databaseSizeBeforeCreate);
	}
	
    @Test
    @Transactional
    public void getAllOffers() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offerMapper.toEntity(offerDTO));

        // Get all the offerList
        restOfferMockMvc.perform(get("/v1/offers"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].offer_code").value(hasItem(DEFAULT_OFFER_CODE.toString())))
            .andExpect(jsonPath("$.[*].product_code").value(hasItem(DEFAULT_PRODUCT_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].offer_price").value(hasItem(DEFAULT_OFFER_PRICE.intValue())))

            .andExpect(jsonPath("$.[*].start_date[0]").value(DEFAULT_START_DATE.getYear()))
            .andExpect(jsonPath("$.[*].start_date[1]").value(DEFAULT_START_DATE.getMonthValue()))
            .andExpect(jsonPath("$.[*].start_date[2]").value(DEFAULT_START_DATE.getDayOfMonth()))
            .andExpect(jsonPath("$.[*].end_date[0]").value(DEFAULT_END_DATE.getYear()))
            .andExpect(jsonPath("$.[*].end_date[1]").value(DEFAULT_END_DATE.getMonthValue()))
            .andExpect(jsonPath("$.[*].end_date[2]").value(DEFAULT_END_DATE.getDayOfMonth()))
            
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getOffer() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offerMapper.toEntity(offerDTO));

        // Get the offer
        restOfferMockMvc.perform(get("/v1/offers/{offerCode}", offerDTO.getOfferCode()))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
            .andExpect(jsonPath("$.offer_code").value(DEFAULT_OFFER_CODE.toString()))
            .andExpect(jsonPath("$.product_code").value(DEFAULT_PRODUCT_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.offer_price").value(DEFAULT_OFFER_PRICE.intValue()))
            
            .andExpect(jsonPath("$.start_date[0]").value(DEFAULT_START_DATE.getYear()))
            .andExpect(jsonPath("$.start_date[1]").value(DEFAULT_START_DATE.getMonthValue()))
            .andExpect(jsonPath("$.start_date[2]").value(DEFAULT_START_DATE.getDayOfMonth()))
            .andExpect(jsonPath("$.end_date[0]").value(DEFAULT_END_DATE.getYear()))
            .andExpect(jsonPath("$.end_date[1]").value(DEFAULT_END_DATE.getMonthValue()))
            .andExpect(jsonPath("$.end_date[2]").value(DEFAULT_END_DATE.getDayOfMonth()))
            
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }
    
    @Test
    @Transactional
    public void getNonExistingOffer() throws Exception {
        // Get the offer
        restOfferMockMvc.perform(get("/v1/offers/{offerCode}", DEFAULT_OFFER_CODE))
            .andExpect(status().isNotFound());
    }
    
    @Test
    @Transactional
    public void deleteExistingOffer() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offerMapper.toEntity(offerDTO));

        int databaseSizeBeforeDelete = offerRepository.findAllActive().size();

        // Delete the offer
        restOfferMockMvc.perform(delete("/v1/offers/{offerCode}", offerDTO.getOfferCode())
            .accept(APPLICATION_JSON))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Offer> offerList = offerRepository.findAllActive();
        assertThat(offerList).hasSize(databaseSizeBeforeDelete - 1);
    }
    
    @Test
    @Transactional
    public void deleteNonExistingOffer() throws Exception {
        // Delete the offer
        restOfferMockMvc.perform(delete("/v1/offers/{offerCode}", "not_exist")
            .accept(APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}
