package sample.persistence;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String login;

    private String passwd;

    private String roles;

    public static User Empty() {
        return new User("<not found>", "", new String[]{});
    }

    protected User() { }

    public User(String login, String passwd, String[] roles) {
        this.login = login;
        this.passwd = passwd;
        setRoles(roles);
    }

    public String getLogin() {
        return login;
    }

    public String getPasswd() {
        return passwd;
    }

    public List<GrantedAuthority> getRoles() {
        return Arrays.stream(roles.split(":"))
                .map(s -> (GrantedAuthority) () -> s)
                .collect(Collectors.toList());
    }

    public void setRoles(String[] roles) {
        this.roles = String.join(":", roles);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", passwd='" + passwd + '\'' +
                "roles:["+ roles +"]"+
                '}';
    }
}
