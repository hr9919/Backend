package com.example.project.controller;

import com.example.project.dto.TokenRequestBody;
import com.example.project.service.UserFcmTokenService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users/{userId}/tokens")
public class UserFcmTokenController {

    private final UserFcmTokenService service;

    public UserFcmTokenController(UserFcmTokenService service) {
        this.service = service;
    }

    @PostMapping
    public void saveToken(@PathVariable Long userId, @RequestBody TokenRequestBody body) {
        service.saveOrUpdateToken(userId, body);
    }
}

