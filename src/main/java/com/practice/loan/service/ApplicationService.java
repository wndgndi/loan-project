package com.practice.loan.service;

import com.practice.loan.dto.ApplicationDTO.Request;
import com.practice.loan.dto.ApplicationDTO.Response;

public interface ApplicationService {

    Response create(Request request);

    Response get(Long applicationId);

    Response update(Long applicationId, Request request);

    void delete(Long applicationId);
}
