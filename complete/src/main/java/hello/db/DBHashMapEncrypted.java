package hello.db;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class DBHashMapEncrypted implements DBInterface {
    private final HashMap<String, String> storage = new HashMap<>();
    private final PasswordEncoder enc;

    public DBHashMapEncrypted() {
        this(new Pbkdf2PasswordEncoder());
    }

    public DBHashMapEncrypted(PasswordEncoder penc) {
        this.enc = penc;
        storage.put("u1", enc.encode("p1"));
        storage.put("u2", enc.encode("p2"));
        storage.put("u3", enc.encode("p3"));
    }

    @Override
    public Collection<Map.Entry<String, String>> source() {
        return storage.entrySet();
    }

    @Override
    public boolean check(String login, String password) {
        return storage.containsKey(login)
                && enc.matches(password, storage.get(login));
    }

    private UserDetails buildUserWithSecuredPassword(Map.Entry<String, String> entry) {
        return User
                .withUsername(entry.getKey())
                .password("{pbkdf2}"+entry.getValue())
                .roles(DBRoles.getUA())
                .build();
    }

    @Override
    public List<UserDetails> usersDetails() {
        return source().stream()
                .map(this::buildUserWithSecuredPassword)
                .collect(Collectors.toList());
    }

    public static void mainDesignSample001() {
        DBInterface db = new DBHashMapEncrypted();
        System.out.println(db.check("u1", "p1")); // true
        System.out.println(db.check("u2", "p2")); // true
        System.out.println(db.check("u3", "p3")); // true
        System.out.println(db.check("user", "password1")); // false

        db.source().forEach(e-> System.out.println(e.getValue()));
        Pbkdf2PasswordEncoder enc = new Pbkdf2PasswordEncoder();
        UserDetails ud = User
                .builder()
                .passwordEncoder(enc::encode)
                .username("alex")
                .password("p1")
                .roles("USER")
                .build();
        String pas = ud.getPassword();
        System.out.println(pas);
        boolean p2 = enc.matches("p1", pas);
        System.out.println(p2);
    }

}
