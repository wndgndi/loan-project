package com.practice.loan.repository;

import com.practice.loan.domain.Repayment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepaymentRepository extends JpaRepository<Repayment, Long> {

    List<Repayment> findAllByApplicationId(Long applicationId);
}
