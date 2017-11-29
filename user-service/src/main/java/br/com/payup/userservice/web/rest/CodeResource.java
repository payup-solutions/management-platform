package br.com.payup.userservice.web.rest;

import java.net.URISyntaxException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import br.com.payup.userservice.exception.ExistingCodeException;
import br.com.payup.userservice.exception.InactiveCodeException;
import br.com.payup.userservice.exception.NoActiveCodeException;
import br.com.payup.userservice.service.CodeService;
import br.com.payup.userservice.service.dto.CodeDTO;
import br.com.payup.userservice.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Code.
 */
@RestController
@RequestMapping("/api")
public class CodeResource {

    private final Logger log = LoggerFactory.getLogger(CodeResource.class);

    private static final String ENTITY_NAME = "code";

    private final CodeService codeService;

    public CodeResource(CodeService codeService) {
        this.codeService = codeService;
    }

    /**
     * GET  /codes/activate : Activate a new code.
     *
     * @param codeDTO the codeDTO to create
     * @return the ResponseEntity with status 200 (Created) and with body the new codeDTO
     */
    @GetMapping("/codes/activate/{code}")
    @Timed
    public ResponseEntity<CodeDTO> activateCode(@Valid @PathVariable String code) throws URISyntaxException {
        log.debug("REST request to activate Code : {}", code);
        CodeDTO result;
		try {
			result = codeService.activateCode(code);
		} catch (ExistingCodeException e) {
			return ResponseEntity.badRequest()
	                .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, e.getCode(), e.getMessage())).body(null);
		}
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /codes/deactivate/:id : get the "id" code.
     *
     * @param id the id of the codeDTO to deactivate
     * @return the ResponseEntity with status 200 (OK) and with body the codeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/codes/deactivate/{id}")
    @Timed
    public ResponseEntity<Boolean> deactivateCode(@PathVariable Long id) {
        log.debug("REST request to deactivate Code : {}", id);
        try {
			codeService.deactivateCode(id);
        } catch (InactiveCodeException e) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, e.getCode(), e.getMessage())).body(null);
		}
        return ResponseEntity.ok()
                .body(true);
    }
    
    /**
     * GET  /codes : get the consumer current code.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the codeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/codes")
    @Timed
    public ResponseEntity<CodeDTO> getConsumerCurrentCode() {
        log.debug("REST request to get consumer current Code }");
        CodeDTO result;
		try {
			result = codeService.getConsumerCurrentCode();
		} catch (NoActiveCodeException e) {
			return ResponseEntity.badRequest()
	                .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, e.getCode(), e.getMessage())).body(null);
		}
        return ResponseEntity.ok()
                .body(result);
    }

}
