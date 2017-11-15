package br.com.payup.userservice.service;

import br.com.payup.userservice.service.dto.ConsumerDTO;
import java.util.List;

/**
 * Service Interface for managing Consumer.
 */
public interface ConsumerService {

    /**
     * Save a consumer.
     *
     * @param consumerDTO the entity to save
     * @return the persisted entity
     */
    ConsumerDTO save(ConsumerDTO consumerDTO);

    /**
     *  Get all the consumers.
     *
     *  @return the list of entities
     */
    List<ConsumerDTO> findAll();

    /**
     *  Get the "id" consumer.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ConsumerDTO findOne(Long id);

    /**
     *  Delete the "id" consumer.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
