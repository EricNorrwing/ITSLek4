package se.norrwing.itslek4demo.configuration;

import org.jboss.aerogear.security.otp.Totp;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import se.norrwing.itslek4demo.model.User;
import se.norrwing.itslek4demo.repository.UserRepository;
import se.norrwing.itslek4demo.temp.CustomWebAuthenticationDetails;

public class TwoFactorAuthenticationProvider extends DaoAuthenticationProvider {

    private final UserRepository userRepository;
    private UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;

    public TwoFactorAuthenticationProvider(UserRepository userRepository, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder){
        setUserDetailsService(userDetailsService);
        setPasswordEncoder(passwordEncoder);
        this.userRepository = userRepository;
    }

    @Override
    public Authentication authenticate(Authentication auth)
            throws AuthenticationException {
        String verificationCode
                = ((CustomWebAuthenticationDetails) auth.getDetails())
                .getVerificationCode();
        User user = userRepository.findByEmail(auth.getName());
        if (user == null) {
            throw new BadCredentialsException("Invalid username or password");
        }
        Totp totp = new Totp( user.getSecret());
        if (!totp.verify(verificationCode)) {
            throw new BadCredentialsException("Invalid verification code");
        }
        Authentication result = super.authenticate(auth);
        return new UsernamePasswordAuthenticationToken(
                user, result.getCredentials(), result.getAuthorities());
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
