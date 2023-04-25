package com.practice.loan.service;

import com.practice.loan.dto.BalanceDTO.Request;
import com.practice.loan.dto.BalanceDTO.Response;
import com.practice.loan.dto.BalanceDTO.UpdateRequest;

public interface BalanceService {

    Response create(Long applicationId, Request request);

    Response update(Long applicationId, UpdateRequest request);
}
