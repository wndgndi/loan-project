package com.practice.loan.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class TermsDTO implements Serializable {

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class Request {

        private String name;

        private String termsDetailUrl;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class Response {

        private Long termsId;

        private String name;

        private String termsDetailUrl;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;
    }
}
