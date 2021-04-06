package com.assa.studentregistration.registration.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository mTokenRepository;

    @Autowired
    public ConfirmationTokenService(final ConfirmationTokenRepository tokenRepository) {
        mTokenRepository = tokenRepository;
    }

    public void saveConfirmationToken(final ConfirmationToken token) {
        mTokenRepository.save(token);
    }

    public ConfirmationToken fetchToken(final String token){
        return mTokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new IllegalStateException(String.format("Token %s not found", token)));

    }

}
