package com.morgan.andy.repository;

import com.morgan.andy.domain.HomeworkQuestions;
import com.morgan.andy.domain.HomeworkStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface HomeworkStatusRepository extends JpaRepository<HomeworkStatus, Long> {

    public Set<HomeworkStatus> getAllByUserId(Long userId);

}
