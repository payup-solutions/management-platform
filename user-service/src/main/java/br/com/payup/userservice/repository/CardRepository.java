package br.com.payup.userservice.repository;

import br.com.payup.userservice.domain.Card;
import br.com.payup.userservice.domain.Consumer;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Card entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

	Card findByActiveTrueAndConsumer(Consumer consumer);

}
