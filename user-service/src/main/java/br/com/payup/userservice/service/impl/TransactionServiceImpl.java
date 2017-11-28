package br.com.payup.userservice.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.payup.userservice.repository.TransactionRepository;
import br.com.payup.userservice.service.TransactionService;
import br.com.payup.userservice.service.dto.TransactionDTO;
import br.com.payup.userservice.service.mapper.TransactionMapper;

/**
 * Service Implementation for managing Transaction.
 */
@Service
@Transactional
public class TransactionServiceImpl implements TransactionService{

    private final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;

    private final TransactionMapper transactionMapper;

    public TransactionServiceImpl(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    /**
     *  Get all the transactions.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> findAll() {
        log.debug("Request to get all Transactions");
        //TODO USE LOGGED CONSUMER ID
        //TODO IMPLEMENT PAGINATION
        return transactionRepository.findByConsumerIdOrderByTimestampDesc(1L).stream()
            .map(transactionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

}
