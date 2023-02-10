package com.example.store.configuration;

import com.example.store.service.NoOpSmsSender;
import com.example.store.service.SmsSender;
import com.example.store.service.TwilioSmsSender;
import com.example.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfiguration {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public ApplicationListener<ContextRefreshedEvent> startupEventListener() {
        return event -> userService.createDefaultAdminIfNotExist();
    }

    @Bean
    @ConditionalOnProperty(
            value="sms.enabled",
            havingValue = "true",
            matchIfMissing = false
    )
    public SmsSender smsSender() {
        return new TwilioSmsSender();
    }
    @Bean
    @ConditionalOnProperty(
            value="sms.enabled",
            havingValue = "false",
            matchIfMissing = false
    )
    public SmsSender noOpSmsSender() {
        return new NoOpSmsSender();
    }
}
