package br.com.payup.userservice.service;

import br.com.payup.userservice.service.dto.TransactionDTO;
import java.util.List;

/**
 * Service Interface for managing Transaction.
 */
public interface TransactionService {

    /**
     *  Get all the transactions.
     *
     *  @return the list of entities
     */
    List<TransactionDTO> findAll();

}
