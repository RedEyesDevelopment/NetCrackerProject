package projectpackage.service.securityservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Created by Gvozd on 07.01.2017.
 */
@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public String findLoggedInUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public Boolean autologin(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        System.out.println("ENCODED PASSWORD="+encodedPassword);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, encodedPassword, userDetails.getAuthorities());
        authenticationManager.authenticate(authenticationToken);
        if (authenticationToken.isAuthenticated()){
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            return true;
        } else {
            return false;
        }
    }
}
