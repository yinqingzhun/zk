package qs.repository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;
import qs.model.JwtUser;
import qs.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class UserRepository {
    List<User> users = new ArrayList<>();

    {
        User user = new User() {{
            setId(UUID.randomUUID().toString());
            setUsername("u");
            setPassword("$2a$10$Vv4QX/t9X8SR6qBt4cXsLeysgxkwWcsSVksU682QGovjt9Skv.7cG");
            setRoles(Arrays.asList("ROLE_USER"));
        }};
        users.add(user);
    }

    public JwtUser getUserByUserName(String userName) {
        Optional<User> optionalUser = users.stream().filter(p -> p.getUsername() != null && p.getUsername().equalsIgnoreCase(userName)).findAny();
        if (!optionalUser.isPresent()) {
            return null;
        }
        User user = optionalUser.get();
        JwtUser userInfo = new JwtUser(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()), user.getLastPasswordResetDate());
        return userInfo;

    }

    public User insert(User user) {
        user.setId(UUID.randomUUID().toString());
        users.add(user);
        return user;
    }

}
