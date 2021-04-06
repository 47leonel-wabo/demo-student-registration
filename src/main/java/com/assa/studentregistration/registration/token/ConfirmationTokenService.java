package com.assa.studentregistration.registration.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository mTokenRepository;

    @Autowired
    public ConfirmationTokenService(ConfirmationTokenRepository tokenRepository) {
        mTokenRepository = tokenRepository;
    }

    public void saveConfirmationToken(ConfirmationToken token) {
        mTokenRepository.save(token);
    }

    public ConfirmationToken fetchToken(String token) {
        return mTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new IllegalStateException(String.format("Confirmation token with token %s not found", token)));
    }
}
