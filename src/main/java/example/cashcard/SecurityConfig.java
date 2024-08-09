package example.cashcard;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

// use this class to configure Spring and Spring Boot itself. Any Beans specified in this class will now be available to Spring's Auto Configuration engine.
@Configuration
class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // purpose is to configure the filter chain for the cashcard application
        http.authorizeHttpRequests(request -> request
                .requestMatchers("/cashcards/**") // requests to this URL requires authentication
                .authenticated())
            .httpBasic(Customizer.withDefaults()) // enables HTTP basic authentication settings
            .csrf(csrf -> csrf.disable()); // disable cross-site request forgery protection which is on by default since it's a simple app
        return http.build();
    }

    @Bean
    UserDetailsService testOnlyUsers(PasswordEncoder passwordEncoder) {
        User.UserBuilder users = User.builder();
        UserDetails sarah = users
            .username("sarah1")
            .password(passwordEncoder.encode("abc123"))
            .roles()
            .build();
        return new InMemoryUserDetailsManager(sarah);

    }

    // Spring Security expects a Bean to configure its Filter Chain, which you learned about in the Simple Spring Security lesson. Annotating a method returning a SecurityFilterChain with the @Bean satisfies this expectation
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}