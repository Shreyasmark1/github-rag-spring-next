package com.example.spring_rag.security;

import com.example.spring_rag.entity.UserEntity;
import com.example.spring_rag.exceptions.ApiException;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

@Configuration
public class UserSession {

    private AppUserPrinciple require(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof AppUserPrinciple principle)){
            throw ApiException.unAuthorized("Not authenticated");
        }
        return principle;
    }

    public UserEntity getUser(){
        return require().getUser();
    }

    public UUID getId(){
        return getUser().getId();
    }

}
