package com.assa.studentregistration.appuser;

import com.assa.studentregistration.email.EmailService;
import com.assa.studentregistration.registration.token.ConfirmationToken;
import com.assa.studentregistration.registration.token.ConfirmationTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository mUserRepository;
    private final BCryptPasswordEncoder mPasswordEncoder;
    private final ConfirmationTokenService mTokenService;
    private final EmailService mEmailService;
    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    public AppUserDetailsService(
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder,
            ConfirmationTokenService tokenService, EmailService emailService) {
        mUserRepository = userRepository;
        mPasswordEncoder = passwordEncoder;
        mTokenService = tokenService;
        mEmailService = emailService;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return mUserRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User  with %s not found", username)));
    }

    public String signUpUser(AppUserDetail appUserDetail) {
        boolean isUserExists = mUserRepository.findByEmail(appUserDetail.getEmail()).isPresent();
        if (isUserExists) {
            throw new IllegalStateException("Email already exist");
        }
        String encodedPassword = mPasswordEncoder.encode(appUserDetail.getPassword());
        appUserDetail.setPassword(encodedPassword);
        mUserRepository.save(appUserDetail);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUserDetail);
        mTokenService.saveConfirmationToken(confirmationToken);
        // Send email
        String link = String.format(
                "<html>" +
                        "<head>" +
                        "<title>Student Registration</title>" +
                        "<style>" +
                        "h1{background-color: #3e3e3e; color: white;}" +
                        "body{padding: 10px;}" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<h1>Confirm Email</h1>" +
                        "Hi, %s<br/><br/>" +
                        "Welcome,<br/> please click on the link below to activate your account<br/><br/>" +
                        "<a href=\"http://localhost:8080/api/v1/registration/confirm?token=%s\">Activation Link</a><br/><br/>" +
                        "This link will expires in 15 min." +
                        "</body>" +
                        "</html>", appUserDetail.getFirstName(), token);
        mEmailService.send(appUserDetail.getEmail(),link);
        return token;
    }

    public AppUserDetail fetchUserById(final Long userId) {
        return mUserRepository
                .findById(userId)
                .orElseThrow(() -> new IllegalStateException(String.format("User with id %s not found", userId)));
    }

    public void enableUser(AppUserDetail userDetail) {
        userDetail.setEnabled(true);
        mUserRepository.save(userDetail);
    }
}
