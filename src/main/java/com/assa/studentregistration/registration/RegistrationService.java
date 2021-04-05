package com.assa.studentregistration.registration;

import com.assa.studentregistration.appuser.AppUserDetail;
import com.assa.studentregistration.appuser.AppUserDetailsService;
import com.assa.studentregistration.appuser.AppUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final EmailValidation mEmailValidation;
    private final AppUserDetailsService mUserDetailsService;

    @Autowired
    public RegistrationService(EmailValidation emailValidation, AppUserDetailsService userDetailsService) {
        mEmailValidation = emailValidation;
        mUserDetailsService = userDetailsService;
    }

    public String register(RegistrationRequest request) {
        boolean isValidEmail = mEmailValidation.test(request.getEmail());
        if (!isValidEmail) {
            throw new IllegalStateException("Invalid email!");
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

}
