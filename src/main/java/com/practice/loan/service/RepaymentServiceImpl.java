package com.practice.loan.service;

import com.practice.loan.domain.Application;
import com.practice.loan.domain.Entry;
import com.practice.loan.domain.Repayment;
import com.practice.loan.dto.BalanceDTO;
import com.practice.loan.dto.BalanceDTO.RepaymentRequest.RepaymentType;
import com.practice.loan.dto.RepaymentDTO.ListResponse;
import com.practice.loan.dto.RepaymentDTO.Request;
import com.practice.loan.dto.RepaymentDTO.Response;
import com.practice.loan.dto.RepaymentDTO.UpdateResponse;
import com.practice.loan.exception.BaseException;
import com.practice.loan.exception.ResultType;
import com.practice.loan.repository.ApplicationRepository;
import com.practice.loan.repository.EntryRepository;
import com.practice.loan.repository.RepaymentRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RepaymentServiceImpl implements RepaymentService {

    private final RepaymentRepository repaymentRepository;

    private final ModelMapper modelMapper;

    private final EntryRepository entryRepository;

    private final BalanceService balanceService;

    private final ApplicationRepository applicationRepository;

    @Override
    public Response create(Long applicationId, Request request) {
        if (!isRepayableApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        Repayment repayment = modelMapper.map(request, Repayment.class);
        repayment.setApplicationId(applicationId);

        repaymentRepository.save(repayment);

        BalanceDTO.Response updatedBalance = balanceService.repaymentUpdate(applicationId,
            BalanceDTO.RepaymentRequest.builder()
                .repaymentAmount(request.getRepaymentAmount())
                .type(RepaymentType.REMOVE)
                .build());

        Response response = modelMapper.map(repayment, Response.class);
        response.setBalance(updatedBalance.getBalance());
        response.setGuideMessage(updatedBalance.getGuideMessage());

        return response;
    }

    @Override
    public List<ListResponse> get(Long applicationId) {
        List<Repayment> repayments = repaymentRepository.findAllByApplicationId(applicationId);

        return repayments.stream().map(r -> modelMapper.map(r, ListResponse.class)).collect(
            Collectors.toList());
    }

    @Override
    public UpdateResponse update(Long repaymentId, Request request) {
        Repayment repayment = repaymentRepository.findById(repaymentId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        Long applicationId = repayment.getApplicationId();
        BigDecimal beforeRepaymentAmount = repayment.getRepaymentAmount();

        balanceService.repaymentUpdate(applicationId,
            BalanceDTO.RepaymentRequest.builder()
                .repaymentAmount(beforeRepaymentAmount)
                .type(RepaymentType.ADD)
                .build()
        );

        repayment.setRepaymentAmount(request.getRepaymentAmount());
        repaymentRepository.save(repayment);

        BalanceDTO.Response updateBalance = balanceService.repaymentUpdate(applicationId,
            BalanceDTO.RepaymentRequest.builder()
            .repaymentAmount(request.getRepaymentAmount())
            .type(RepaymentType.REMOVE).build()
        );

        return UpdateResponse.builder()
            .applicationId(applicationId)
            .beforeRepaymentAmount(beforeRepaymentAmount)
            .afterRepaymentAmount(request.getRepaymentAmount())
            .balance(updateBalance.getBalance())
            .createdAt(repayment.getCreatedAt())
            .updatedAt(repayment.getUpdatedAt())
            .build();
    }

    @Override
    public void delete(Long repaymentId) {
        Repayment repayment = repaymentRepository.findById(repaymentId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        Long applicationId = repayment.getApplicationId();
        BigDecimal removeRepaymentAmount = repayment.getRepaymentAmount();

        balanceService.repaymentUpdate(applicationId,
            BalanceDTO.RepaymentRequest.builder()
                .repaymentAmount(removeRepaymentAmount)
                .type(RepaymentType.ADD)
                .build());

        repayment.setIsDeleted(true);
        repaymentRepository.save(repayment);
    }

    private boolean isRepayableApplication(Long applicationId) {
        Optional<Application> existedApplication = applicationRepository.findById(applicationId);
        if (existedApplication.isEmpty()) {
            return false;
        }

        if (existedApplication.get().getContractedAt() == null) {
            return false;
        }

        Optional<Entry> existedEntry = entryRepository.findByApplicationId(applicationId);
        return existedEntry.isPresent();
    }

}

