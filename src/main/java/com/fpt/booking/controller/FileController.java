package com.fpt.booking.controller;

import com.fpt.booking.controller.base.BaseController;
import com.fpt.booking.domain.payload.response.UploadFileResponse;
import com.fpt.booking.services.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.EXPECTATION_FAILED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController extends BaseController {

    private final FileUploadService fileUploadService;

    @PostMapping("/upload-files")
    @Operation(summary = "Upload one or multiple file")
    public ResponseEntity<?> uploadFiles(@RequestParam("file") MultipartFile[] files) {
        String message = null;
        try {
            List<String> fileNames = new ArrayList<>();
            Arrays.stream(files).forEach(file -> {
                fileUploadService.save(file);
                fileNames.add(file.getOriginalFilename());
            });
            message = "File(s) uploaded successfully " + fileNames;
            return createSuccessResponse(message);
        } catch (Exception e) {
            return createFailureResponse(EXPECTATION_FAILED.toString(), message, "");
        }
    }

    @GetMapping("/{fileName}")
    @Operation(summary = "Get file by name")
    public ResponseEntity<Resource> getFileByName(@PathVariable String fileName) {
        Resource resource = fileUploadService.getFileByName(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filaName=\"" + resource.getFilename() + "\"").body(resource);
    }

    @GetMapping("/image/{filename:.+}")
    @Operation(summary = "View file by name")
    public ResponseEntity<Resource> getImage(@PathVariable String filename)  {
        Resource resource = fileUploadService.getFileByName(filename);
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    @GetMapping("/all-files")
    @Operation(summary = "Get all files in project")
    public ResponseEntity<?> loadAllFiles() {
        List<UploadFileResponse> files = fileUploadService.loadAllFiles()
                .map(path -> {
                    String fileName = path.getFileName().toString();
                    String url = MvcUriComponentsBuilder
                            .fromMethodName(FileController.class,
                                    "getFileByName",
                                    path.getFileName().toString()).build().toString();
                    return new UploadFileResponse(fileName, url);
                }).toList();
        return createSuccessResponse("Danh sách files đã tải lên.", files);
    }
}