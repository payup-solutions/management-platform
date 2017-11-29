package br.com.payup.userservice.service.impl;

import java.time.ZonedDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.payup.userservice.domain.Code;
import br.com.payup.userservice.domain.Consumer;
import br.com.payup.userservice.exception.ExistingCodeException;
import br.com.payup.userservice.exception.InactiveCodeException;
import br.com.payup.userservice.exception.NoActiveCodeException;
import br.com.payup.userservice.repository.CodeRepository;
import br.com.payup.userservice.repository.ConsumerRepository;
import br.com.payup.userservice.service.CodeService;
import br.com.payup.userservice.service.dto.CodeDTO;
import br.com.payup.userservice.service.mapper.CodeMapper;

/**
 * Service Implementation for managing Code.
 */
@Service
@Transactional
public class CodeServiceImpl implements CodeService{

	private final static int MAX_HOURS_CODE_DURATION = 8; 
	
    private final Logger log = LoggerFactory.getLogger(CodeServiceImpl.class);

    private final CodeRepository codeRepository;

    private final CodeMapper codeMapper;
    
    private final ConsumerRepository consumerRepository;

    public CodeServiceImpl(CodeRepository codeRepository, CodeMapper codeMapper, ConsumerRepository consumerRepository) {
        this.codeRepository = codeRepository;
        this.codeMapper = codeMapper;
        this.consumerRepository = consumerRepository;
    }

    /**
     * Activate a code.
     *
     * @param code value to activate 
     * @return the persisted entity
     * @throws ExistingCodeException 
     */
    @Override
    public CodeDTO activateCode(String code) throws ExistingCodeException {
        log.debug("Request to activate Code : {}", code);
        validateUniqueCode(code);
        Code builtCode = buildCode(code);
        builtCode = codeRepository.save(builtCode);
        return codeMapper.toDto(builtCode);
    }
    
    private void validateUniqueCode(String code) throws ExistingCodeException {
		Boolean codeExists = codeRepository.existsByValue(code);
		if (codeExists) {
			throw new ExistingCodeException();
		}
		
	}

	private Code buildCode(String code) {
    	// TODO GET LOGGED CONSUMER
    	List<Consumer> consumers = consumerRepository.findAll();
    	
    	Code builtCode = new Code().active(true).activationDate(ZonedDateTime.now()).value(code).consumer(consumers.get(0));
    	builtCode.expirationDate(builtCode.getActivationDate().plusHours(MAX_HOURS_CODE_DURATION));
    	return builtCode;
    }

    /**
     *  Deactivate one code by id.
     *
     *  @param id the id of the entity
     * @throws InactiveCodeException 
     */
    @Override
    @Transactional(readOnly = true)
    public void deactivateCode(Long id) throws InactiveCodeException {
        log.debug("Request to deactivate Code : {}", id);
        
        Code code = codeRepository.findOne(id);
        validateConsumer(code);
        validateActive(code);
        
        code.setActive(false);
        codeRepository.save(code);
        
    }
    
    private void validateActive(Code code) throws InactiveCodeException {
		if (code.isActive().equals(false)) {
			throw new InactiveCodeException();
		}
	}

	private void validateConsumer(Code code) {
		// TODO VALIDATE IF CONSUMER IS LOGGED CONSUMER
		// AND THROW EXCEPTIONS
	}

	/**
     * Get the consumer current code
     * 
     * @return the current consumer codeDTO
	 * @throws NoActiveCodeException 
     */
	@Override
	public CodeDTO getConsumerCurrentCode() throws NoActiveCodeException {
		// TODO GET LOGGED CONSUMER
    	List<Consumer> consumers = consumerRepository.findAll();
    	
    	Code code = codeRepository.findByConsumerIdAndActiveTrue(consumers.get(0).getId());
    	
    	if (code == null) {
    		throw new NoActiveCodeException();
    	}
		// TODO Auto-generated method stub
		return codeMapper.toDto(code);
	}

}
