package com.practice.loan.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.practice.loan.domain.Terms;
import com.practice.loan.dto.TermsDTO.Request;
import com.practice.loan.dto.TermsDTO.Response;
import com.practice.loan.repository.TermsRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
public class TermsServiceTest {

    @InjectMocks
    TermsServiceImpl termsService;

    @Mock
    private TermsRepository termsRepository;

    @Spy
    private ModelMapper modelMapper;

    @Test
    void Should_ReturnResponseOfNewTermsEntity_When_RequestTerms() {
        Terms entity = Terms.builder()
            .name("대출 이용 약관")
            .termsDetailUrl("https:abc-sotrage.acc/djqtsma")
            .build();


        Request request = Request.builder()
                .name("대출 이용 약관")
                .termsDetailUrl("https:abc-sotrage.acc/djqtsma")
                .build();

        when(termsRepository.save(ArgumentMatchers.any(Terms.class))).thenReturn(entity);

        Response actual = termsService.create(request);
        assertThat(actual.getName()).isSameAs(entity.getName());
        assertThat(actual.getTermsDetailUrl()).isSameAs(entity.getTermsDetailUrl());
    }

    @Test
    void Should_ReturnAllResponseOfExistTermsEntities_When_RequestTermsList() {
        Terms entityA = Terms.builder()
            .name("대출 이용약관 1")
            .termsDetailUrl("https://djqtsmsfldzm.asdfa/rewqw")
            .build();

        Terms entityB = Terms.builder()
            .name("대출 이용약관 2")
            .termsDetailUrl("https://xvbcxbldzm.aewrqdfa/rewqw")
            .build();

        List<Terms> list = new ArrayList<>(Arrays.asList(entityA, entityB));

        when(termsRepository.findAll()).thenReturn(Arrays.asList(entityA, entityB));

        List<Response> actual = termsService.getAll();

        assertThat(actual.size()).isSameAs(list.size());
    }

}
