package com.example.steganography.controller;

import com.example.steganography.service.SteganographyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*")
public class SteganographyController {

    @Autowired
    private SteganographyService service;

    @PostMapping("/encode")
    public ResponseEntity<ByteArrayResource> encode(
            @RequestParam("image") MultipartFile image,
            @RequestParam("message") String message
    ) throws Exception {

        byte[] encodedImage = service.encodeMessage(image, message);

        ByteArrayResource resource = new ByteArrayResource(encodedImage);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=encoded-image.png")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @PostMapping("/decode")
    public String decode(@RequestParam("image") MultipartFile image) throws Exception {
        return service.decodeMessage(image);
    }
}