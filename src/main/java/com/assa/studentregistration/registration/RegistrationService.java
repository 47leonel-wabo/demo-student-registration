package com.assa.studentregistration.registration;

import com.assa.studentregistration.appuser.AppUserDetail;
import com.assa.studentregistration.appuser.AppUserDetailsService;
import com.assa.studentregistration.appuser.AppUserRole;
import com.assa.studentregistration.registration.token.ConfirmationToken;
import com.assa.studentregistration.registration.token.ConfirmationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class RegistrationService {

    private final EmailValidation mEmailValidation;
    private final AppUserDetailsService mUserDetailsService;
    private final ConfirmationTokenService mTokenService;

    @Autowired
    public RegistrationService(EmailValidation emailValidation, AppUserDetailsService userDetailsService, ConfirmationTokenService tokenService) {
        mEmailValidation = emailValidation;
        mUserDetailsService = userDetailsService;
        mTokenService = tokenService;
    }

    public String register(final RegistrationRequest request) {
        boolean isValidEmail = mEmailValidation.test(request.getEmail());
        if (!isValidEmail) {
            throw new IllegalStateException("email already in use");
        }
        return mUserDetailsService.signUpUser(
                new AppUserDetail(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        AppUserRole.USER
                )
        );
    }

    @Transactional
    public String confirmToken(final String token) {
        ConfirmationToken confirmationToken = mTokenService.fetchToken(token);
        if (confirmationToken.getConfirmAt() != null) {
            throw new IllegalStateException("Email already confirmed");
        }
        LocalDateTime tokenExpiredAt = confirmationToken.getExpiredAt();
        if (tokenExpiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired");
        }

        AppUserDetail userDetail = mUserDetailsService
                .fetchUserById(confirmationToken.getAppUser().getId());
        // set confirmation token time
        confirmationToken.setConfirmAt(LocalDateTime.now());
        mTokenService.saveConfirmationToken(confirmationToken);
        // enable user
        mUserDetailsService.enableUser(userDetail);
        return "Registration confirmed";
    }

}
