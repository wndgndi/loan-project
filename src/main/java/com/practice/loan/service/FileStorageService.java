package com.practice.loan.service;

import java.nio.file.Path;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    void save(Long applicationId, MultipartFile file);

    Resource load(Long applicationId, String fileName);

    Stream<Path> loadAll(Long applicationId);

    void deleteAll(Long applicationId);
}
