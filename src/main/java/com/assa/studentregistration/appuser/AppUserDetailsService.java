package com.assa.studentregistration.appuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository mUserRepository;

    @Autowired
    public AppUserDetailsService(UserRepository userRepository) {
        mUserRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return mUserRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User  with %s not found", username)));
    }

}
