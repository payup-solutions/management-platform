package br.com.payup.userservice.service.mapper;

import br.com.payup.userservice.domain.*;
import br.com.payup.userservice.service.dto.ConsumerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Consumer and its DTO ConsumerDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConsumerMapper extends EntityMapper <ConsumerDTO, Consumer> {
    
    
    default Consumer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Consumer consumer = new Consumer();
        consumer.setId(id);
        return consumer;
    }
}
