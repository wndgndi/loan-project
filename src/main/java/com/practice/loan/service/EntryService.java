package com.practice.loan.service;

import com.practice.loan.dto.EntryDTO.Request;
import com.practice.loan.dto.EntryDTO.Response;
import com.practice.loan.dto.EntryDTO.UpdateResponse;

public interface EntryService {

   Response create(Long applicationId, Request request);

   Response get(Long applicationId);

   UpdateResponse update(Long entryId, Request request);

   void delete(Long entryId);
}
