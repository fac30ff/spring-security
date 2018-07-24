package security.beans;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import security.jpa.User;
import security.jpa.UserRepository;

import java.util.ArrayList;
import java.util.Map;

@Configuration
public class CreateUsersBean {
    @Bean
    public CommandLineRunner createUsersInDataBase(UserRepository repository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                final String PREFIX = "{pbkdf2}";
                System.out.print("__Initial uses writing into DB...");
                ArrayList<String[]> r = new ArrayList<>();
                r.add(new String[]{"ROLE_USER"});
                r.add(new String[]{"ROLE_ADMIN"});
                r.add(new String[]{"ROLE_USER", "ROLE_ADMIN"});
                int index = 0;

                for (Map.Entry<String, String> entry : new DBHashMapEncrypted().source()) {
                    String login = entry.getKey();
                    String password = PREFIX + entry.getValue();
                    String[] roless = r.get(index++);
                    repository.save(new User(login, password, roless));
                }

                System.out.println("Done");
            }
        };
    }
}
