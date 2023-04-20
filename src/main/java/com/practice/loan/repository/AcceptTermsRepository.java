package com.practice.loan.repository;

import com.practice.loan.domain.AcceptTerms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcceptTermsRepository extends JpaRepository<AcceptTerms, Long> {

}
