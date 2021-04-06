package com.assa.studentregistration.appuser;

import com.assa.studentregistration.registration.token.ConfirmationToken;
import com.assa.studentregistration.registration.token.ConfirmationTokenService;
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

    @Autowired
    public AppUserDetailsService(
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder,
            ConfirmationTokenService tokenService) {
        mUserRepository = userRepository;
        mPasswordEncoder = passwordEncoder;
        mTokenService = tokenService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
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
        // TODO: Send email
        return token;
    }
}
