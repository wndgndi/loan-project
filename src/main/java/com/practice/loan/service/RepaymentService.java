package com.practice.loan.service;

import com.practice.loan.dto.RepaymentDTO.ListResponse;
import com.practice.loan.dto.RepaymentDTO.Request;
import com.practice.loan.dto.RepaymentDTO.Response;
import com.practice.loan.dto.RepaymentDTO.UpdateResponse;
import java.util.List;

public interface RepaymentService {

    Response create(Long applicationId, Request request);

    List<ListResponse> get(Long applicationId);

    UpdateResponse update(Long repaymentId, Request request);

    void delete(Long repaymentId);
}
