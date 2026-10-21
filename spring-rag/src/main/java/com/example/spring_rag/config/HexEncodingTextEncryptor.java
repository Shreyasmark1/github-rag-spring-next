package com.example.spring_rag.config;

import org.jspecify.annotations.NullMarked;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.TextEncryptor;

public class HexEncodingTextEncryptor implements TextEncryptor {

    private final BytesEncryptor encryptor;

    public HexEncodingTextEncryptor(BytesEncryptor encryptor) {
        this.encryptor = encryptor;
    }

    @Override
    public @NullMarked String encrypt(String text) {
        return new String(Hex.encode(this.encryptor.encrypt(Utf8.encode(text))));
    }

    @Override
    public @NullMarked String decrypt(String encryptedText) {
        return Utf8.decode(this.encryptor.decrypt(Hex.decode(encryptedText)));
    }
}