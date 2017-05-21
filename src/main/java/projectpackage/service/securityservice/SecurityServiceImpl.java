package projectpackage.service.securityservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import projectpackage.model.security.AuthCredentials;
import projectpackage.repository.securitydao.AuthCredentialsDAO;

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

    @Autowired
    private AuthCredentialsDAO authCredentialsDAO;

    @Override
    public int getAuthenticatedUserId(String s){
        AuthCredentials credentials = authCredentialsDAO.getUserByUsername(s);
        return credentials.getUserId();
    }

    @Override
    public Boolean autologin(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,password, userDetails.getAuthorities());
        authenticationManager.authenticate(authenticationToken);
        if (authenticationToken.isAuthenticated()){
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            return true;
        } else {
            return false;
        }
    }
}
