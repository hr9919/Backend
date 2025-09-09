package com.example.project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageService {

    public String upload(MultipartFile file, String objectName) {
        String bucketUrl = "https://objectstorage.ap-osaka-1.oraclecloud.com/n/axfmxzr7cohq/b/melting-images/o/";
        return bucketUrl + objectName;
    }

    public String uploadProfileImage(Long userId, MultipartFile file) {
        String objectName = "profile/" + userId + "_" + file.getOriginalFilename();
        return upload(file, objectName);
    }

    public String uploadReviewImage(Long reviewId, MultipartFile file) {
        String objectName = "review/" + reviewId + "_" + file.getOriginalFilename();
        return upload(file, objectName);
    }
}
