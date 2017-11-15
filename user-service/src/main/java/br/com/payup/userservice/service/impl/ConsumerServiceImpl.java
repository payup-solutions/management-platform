package br.com.payup.userservice.service.impl;

import br.com.payup.userservice.service.ConsumerService;
import br.com.payup.userservice.domain.Consumer;
import br.com.payup.userservice.repository.ConsumerRepository;
import br.com.payup.userservice.service.dto.ConsumerDTO;
import br.com.payup.userservice.service.mapper.ConsumerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Consumer.
 */
@Service
@Transactional
public class ConsumerServiceImpl implements ConsumerService{

    private final Logger log = LoggerFactory.getLogger(ConsumerServiceImpl.class);

    private final ConsumerRepository consumerRepository;

    private final ConsumerMapper consumerMapper;

    public ConsumerServiceImpl(ConsumerRepository consumerRepository, ConsumerMapper consumerMapper) {
        this.consumerRepository = consumerRepository;
        this.consumerMapper = consumerMapper;
    }

    /**
     * Save a consumer.
     *
     * @param consumerDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ConsumerDTO save(ConsumerDTO consumerDTO) {
        log.debug("Request to save Consumer : {}", consumerDTO);
        Consumer consumer = consumerMapper.toEntity(consumerDTO);
        consumer = consumerRepository.save(consumer);
        return consumerMapper.toDto(consumer);
    }

    /**
     *  Get all the consumers.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ConsumerDTO> findAll() {
        log.debug("Request to get all Consumers");
        return consumerRepository.findAll().stream()
            .map(consumerMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one consumer by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ConsumerDTO findOne(Long id) {
        log.debug("Request to get Consumer : {}", id);
        Consumer consumer = consumerRepository.findOne(id);
        return consumerMapper.toDto(consumer);
    }

    /**
     *  Delete the  consumer by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Consumer : {}", id);
        consumerRepository.delete(id);
    }
}
