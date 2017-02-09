package com.morgan.andy.repository;

import com.morgan.andy.domain.HWClass;
import com.morgan.andy.domain.QuestionsRef;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface QuestionsRefRepository extends JpaRepository<QuestionsRef, Long> {


}
