package com.practice.loan.service;

import com.practice.loan.dto.CounselDTO.Request;
import com.practice.loan.dto.CounselDTO.Response;

public interface CounselService {

    Response create(Request request);

    Response get(Long counselId);

    Response update(Long counselId, Request request);

    void delete(Long counselId);
}