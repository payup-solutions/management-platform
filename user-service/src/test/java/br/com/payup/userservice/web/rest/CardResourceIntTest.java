package br.com.payup.userservice.web.rest;

import br.com.payup.userservice.UserserviceApp;

import br.com.payup.userservice.domain.Card;
import br.com.payup.userservice.domain.Consumer;
import br.com.payup.userservice.repository.CardRepository;
import br.com.payup.userservice.service.CardService;
import br.com.payup.userservice.service.dto.CardDTO;
import br.com.payup.userservice.service.mapper.CardMapper;
import br.com.payup.userservice.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CardResource REST controller.
 *
 * @see CardResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserserviceApp.class)
public class CardResourceIntTest {

    private static final String DEFAULT_NUMBER = "AAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBBBBBBBBBBB";

    private static final String DEFAULT_EXPIRATION_DATE = "AAAAA";
    private static final String UPDATED_EXPIRATION_DATE = "BBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CVV = "AAA";
    private static final String UPDATED_CVV = "BBB";

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private CardService cardService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCardMockMvc;

    private Card card;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CardResource cardResource = new CardResource(cardService);
        this.restCardMockMvc = MockMvcBuilders.standaloneSetup(cardResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Card createEntity(EntityManager em) {
        Card card = new Card()
            .number(DEFAULT_NUMBER)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .name(DEFAULT_NAME)
            .cvv(DEFAULT_CVV);
        // Add required entity
        Consumer consumer = ConsumerResourceIntTest.createEntity(em);
        em.persist(consumer);
        em.flush();
        card.setConsumer(consumer);
        return card;
    }

    @Before
    public void initTest() {
        card = createEntity(em);
    }

    @Test
    @Transactional
    public void createCard() throws Exception {
        int databaseSizeBeforeCreate = cardRepository.findAll().size();

        // Create the Card
        CardDTO cardDTO = cardMapper.toDto(card);
        restCardMockMvc.perform(post("/api/cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cardDTO)))
            .andExpect(status().isCreated());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeCreate + 1);
        Card testCard = cardList.get(cardList.size() - 1);
        assertThat(testCard.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testCard.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testCard.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCard.getCvv()).isEqualTo(DEFAULT_CVV);
    }

    @Test
    @Transactional
    public void createCardWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cardRepository.findAll().size();

        // Create the Card with an existing ID
        card.setId(1L);
        CardDTO cardDTO = cardMapper.toDto(card);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCardMockMvc.perform(post("/api/cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cardDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardRepository.findAll().size();
        // set the field null
        card.setNumber(null);

        // Create the Card, which fails.
        CardDTO cardDTO = cardMapper.toDto(card);

        restCardMockMvc.perform(post("/api/cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cardDTO)))
            .andExpect(status().isBadRequest());

        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExpirationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardRepository.findAll().size();
        // set the field null
        card.setExpirationDate(null);

        // Create the Card, which fails.
        CardDTO cardDTO = cardMapper.toDto(card);

        restCardMockMvc.perform(post("/api/cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cardDTO)))
            .andExpect(status().isBadRequest());

        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardRepository.findAll().size();
        // set the field null
        card.setName(null);

        // Create the Card, which fails.
        CardDTO cardDTO = cardMapper.toDto(card);

        restCardMockMvc.perform(post("/api/cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cardDTO)))
            .andExpect(status().isBadRequest());

        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCvvIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardRepository.findAll().size();
        // set the field null
        card.setCvv(null);

        // Create the Card, which fails.
        CardDTO cardDTO = cardMapper.toDto(card);

        restCardMockMvc.perform(post("/api/cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cardDTO)))
            .andExpect(status().isBadRequest());

        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCards() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get all the cardList
        restCardMockMvc.perform(get("/api/cards?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(card.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].cvv").value(hasItem(DEFAULT_CVV.toString())));
    }

    @Test
    @Transactional
    public void getCard() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get the card
        restCardMockMvc.perform(get("/api/cards/{id}", card.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(card.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.toString()))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.cvv").value(DEFAULT_CVV.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCard() throws Exception {
        // Get the card
        restCardMockMvc.perform(get("/api/cards/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCard() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);
        int databaseSizeBeforeUpdate = cardRepository.findAll().size();

        // Update the card
        Card updatedCard = cardRepository.findOne(card.getId());
        updatedCard
            .number(UPDATED_NUMBER)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .name(UPDATED_NAME)
            .cvv(UPDATED_CVV);
        CardDTO cardDTO = cardMapper.toDto(updatedCard);

        restCardMockMvc.perform(put("/api/cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cardDTO)))
            .andExpect(status().isOk());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
        Card testCard = cardList.get(cardList.size() - 1);
        assertThat(testCard.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testCard.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testCard.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCard.getCvv()).isEqualTo(UPDATED_CVV);
    }

    @Test
    @Transactional
    public void updateNonExistingCard() throws Exception {
        int databaseSizeBeforeUpdate = cardRepository.findAll().size();

        // Create the Card
        CardDTO cardDTO = cardMapper.toDto(card);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCardMockMvc.perform(put("/api/cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cardDTO)))
            .andExpect(status().isCreated());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCard() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);
        int databaseSizeBeforeDelete = cardRepository.findAll().size();

        // Get the card
        restCardMockMvc.perform(delete("/api/cards/{id}", card.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Card.class);
        Card card1 = new Card();
        card1.setId(1L);
        Card card2 = new Card();
        card2.setId(card1.getId());
        assertThat(card1).isEqualTo(card2);
        card2.setId(2L);
        assertThat(card1).isNotEqualTo(card2);
        card1.setId(null);
        assertThat(card1).isNotEqualTo(card2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CardDTO.class);
        CardDTO cardDTO1 = new CardDTO();
        cardDTO1.setId(1L);
        CardDTO cardDTO2 = new CardDTO();
        assertThat(cardDTO1).isNotEqualTo(cardDTO2);
        cardDTO2.setId(cardDTO1.getId());
        assertThat(cardDTO1).isEqualTo(cardDTO2);
        cardDTO2.setId(2L);
        assertThat(cardDTO1).isNotEqualTo(cardDTO2);
        cardDTO1.setId(null);
        assertThat(cardDTO1).isNotEqualTo(cardDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(cardMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(cardMapper.fromId(null)).isNull();
    }
}
