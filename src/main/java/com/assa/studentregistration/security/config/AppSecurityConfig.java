package com.assa.studentregistration.security.config;

import com.assa.studentregistration.appuser.AppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AppUserDetailsService mAppUserDetailsService;
    private final PasswordEncoder mPasswordEncoder;

    @Autowired
    public AppSecurityConfig(AppUserDetailsService appUserDetailsService, PasswordEncoder passwordEncoder) {
        mAppUserDetailsService = appUserDetailsService;
        mPasswordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .mvcMatchers("/api/v*/registration/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(mPasswordEncoder);
        provider.setUserDetailsService(mAppUserDetailsService);
        return provider;
    }
}
