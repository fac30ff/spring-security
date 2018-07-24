package hello.db;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DBHashMapPlain implements DBInterface {

    private final HashMap<String, String> storage = new HashMap<>();

    public DBHashMapPlain() {
        storage.put("u1", "p1");
        storage.put("u2", "p2");
        storage.put("u3", "p3");
    }

    @Override
    public Collection<Map.Entry<String, String>> source() {
        return storage.entrySet();
    }

    @Override
    public boolean check(String login, String password) {
        return storage.containsKey(login)
                && storage.get(login).equals(password);
    }

    private UserDetails buildUserWithPlainPassword(Map.Entry<String, String> entry) {
        return User
                .withDefaultPasswordEncoder()
                .username(entry.getKey())
                .password(entry.getValue())
                .roles(DBRoles.get())
                .build();
    }

    @Override
    public List<UserDetails> usersDetails() {
        return source().stream()
                .map(this::buildUserWithPlainPassword)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        DBInterface db = new DBHashMapPlain();
        System.out.println(db.check("u1", "p1")); // true
        System.out.println(db.check("u2", "p2")); // true
        System.out.println(db.check("u3", "p3")); // true
        System.out.println(db.check("user", "password1")); // false
    }
}
