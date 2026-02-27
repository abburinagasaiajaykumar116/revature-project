package org.example.revshop.service;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ImageService {
    public String uploadImage(MultipartFile file);
}
