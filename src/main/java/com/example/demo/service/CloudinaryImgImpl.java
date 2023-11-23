package com.example.demo.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

import com.cloudinary.Cloudinary;
@Service
public class CloudinaryImgImpl implements CloudinaryImageService {
	@Autowired
	Cloudinary cloudinary;

	@Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        return cloudinary.uploader()
                .upload(multipartFile.getBytes(),
                        Map.of("public_id", UUID.randomUUID().toString()))
                .get("url")
                .toString();
    }
	
	@Override
	public Boolean deleteFile(String imgUrl) {
		try {
	        String publicId = extractPublicId(imgUrl);

	        cloudinary.uploader().destroy(publicId, null);

	        return true; // Deletion successful
	    } catch (Exception e) {
	        e.printStackTrace(); // You might want to log the exception or handle it appropriately
	        return false; // Deletion failed
	    }
	}
	
	private String extractPublicId(String imgUrl) {
	    int startIndex = imgUrl.lastIndexOf("/") + 1;
	    int endIndex = imgUrl.lastIndexOf(".");
	    return imgUrl.substring(startIndex, endIndex);
	}
}
