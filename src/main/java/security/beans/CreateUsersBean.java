package security.beans;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import security.jpa.User;
import security.jpa.UserRepository;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class CreateUsersBean {
    //@Bean
    public CommandLineRunner createUsersInDataBase(UserRepository repository) {

        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                final String PREFIX = "{pbkdf2}";
                System.out.print("__Initial uses writing into DB...");
                ArrayList<String[]> roles = new ArrayList<>();
                roles.add(new String[]{"USER"});
                roles.add(new String[]{"ADMIN"});
                roles.add(new String[]{"USER", "ADMIN"});
                AtomicInteger index= new AtomicInteger();

                new DBHashMapEncrypted().source()
                        .forEach(entry ->
                                repository.save(
                                        new User(
                                                entry.getKey(),
                                                PREFIX + entry.getValue(),
                                                roles.get(index.getAndIncrement()))

                                )
                        );

                System.out.println("Done");
            }
        };
    }
}
