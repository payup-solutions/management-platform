package br.com.payup.userservice.service;

import br.com.payup.userservice.exception.ExistingCodeException;
import br.com.payup.userservice.exception.InactiveCodeException;
import br.com.payup.userservice.exception.NoActiveCodeException;
import br.com.payup.userservice.service.dto.CodeDTO;

/**
 * Service Interface for managing Code.
 */
public interface CodeService {

    /**
     * Activate a code.
     *
     * @param code value to activate 
     * @return the persisted entity
     * @throws ExistingCodeException 
     */
    CodeDTO activateCode(String code) throws ExistingCodeException;

    /**
     *  Deactivate the "id" code.
     *
     *  @param id the id of the entity
     * @throws InactiveCodeException 
     */
    void deactivateCode(Long id) throws InactiveCodeException;

    /**
     * Get the consumer current code
     * 
     * @return the current consumer codeDTO
     * @throws NoActiveCodeException 
     */
	CodeDTO getConsumerCurrentCode() throws NoActiveCodeException;

}
