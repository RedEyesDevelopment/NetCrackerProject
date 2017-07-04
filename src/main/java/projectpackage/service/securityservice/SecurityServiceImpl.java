package projectpackage.service.securityservice;

import org.apache.log4j.Logger;
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

@Service
public class SecurityServiceImpl implements SecurityService {

    private static final Logger LOGGER = Logger.getLogger(SecurityServiceImpl.class);

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
    public boolean isPasswordMatchEncrypted(String raw, String encoded) {
        return bCryptPasswordEncoder.matches(raw, encoded);
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
        LOGGER.info("login="+username);
        LOGGER.info("password="+password+" encoded="+encodedPassword);
        Collection<? extends GrantedAuthority> authorities;
        if(userDetails != null && null!=userDetails.getAuthorities()){
            authorities =userDetails.getAuthorities();
        }  else authorities = null;
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken
                (userDetails, password, authorities);
        authenticationManager.authenticate(authenticationToken);
        if (authenticationToken.isAuthenticated()){
            LOGGER.info("auth+");
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            return Boolean.TRUE;
        } else {
            LOGGER.info("auth-");
            return Boolean.FALSE;
        }
    }
}
