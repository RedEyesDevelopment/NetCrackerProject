package projectpackage.service.securityservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectpackage.model.security.AuthCredentials;
import projectpackage.repository.securitydao.AuthCredentialsDAO;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AuthCredentialsDAO authCredentialsDAO;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String s) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        AuthCredentials credentials = authCredentialsDAO.getUserByUsername(s);
        if (null==credentials || null==credentials.getRolename()) {
            return null;
        }
            grantedAuthorities.add(new SimpleGrantedAuthority(credentials.getRolename()));
        return new org.springframework.security.core.userdetails.User(credentials.getLogin(), credentials.getPassword(), grantedAuthorities);
    }
}
