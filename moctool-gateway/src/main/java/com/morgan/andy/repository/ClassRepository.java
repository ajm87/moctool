package com.morgan.andy.repository;

import com.morgan.andy.domain.Achievement;
import com.morgan.andy.domain.HWClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface ClassRepository extends JpaRepository<HWClass, Long> {


}
