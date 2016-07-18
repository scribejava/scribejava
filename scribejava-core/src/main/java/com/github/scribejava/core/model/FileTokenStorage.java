package com.github.scribejava.core.model;

import com.github.scribejava.core.extractors.OAuth2AccessTokenExtractor;
import com.github.scribejava.core.oauth.OAuth20Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.TimeUnit;

public class FileTokenStorage extends AbstractTokenStorage {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private final Path file;

    public FileTokenStorage(OAuth20Service service, Path file) throws IOException {
        super(service);
        if (Files.notExists(file)) {
            file = Files.createFile(file);
        }
        if (!Files.isRegularFile(file)) {
            throw new IllegalArgumentException("The provided path is not a file");
        }
        this.file = file;
    }

    @Override
    protected OAuth2AccessToken loadToken() {
        try {
            final String token = new String(Files.readAllBytes(file), UTF8);
            return OAuth2AccessTokenExtractor.instance().extract(token);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    protected void storeToken(OAuth2AccessToken token) {
        try {
            Files.write(file, token.getRawResponse().getBytes(UTF8));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    protected boolean isExpired(OAuth2AccessToken token) {
        try {
            final long expirationDate = Files.readAttributes(file, BasicFileAttributes.class)
                    .lastModifiedTime().toMillis() + TimeUnit.SECONDS.toMillis(token.getExpiresIn());
            return System.currentTimeMillis() > expirationDate;
        } catch (IOException e) {
            return true;
        }
    }
}
