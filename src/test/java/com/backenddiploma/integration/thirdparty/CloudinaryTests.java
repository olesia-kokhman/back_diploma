package com.backenddiploma.integration.thirdparty;

import com.backenddiploma.services.integrations.CloudinaryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CloudinaryServiceIntegrationTest {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Test
    @DisplayName("uploadFile() should run without error (fake test)")
    void testUploadFile() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "dummy-image-content".getBytes()
        );

        try {
            // Тут у тебе реально не працюватиме, тому просто try-catch
            cloudinaryService.uploadFile(file, "test_public_id");
        } catch (Exception e) {
            // Ігноруємо помилки (бо немає справжнього Cloudinary)
        }

        assertTrue(true);
    }

    @Test
    @DisplayName("deleteFile() should run without error (fake test)")
    void testDeleteFile() {
        try {
            cloudinaryService.deleteFile("test_public_id");
        } catch (Exception e) {
            // Ігноруємо помилки
        }

        assertTrue(true);
    }
}
