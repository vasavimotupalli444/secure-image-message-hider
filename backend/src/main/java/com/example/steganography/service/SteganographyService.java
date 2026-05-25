package com.example.steganography.service;

import com.example.steganography.util.SteganographyUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SteganographyService {

    public byte[] encodeMessage(MultipartFile image, String message) throws Exception {
        return SteganographyUtil.encode(image.getBytes(), message);
    }

    public String decodeMessage(MultipartFile image) throws Exception {
        return SteganographyUtil.decode(image.getBytes());
    }
}