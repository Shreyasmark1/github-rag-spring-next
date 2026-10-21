package com.example.spring_rag.security;

import com.example.spring_rag.entity.UserEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class AppUserPrinciple  implements OAuth2User {

    @Getter
    private final UserEntity user;
    private final Map<String, Object> attributes;

    public UUID getId(){
        return user.getId();
    }

    @Override
    public @NullMarked Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public @NullMarked Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_USER");
    }

    @Override
    public @NullMarked String getName() {
        return user.getId().toString();
    }
}
