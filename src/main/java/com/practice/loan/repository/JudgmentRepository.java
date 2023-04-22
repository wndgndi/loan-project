package com.practice.loan.repository;

import com.practice.loan.domain.Judgment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JudgmentRepository extends JpaRepository<Judgment, Long> {

    Optional<Judgment> findByApplicationId(Long applicationId);
}
