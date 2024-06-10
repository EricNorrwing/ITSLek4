package se.norrwing.itslek4demo.configuration;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import se.norrwing.itslek4demo.temp.CustomWebAuthenticationDetailsSource;

public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public CustomUsernamePasswordAuthenticationFilter(CustomWebAuthenticationDetailsSource customWebAuthenticationDetailsSource) {
        setAuthenticationDetailsSource(customWebAuthenticationDetailsSource);
    }
}
