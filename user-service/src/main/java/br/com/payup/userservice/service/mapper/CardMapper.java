package br.com.payup.userservice.service.mapper;

import br.com.payup.userservice.domain.*;
import br.com.payup.userservice.service.dto.CardDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Card and its DTO CardDTO.
 */
@Mapper(componentModel = "spring", uses = {ConsumerMapper.class, })
public interface CardMapper extends EntityMapper <CardDTO, Card> {

    @Mapping(source = "consumer.id", target = "consumerId")
    @Mapping(source = "consumer.name", target = "consumerName")
    CardDTO toDto(Card card); 

    @Mapping(source = "consumerId", target = "consumer")
    Card toEntity(CardDTO cardDTO); 
    default Card fromId(Long id) {
        if (id == null) {
            return null;
        }
        Card card = new Card();
        card.setId(id);
        return card;
    }
}
