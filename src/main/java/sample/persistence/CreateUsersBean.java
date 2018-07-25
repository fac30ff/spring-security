package sample.persistence;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

@Configuration
public class CreateUsersBean {
    @Bean
    public CommandLineRunner createUsersInDataBase(UserRepository repository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) {
                System.out.print("__Initial uses writing into DB...");

                class UserData {
                    String login;
                    String passwd;
                    String[] roles;

                    public UserData(String login, String passwd, String[] roles) {
                        this.login = login;
                        this.passwd = passwd;
                        this.roles = roles;
                    }
                }

                PasswordEncoder encoder = new Pbkdf2PasswordEncoder();
                UserData[] users = {
                        new UserData("u1", encoder.encode("p1"), new String[]{"ROLE_USER"}),
                        new UserData("u2", encoder.encode("p2"), new String[]{"ROLE_ADMIN"}),
                        new UserData("u3", encoder.encode("p3"), new String[]{"ROLE_USER", "ROLE_ADMIN"}),
                };

                for (UserData u : users) {
                    repository.save(new User(u.login, u.passwd, u.roles));
                }

                System.out.println("Done");
            }
        };
    }
}
