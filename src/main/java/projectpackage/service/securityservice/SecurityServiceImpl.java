package projectpackage.service.securityservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import projectpackage.model.auth.User;
import projectpackage.model.security.AuthCredentials;
import projectpackage.repository.securitydao.AuthCredentialsDAO;

import java.util.Collection;

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
    public boolean cryptUserPass(User user) {
        if (null!=user && null!=user.getPassword()){
            String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            return true;
        }
        return false;
    }

    @Override
    public int getAuthenticatedUserId(String s){
        AuthCredentials credentials = authCredentialsDAO.getUserByUsername(s);
        return credentials.getUserId();
    }

    @Override
    public Boolean autologin(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        Collection<? extends GrantedAuthority> authorities;
        if(null!=userDetails.getAuthorities()){
            authorities =userDetails.getAuthorities();
        }  else authorities = null;
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken
                (userDetails, password, authorities);
        authenticationManager.authenticate(authenticationToken);
        if (authenticationToken.isAuthenticated()){
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}
