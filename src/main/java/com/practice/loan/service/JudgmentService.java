package com.practice.loan.service;

import com.practice.loan.dto.ApplicationDTO.GrantAmount;
import com.practice.loan.dto.JudgmentDTO.Request;
import com.practice.loan.dto.JudgmentDTO.Response;

public interface JudgmentService {

    Response create(Request request);

    Response get(Long judgementId);

    Response getJudgementOfApplication(Long applicationId);

    Response update(Long judgmentId, Request request);

    void delete(Long judgmentId);

    GrantAmount grant(Long judgmentId);
}
