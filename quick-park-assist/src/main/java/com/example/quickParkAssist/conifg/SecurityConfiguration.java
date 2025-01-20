package com.example.quickParkAssist.conifg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.quickParkAssist.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }


    @Override
protected void configure(HttpSecurity http) throws Exception {
    http
            .csrf().disable() // CSRF protection is disabled
        .authorizeRequests()
            .antMatchers(
                "/registration**",// Permit registration
                    "/searchspots",
                "/css/**",          // Allow CSS files
                "/js/**",           // Allow JS files
                "/images/**",       // Allow image files
                "/index**",         // Allow index page
                "/static/**"  ,   // Allow static resources (optional, not typically needed)
                 "/reset-password",
                    "/forgotPassword","/resetPassword/**"
            ).permitAll()           // Allow everyone to access these paths
            .antMatchers("/admin/**").hasRole("ADMIN") // Restrict admin pages to ROLE_ADMIN
            .antMatchers("/user/**").hasAnyRole("USER", "ADMIN") // Restrict user pages to ROLE_USER or ROLE_ADMIN
            .anyRequest().authenticated() // All other URLs require authentication
        .and()
        .formLogin()
            .loginPage("/login") // Customize login page
            .permitAll()         // Allow everyone to access the login page
        .and()
        .logout()
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/") // Redirect after logout
            .permitAll();         // Allow logout for everyone


}

}