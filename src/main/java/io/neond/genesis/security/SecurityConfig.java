package io.neond.genesis.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthorizationFilter customAuthorizationFilter;
    private final CustomSuccessHandler customSuccessHandler;
    private final CustomFailureHandler customFailureHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors().disable()
                .csrf().disable()
                .formLogin().disable()
                .headers().frameOptions().disable()
                .and().httpBasic().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .apply(new CustomDsl())

                .and()
                .authorizeHttpRequests()
                .requestMatchers("/member/**").hasAnyAuthority("ROLE_PERMITTED")
                .requestMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN")
                .anyRequest().permitAll();




        return http.build();
    }

    public class CustomDsl extends AbstractHttpConfigurer<CustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {


            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            CustomAuthenticationFilter authenticationFilter = new CustomAuthenticationFilter(authenticationManager);

            authenticationFilter.setFilterProcessesUrl("/login");
            authenticationFilter.setAuthenticationSuccessHandler(customSuccessHandler);
            authenticationFilter.setAuthenticationFailureHandler(customFailureHandler);

            http
                    .addFilter(authenticationFilter)
                    .addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        }
    }
}
