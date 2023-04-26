package com.practice.loan.service;

import com.practice.loan.domain.Balance;
import com.practice.loan.dto.BalanceDTO.CreateRequest;
import com.practice.loan.dto.BalanceDTO.RepaymentRequest;
import com.practice.loan.dto.BalanceDTO.RepaymentRequest.RepaymentType;
import com.practice.loan.dto.BalanceDTO.Response;
import com.practice.loan.dto.BalanceDTO.UpdateRequest;
import com.practice.loan.exception.BaseException;
import com.practice.loan.exception.ResultType;
import com.practice.loan.repository.BalanceRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepository balanceRepository;

    private final ModelMapper modelMapper;

    @Override
    public Response create(Long applicationId, CreateRequest request) {
        Balance balance = modelMapper.map(request, Balance.class);

        // 첫 생성은 entry amount 를 balance
        BigDecimal entryAmount = request.getEntryAmount();
        balance.setApplicationId(applicationId);
        balance.setBalance(entryAmount);

        balanceRepository.findByApplicationId(applicationId).ifPresent(b -> {
            balance.setBalanceId(b.getBalanceId());
            balance.setIsDeleted(b.getIsDeleted());
            balance.setCreatedAt(b.getCreatedAt());
            balance.setUpdatedAt(LocalDateTime.now());
        });

        Balance saved = balanceRepository.save(balance);

        return modelMapper.map(saved, Response.class);
    }

    @Override
    public Response get(Long applicationId) {
        Balance balance = balanceRepository.findById(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        return modelMapper.map(balance, Response.class);
    }

    @Override
    public Response update(Long applicationId, UpdateRequest request) {
        Balance balance = balanceRepository.findByApplicationId(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        BigDecimal beforeEntryAmount = request.getBeforeEntryAmount();
        BigDecimal afterEntryAmount = request.getAfterEntryAmount();
        BigDecimal updatedBalance = balance.getBalance();
        updatedBalance = updatedBalance.subtract(beforeEntryAmount).add(afterEntryAmount);

        balance.setBalance(updatedBalance);

        Balance updated = balanceRepository.save(balance);

        return modelMapper.map(updated, Response.class);
    }

    @Override
    public Response repaymentUpdate(Long applicationId, RepaymentRequest request) {
        Balance balance = balanceRepository.findByApplicationId(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        BigDecimal updatedBalance = balance.getBalance();
        BigDecimal repaymentAmount = request.getRepaymentAmount();

        if (request.getType().equals(RepaymentType.ADD)) {
            updatedBalance = updatedBalance.add(repaymentAmount);
        } else {
            if(updatedBalance.signum() == 0) {
                throw new BaseException(ResultType.ALREADY_PAID);
            }
            updatedBalance = updatedBalance.subtract(repaymentAmount);
        }

        if(updatedBalance.signum() < 0) {
            balance.setBalance(BigDecimal.ZERO);
        } else {
            balance.setBalance(updatedBalance);
        }

        Balance updated = balanceRepository.save(balance);
        Response response = modelMapper.map(updated, Response.class);

        if(updatedBalance.signum() < 0) {
            updatedBalance = updatedBalance.abs();
            response.setGuideMessage("모든 금액을 상환하였으며, 차액 " + updatedBalance + "원은 계좌로 입금됩니다.");
        } else {
            response.setGuideMessage("상환 금액은 " + repaymentAmount + "원이며, 남은 금액은 " + updatedBalance + "원 입니다.");
        }

        return response;
    }

    @Override
    public void delete(Long applicationId) {
        Balance balance = balanceRepository.findByApplicationId(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        balance.setIsDeleted(true);

        balanceRepository.save(balance);
    }
}
