package com.morgan.andy.repository;

import com.morgan.andy.domain.Achievement;
import com.morgan.andy.domain.PersistAutomaton;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    Optional<Achievement> findOneByAchievementKey(String achievementKey);

}
