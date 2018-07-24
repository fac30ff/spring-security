package security.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import security.beans.DBHashMapEncrypted;
import security.jpa.UserRepository;

import java.util.List;

@Service
public class SQLUserDetailsService implements UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(SQLUserDetailsService.class);

    @Autowired
    private DBHashMapEncrypted hash;

    @Autowired
    private UserRepository repo;

    //@Override
    public UserDetails loadUserByUsername1(String username) throws UsernameNotFoundException {
        LOG.info("____"+username);
        for (UserDetails ud : hash.usersDetails()) {
            if (username.equals(ud.getUsername())) {
                return new User(ud.getUsername(), ud.getPassword(), DBRoles.auth());
            }
        }
        throw new UsernameNotFoundException(String.format("User with login `%s` not found", username));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<security.jpa.User> byLogin = repo.findByLogin(username);
        byLogin.forEach(user -> LOG.info(user.toString()));
        if (byLogin.size() == 1) {
            security.jpa.User u = byLogin.get(0);
            return new User(u.getLogin(), u.getPasswd(), u.getRoles());
        }
        throw new UsernameNotFoundException(String.format("User with login `%s` not found", username));
    }

}
