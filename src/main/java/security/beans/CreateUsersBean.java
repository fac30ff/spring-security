package security.beans;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import security.db.DBHashMapEncrypted;
import security.jpa.User;
import security.jpa.UserRepository;

@Configuration
public class CreateUsersBean {
    //@Bean
    public CommandLineRunner createUsersInDataBase(UserRepository repository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                new DBHashMapEncrypted().source()
                        .forEach(entry -> repository.save(new User(entry.getKey(), entry.getValue())));
            }
        };
    }
}
