package com.example.spring_rag.security;

import com.example.spring_rag.entity.UserEntity;
import com.example.spring_rag.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GithubOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserService userService;
    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(@NonNull OAuth2UserRequest request){
        OAuth2User githubUser = delegate.loadUser(request);

        String accessToken = request.getAccessToken().getTokenValue();
        String scopes = String.join(",", request.getAccessToken().getScopes());

        UserEntity user = userService.upsertFromGithub(githubUser.getAttributes(), accessToken, scopes);

        return new AppUserPrinciple(user, githubUser.getAttributes());
    }

}
