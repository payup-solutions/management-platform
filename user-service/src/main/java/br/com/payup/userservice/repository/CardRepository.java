package br.com.payup.userservice.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.payup.userservice.domain.Card;


/**
 * Spring Data JPA repository for the Card entity.
 */
@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

	Card findByActiveTrueAndConsumerId(Long consumerId);

	Collection<Card> findByConsumerId(Long consumerId);

}
