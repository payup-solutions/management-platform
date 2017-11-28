package br.com.payup.userservice.service.mapper;

import br.com.payup.userservice.domain.*;
import br.com.payup.userservice.service.dto.TransactionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Transaction and its DTO TransactionDTO.
 */
@Mapper(componentModel = "spring", uses = {ConsumerMapper.class, TransactionTypeMapper.class, })
public interface TransactionMapper extends EntityMapper <TransactionDTO, Transaction> {

    TransactionDTO toDto(Transaction transaction); 

    Transaction toEntity(TransactionDTO transactionDTO); 
    default Transaction fromId(Long id) {
        if (id == null) {
            return null;
        }
        Transaction transaction = new Transaction();
        transaction.setId(id);
        return transaction;
    }
}
