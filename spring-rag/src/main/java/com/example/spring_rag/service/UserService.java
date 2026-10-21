package com.example.spring_rag.service;

import com.example.spring_rag.entity.UserEntity;
import com.example.spring_rag.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TextEncryptor textEncryptor;

    @Transactional
    public UserEntity upsertFromGithub(Map<String, Object> attributes, String accessToken, String scopes){

        Long githubId = toLong(attributes.get("id"));
        String login = String.valueOf(attributes.get("login"));
        String name = attributes.get("name") != null ? String.valueOf(attributes.get("name")) : login;
        String avatarUrl = attributes.get("avatar_url") != null ? String.valueOf(attributes.get("avatar_url")) : null;

        String encryptedToken = textEncryptor.encrypt(accessToken);

        UserEntity user = userRepository.findByGithubId(githubId).orElseGet(UserEntity::new);

        user.setGithubId(githubId);
        user.setGithubUsername(login);
        user.setDisplayName(name);
        user.setAvatarUrl(avatarUrl);
        user.setAccessToken(encryptedToken);
        user.setTokenScopes(scopes);

        return userRepository.save(user);

    }

    @Transactional(readOnly = true)
    public UserEntity requireById(UUID id){
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public String decryptAccessToken(UserEntity user){
        return textEncryptor.decrypt(user.getAccessToken());
    }

    private static Long toLong(Object value){
        if(value instanceof Number number){
            return number.longValue();
        }

        return Long.parseLong(String.valueOf(value));
    }
}
