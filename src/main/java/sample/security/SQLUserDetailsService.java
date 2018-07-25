package sample.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import sample.persistence.UserRepository;

import java.util.List;

@Configuration // or @Component or @Service actually what we need
public class SQLUserDetailsService implements UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(SQLUserDetailsService.class);

    @Autowired
    private UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<sample.persistence.User> users = repo.findByLogin(username);
        users.forEach(user -> LOG.info(user.toString()));
        if (users.size() != 1) {
            throw new UsernameNotFoundException(String.format("User with login `%s` not found", username));
        }
        sample.persistence.User u = users.get(0);
        return new User(u.getLogin(), u.getPasswd(), u.getRoles());
    }

}
