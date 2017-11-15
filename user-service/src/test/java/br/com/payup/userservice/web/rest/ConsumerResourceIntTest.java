package br.com.payup.userservice.web.rest;

import br.com.payup.userservice.UserserviceApp;

import br.com.payup.userservice.domain.Consumer;
import br.com.payup.userservice.repository.ConsumerRepository;
import br.com.payup.userservice.service.ConsumerService;
import br.com.payup.userservice.service.dto.ConsumerDTO;
import br.com.payup.userservice.service.mapper.ConsumerMapper;
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
 * Test class for the ConsumerResource REST controller.
 *
 * @see ConsumerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserserviceApp.class)
public class ConsumerResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private ConsumerMapper consumerMapper;

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restConsumerMockMvc;

    private Consumer consumer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConsumerResource consumerResource = new ConsumerResource(consumerService);
        this.restConsumerMockMvc = MockMvcBuilders.standaloneSetup(consumerResource)
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
    public static Consumer createEntity(EntityManager em) {
        Consumer consumer = new Consumer()
            .name(DEFAULT_NAME);
        return consumer;
    }

    @Before
    public void initTest() {
        consumer = createEntity(em);
    }

    @Test
    @Transactional
    public void createConsumer() throws Exception {
        int databaseSizeBeforeCreate = consumerRepository.findAll().size();

        // Create the Consumer
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);
        restConsumerMockMvc.perform(post("/api/consumers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(consumerDTO)))
            .andExpect(status().isCreated());

        // Validate the Consumer in the database
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeCreate + 1);
        Consumer testConsumer = consumerList.get(consumerList.size() - 1);
        assertThat(testConsumer.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createConsumerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = consumerRepository.findAll().size();

        // Create the Consumer with an existing ID
        consumer.setId(1L);
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConsumerMockMvc.perform(post("/api/consumers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(consumerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Consumer in the database
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = consumerRepository.findAll().size();
        // set the field null
        consumer.setName(null);

        // Create the Consumer, which fails.
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        restConsumerMockMvc.perform(post("/api/consumers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(consumerDTO)))
            .andExpect(status().isBadRequest());

        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllConsumers() throws Exception {
        // Initialize the database
        consumerRepository.saveAndFlush(consumer);

        // Get all the consumerList
        restConsumerMockMvc.perform(get("/api/consumers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consumer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getConsumer() throws Exception {
        // Initialize the database
        consumerRepository.saveAndFlush(consumer);

        // Get the consumer
        restConsumerMockMvc.perform(get("/api/consumers/{id}", consumer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(consumer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingConsumer() throws Exception {
        // Get the consumer
        restConsumerMockMvc.perform(get("/api/consumers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConsumer() throws Exception {
        // Initialize the database
        consumerRepository.saveAndFlush(consumer);
        int databaseSizeBeforeUpdate = consumerRepository.findAll().size();

        // Update the consumer
        Consumer updatedConsumer = consumerRepository.findOne(consumer.getId());
        updatedConsumer
            .name(UPDATED_NAME);
        ConsumerDTO consumerDTO = consumerMapper.toDto(updatedConsumer);

        restConsumerMockMvc.perform(put("/api/consumers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(consumerDTO)))
            .andExpect(status().isOk());

        // Validate the Consumer in the database
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeUpdate);
        Consumer testConsumer = consumerList.get(consumerList.size() - 1);
        assertThat(testConsumer.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingConsumer() throws Exception {
        int databaseSizeBeforeUpdate = consumerRepository.findAll().size();

        // Create the Consumer
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restConsumerMockMvc.perform(put("/api/consumers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(consumerDTO)))
            .andExpect(status().isCreated());

        // Validate the Consumer in the database
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteConsumer() throws Exception {
        // Initialize the database
        consumerRepository.saveAndFlush(consumer);
        int databaseSizeBeforeDelete = consumerRepository.findAll().size();

        // Get the consumer
        restConsumerMockMvc.perform(delete("/api/consumers/{id}", consumer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Consumer.class);
        Consumer consumer1 = new Consumer();
        consumer1.setId(1L);
        Consumer consumer2 = new Consumer();
        consumer2.setId(consumer1.getId());
        assertThat(consumer1).isEqualTo(consumer2);
        consumer2.setId(2L);
        assertThat(consumer1).isNotEqualTo(consumer2);
        consumer1.setId(null);
        assertThat(consumer1).isNotEqualTo(consumer2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConsumerDTO.class);
        ConsumerDTO consumerDTO1 = new ConsumerDTO();
        consumerDTO1.setId(1L);
        ConsumerDTO consumerDTO2 = new ConsumerDTO();
        assertThat(consumerDTO1).isNotEqualTo(consumerDTO2);
        consumerDTO2.setId(consumerDTO1.getId());
        assertThat(consumerDTO1).isEqualTo(consumerDTO2);
        consumerDTO2.setId(2L);
        assertThat(consumerDTO1).isNotEqualTo(consumerDTO2);
        consumerDTO1.setId(null);
        assertThat(consumerDTO1).isNotEqualTo(consumerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(consumerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(consumerMapper.fromId(null)).isNull();
    }
}
