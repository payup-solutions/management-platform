package br.com.payup.userservice.service.mapper;

import br.com.payup.userservice.domain.*;
import br.com.payup.userservice.service.dto.TransactionTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TransactionType and its DTO TransactionTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TransactionTypeMapper extends EntityMapper <TransactionTypeDTO, TransactionType> {
    
    
    default TransactionType fromId(Long id) {
        if (id == null) {
            return null;
        }
        TransactionType transactionType = new TransactionType();
        transactionType.setId(id);
        return transactionType;
    }
}
