package com.practice.loan.service;

import com.practice.loan.dto.TermsDTO.Request;
import com.practice.loan.dto.TermsDTO.Response;
import java.util.List;

public interface TermsService {

    Response create(Request request);

    List<Response> getAll();
}
