package com.fpt.booking.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileUploadService {
    public void init();
    public void save(MultipartFile file);
    Resource getFileByName(String fileName);
    public void deleteAll();
    Stream<Path> loadAllFiles();
}
