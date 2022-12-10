/*
package qs.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import qs.model.JwtUser;
import qs.repository.UserRepository;
@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userReporitory;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        JwtUser userInfo = userReporitory.getUserByUserName(username);
        if(userInfo==null)
            throw new UsernameNotFoundException(username);
        return userInfo;

    }
}
*/
