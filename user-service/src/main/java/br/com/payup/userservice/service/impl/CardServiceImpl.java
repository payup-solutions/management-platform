package br.com.payup.userservice.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.payup.userservice.domain.Card;
import br.com.payup.userservice.domain.Consumer;
import br.com.payup.userservice.repository.CardRepository;
import br.com.payup.userservice.repository.ConsumerRepository;
import br.com.payup.userservice.service.CardService;
import br.com.payup.userservice.service.dto.CardDTO;
import br.com.payup.userservice.service.mapper.CardMapper;

/**
 * Service Implementation for managing Card.
 */
@Service
@Transactional
public class CardServiceImpl implements CardService{

    private final Logger log = LoggerFactory.getLogger(CardServiceImpl.class);

    private final CardRepository cardRepository;

    private final CardMapper cardMapper;

    private final ConsumerRepository consumerRepository;
    
    public CardServiceImpl(CardRepository cardRepository, CardMapper cardMapper, ConsumerRepository consumerRepository) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
        this.consumerRepository = consumerRepository;
    }

    /**
     * Save a card.
     *
     * @param cardDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CardDTO save(CardDTO cardDTO) {
        log.debug("Request to save Card : {}", cardDTO);
        Card card = cardMapper.toEntity(cardDTO);
        preProccessSave(card);
        card = cardRepository.save(card);
        return cardMapper.toDto(card);
    }

    private void preProccessSave(Card card) {
		validateCard(card);
		setConsumer(card);
	}

	private void setConsumer(Card card) {
		// TODO Auto-generated method stub
		// GET CONSUMER FROM LOGGED USER
		Consumer consumer = new Consumer();
		consumer.setName("Jota");
		consumerRepository.save(consumer);
		card.setConsumer(consumer);
	}

	private void validateCard(Card card) {
		// TODO Auto-generated method stub
		// PUT CARD VALIDATIONS AND THROW EXCEPTION
	}

	/**
     *  Get all the cards.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<CardDTO> findAll() {
        log.debug("Request to get all Cards");
        return cardRepository.findAll().stream()
            .map(cardMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one card by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CardDTO findOne(Long id) {
        log.debug("Request to get Card : {}", id);
        Card card = cardRepository.findOne(id);
        return cardMapper.toDto(card);
    }

    /**
     *  Delete the  card by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Card : {}", id);
        cardRepository.delete(id);
    }
}
