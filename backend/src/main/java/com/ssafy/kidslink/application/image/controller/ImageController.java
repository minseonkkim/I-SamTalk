package com.ssafy.kidslink.application.image.controller;

import com.ssafy.kidslink.application.image.service.ImageService;
import com.ssafy.kidslink.common.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> getLocalImage(@PathVariable("fileName") String fileName) {
        Resource resource = imageService.loadFileAsResourceByLocalStorage(fileName);
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        String contentType = "image/jpeg"; // 필요에 따라 contentType을 결정합니다.
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
