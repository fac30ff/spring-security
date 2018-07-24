package hello.db;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface DBInterface {
    Collection<Map.Entry<String, String>> source();
    default boolean contains(String login) {
        return source().stream().anyMatch(entry -> entry.getKey().equals(login));
    }
    boolean check(String login, String password);
    List<UserDetails> usersDetails();
}
