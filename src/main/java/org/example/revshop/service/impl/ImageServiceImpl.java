package org.example.revshop.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.example.revshop.exception.BadRequestException;
import org.example.revshop.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class ImageServiceImpl implements ImageService {

    private final Cloudinary cloudinary;

    public ImageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadImage(MultipartFile file) {
        // validation
        if (file == null || file.isEmpty())
            throw new BadRequestException("No image file provided");


        try {
            Map uploadResult = cloudinary.uploader()
                    .upload(file.getBytes(), ObjectUtils.emptyMap());

            return uploadResult.get("secure_url").toString();

        } catch (Exception e) {
            throw new BadRequestException("Unable to upload image. Please try again.");
        }
    }
}
