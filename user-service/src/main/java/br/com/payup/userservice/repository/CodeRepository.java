package br.com.payup.userservice.repository;

import br.com.payup.userservice.domain.Code;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Code entity.
 */
@Repository
public interface CodeRepository extends JpaRepository<Code, Long> {

	Boolean existsByValue(String code);

	Code findByConsumerIdAndActiveTrue(Long id);

}
