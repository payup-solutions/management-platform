package br.com.payup.userservice.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.payup.userservice.service.CardService;
import br.com.payup.userservice.web.rest.util.HeaderUtil;
import br.com.payup.userservice.service.dto.CardDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Card.
 */
@RestController
@RequestMapping("/api")
public class CardResource {

    private final Logger log = LoggerFactory.getLogger(CardResource.class);

    private static final String ENTITY_NAME = "card";

    private final CardService cardService;

    public CardResource(CardService cardService) {
        this.cardService = cardService;
    }

    /**
     * POST  /cards : Create a new card.
     *
     * @param cardDTO the cardDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cardDTO, or with status 400 (Bad Request) if the card has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cards")
    @Timed
    public ResponseEntity<CardDTO> createCard(@Valid @RequestBody CardDTO cardDTO) throws URISyntaxException {
        log.debug("REST request to save Card : {}", cardDTO);
        CardDTO result = cardService.save(cardDTO);
        return ResponseEntity.created(new URI("/api/cards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cards : get all the cards.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cards in body
     */
    @GetMapping("/cards")
    @Timed
    public List<CardDTO> getAllCards() {
        log.debug("REST request to get all Cards");
        return cardService.findAll();
        }

    /**
     * GET  /cards/:id : get the "id" card.
     *
     * @param id the id of the cardDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cardDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cards/{id}")
    @Timed
    public ResponseEntity<CardDTO> getCard(@PathVariable Long id) {
        log.debug("REST request to get Card : {}", id);
        CardDTO cardDTO = cardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cardDTO));
    }

    /**
     * DELETE  /cards/:id : delete the "id" card.
     *
     * @param id the id of the cardDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cards/{id}")
    @Timed
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        log.debug("REST request to delete Card : {}", id);
        cardService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    /**
     * GET  /cards/activate/:id : activate the "id" card.
     *
     * @param id the id of the cardDTO to activate
     * @return the ResponseEntity with status 200 (OK) and with body the cardDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cards/activate/{id}")
    @Timed
    public ResponseEntity<CardDTO> activateCard(@PathVariable Long id) {
        log.debug("REST request to get Card : {}", id);
        CardDTO cardDTO = cardService.activateCard(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cardDTO));
    }
}
