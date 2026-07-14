package com.joyas.elegance.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServicio {

    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryServicio(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String subirImagenWebp(MultipartFile archivo, String nombreSlug) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(archivo.getBytes(), ObjectUtils.asMap(
            "public_id", nombreSlug,
            "folder", "productos",
            "format", "webp",
            "overwrite", true
        ));
        return (String) uploadResult.get("secure_url");
    }
}
