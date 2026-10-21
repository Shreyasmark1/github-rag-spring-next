package com.example.spring_rag.controller;

import com.example.spring_rag.dto.ApiResponse;
import com.example.spring_rag.dto.UserResponse;
import com.example.spring_rag.entity.UserEntity;
import com.example.spring_rag.security.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserSession userSession;

    @GetMapping("/login-url")
    public ApiResponse<Map<String, String>> loginUrl(){
        return ApiResponse.success(Map.of("url", "/oauth/authorization/github"));
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> me(){
        UserEntity user = userSession.getUser();
        return ApiResponse.success(UserResponse.fromEntity(user));
    }

}
