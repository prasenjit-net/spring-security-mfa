package net.prasenjit.poc.springsecuritymfa.config;

import lombok.RequiredArgsConstructor;
import net.prasenjit.poc.springsecuritymfa.service.MfaAuthenticatorService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final MfaAuthenticatorService mfaAuthenticatorService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(mfaAuthenticatorService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .and()
                .authorizeRequests()
                .mvcMatchers("/login", "/register")
                .permitAll()
                .antMatchers("/css/*", "/js/*", "/webjars/**")
                .permitAll()
                .anyRequest()
                .authenticated();
    }
}
