package com.morgan.andy.repository;

import com.morgan.andy.domain.PersistAutomaton;
import com.morgan.andy.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface FiniteAutomatonRepository extends JpaRepository<PersistAutomaton, Long> {

    List<PersistAutomaton> findByUserid(long userid);

}
