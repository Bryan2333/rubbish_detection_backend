package com.bryan.rubbish_detection_backend.utils;

import com.bryan.rubbish_detection_backend.exception.CustomException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.UUID;

public class ImageUtil {
    public static @NotNull String saveBase64Image(@NotNull String base64Image, String uploadDir) throws Exception {
        if (base64Image.contains(",")) {
            base64Image = base64Image.split(",")[1];
        }

        String imageType = checkImageBase64Format(base64Image);

        if (imageType == null) {
            throw new CustomException("图片数据异常");
        }

        byte[] imageBytes = Base64.getDecoder().decode(base64Image);

        String filename = UUID.randomUUID() + "." + imageType;

        File directory = new File(uploadDir);

        File outputFile = new File(directory, filename);

        try (OutputStream os = new FileOutputStream(outputFile)) {
            os.write(imageBytes);
        }

        return filename;
    }

    private static String checkImageBase64Format(String base64ImgData) {
        byte[] b = Base64.getDecoder().decode(base64ImgData);

        String type = null;

        if (0x424D == ((b[0] & 0xff) << 8 | (b[1] & 0xff))) {
            type = "bmp";
        } else if (0x8950 == ((b[0] & 0xff) << 8 | (b[1] & 0xff))) {
            type = "png";
        } else if (0xFFD8 == ((b[0] & 0xff) << 8 | (b[1] & 0xff))) {
            type = "jpg";
        }

        return type;
    }
}
