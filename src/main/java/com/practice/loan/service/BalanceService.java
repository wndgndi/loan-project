package com.practice.loan.service;

import com.practice.loan.dto.BalanceDTO.CreateRequest;
import com.practice.loan.dto.BalanceDTO.RepaymentRequest;
import com.practice.loan.dto.BalanceDTO.Response;
import com.practice.loan.dto.BalanceDTO.UpdateRequest;

public interface BalanceService {

    Response create(Long applicationId, CreateRequest request);

    Response get(Long applicationId);

    Response update(Long applicationId, UpdateRequest request);

    Response repaymentUpdate(Long applicationId, RepaymentRequest request);

    void delete(Long applicationId);
}
