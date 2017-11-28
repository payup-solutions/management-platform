package br.com.payup.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.payup.userservice.domain.Consumer;


/**
 * Spring Data JPA repository for the Consumer entity.
 */
@Repository
public interface ConsumerRepository extends JpaRepository<Consumer, Long> {

}
