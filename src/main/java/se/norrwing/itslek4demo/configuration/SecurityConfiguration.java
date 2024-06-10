package se.norrwing.itslek4demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import se.norrwing.itslek4demo.repository.UserRepository;
import se.norrwing.itslek4demo.temp.CustomWebAuthenticationDetailsSource;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {
    private final CustomWebAuthenticationDetailsSource customWebAuthenticationDetailsSource;

    public SecurityConfiguration(CustomWebAuthenticationDetailsSource customWebAuthenticationDetailsSource) {
        this.customWebAuthenticationDetailsSource = customWebAuthenticationDetailsSource;
    }

    @Bean
    public TwoFactorAuthenticationProvider twoFactorAuthenticationProvider(
            UserRepository userRepository,
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        return new TwoFactorAuthenticationProvider(userRepository, userDetailsService, passwordEncoder);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http, TwoFactorAuthenticationProvider twoFactorAuthenticationProvider) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(twoFactorAuthenticationProvider)
                .build();
    }

    @Bean
    public SecurityFilterChain securityChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/registerUser"))
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/register", "/registerUser").permitAll()
                                .requestMatchers("/admin").hasRole("ADMIN")
                                .anyRequest().authenticated())
                .formLogin(formLogin ->
                        formLogin.loginPage("/login")
                                .authenticationDetailsSource(customWebAuthenticationDetailsSource)
                                .defaultSuccessUrl("/", true)
                                .failureUrl("/login?error=true")
                                .permitAll())
                .logout(logout ->
                        logout
                                .logoutUrl("/perform_logout")
                                .logoutSuccessUrl("/login?logout")
                                .deleteCookies("JSESSIONID")
                                .invalidateHttpSession(true)
                );

        return http.build();
    }

        @Bean
        public InMemoryUserDetailsManager userDetailsService () {
            var userDetailsService = new InMemoryUserDetailsManager();
            var user = User.builder()
                    .username("user")
                    .password(passwordEncoder().encode("password"))
                    .roles("USER")
                    .build();
            userDetailsService.createUser(user);
            var admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder().encode("password"))
                    .roles("ADMIN", "USER")
                    .build();
            userDetailsService.createUser(admin);
            return userDetailsService;
        }
        public String calculateDivision () {
            return "hej";
        }

        @Bean
        public PasswordEncoder passwordEncoder () {
            return new BCryptPasswordEncoder();
        }
    }
