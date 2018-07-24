package security.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SQLUserDetailsService implements UserDetailsService {

    @Autowired
    private DBHashMapEncrypted hash;

    /*
    here should be Autowired UsersRepository
    instead of DBHashMapEncrypted
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        for (UserDetails ud : hash.usersDetails()) {
            if (username.equals(ud.getUsername())) {
                return new User(ud.getUsername(), ud.getPassword(), DBRoles.auth());
            }
        }
        throw new UsernameNotFoundException(String.format("User with login `%s` not found", username));
    }

}
