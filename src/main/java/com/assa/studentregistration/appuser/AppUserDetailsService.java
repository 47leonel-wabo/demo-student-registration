package com.assa.studentregistration.appuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository mUserRepository;
    private final BCryptPasswordEncoder mPasswordEncoder;

    @Autowired
    public AppUserDetailsService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        mUserRepository = userRepository;
        mPasswordEncoder = passwordEncoder;
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
        // TODO: Send validation token
        return "User Registered!";
    }
}
