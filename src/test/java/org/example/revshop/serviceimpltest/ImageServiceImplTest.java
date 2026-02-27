package org.example.revshop.serviceimpltest;



import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.example.revshop.exception.BadRequestException;
import org.example.revshop.service.impl.ImageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @InjectMocks
    private ImageServiceImpl imageService;



    @Test
    void testUploadImage_Success() throws Exception {

        MultipartFile file = mock(MultipartFile.class);

        when(file.isEmpty()).thenReturn(false);
        when(file.getBytes()).thenReturn("dummy-image".getBytes());

        when(cloudinary.uploader()).thenReturn(uploader);

        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("secure_url", "https://cloudinary.com/image.jpg");

        when(uploader.upload(any(byte[].class), anyMap()))
                .thenReturn(uploadResult);

        String result = imageService.uploadImage(file);

        assertEquals("https://cloudinary.com/image.jpg", result);
    }

    // ❌ Null file
    @Test
    void testUploadImage_NullFile() {

        assertThrows(BadRequestException.class,
                () -> imageService.uploadImage(null));
    }

    // ❌ Empty file
    @Test
    void testUploadImage_EmptyFile() {

        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);

        assertThrows(BadRequestException.class,
                () -> imageService.uploadImage(file));
    }

    // ❌ Cloudinary throws exception
    @Test
    void testUploadImage_CloudinaryFailure() throws Exception {

        MultipartFile file = mock(MultipartFile.class);

        when(file.isEmpty()).thenReturn(false);
        when(file.getBytes()).thenReturn("dummy-image".getBytes());

        when(cloudinary.uploader()).thenReturn(uploader);

        when(uploader.upload(any(byte[].class), anyMap()))
                .thenThrow(new RuntimeException("Upload failed"));

        assertThrows(BadRequestException.class,
                () -> imageService.uploadImage(file));
    }
}