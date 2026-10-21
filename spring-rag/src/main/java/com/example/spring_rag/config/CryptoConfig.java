package com.example.spring_rag.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.AesCbcBytesEncryptor;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.TextEncryptor;

@Configuration
public class CryptoConfig {

    @Bean
    TextEncryptor textEncryptor(
        @Value("${app.token-encryptor-password}") String password,
        @Value("${app.token-encryptor-salt}") String salt
    ){
        BytesEncryptor bytesEncryptor = AesCbcBytesEncryptor.withPassword(password, salt).build();
        return new HexEncodingTextEncryptor(bytesEncryptor);
    }
}
