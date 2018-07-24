package hello;

import hello.db.DBHashMapEncrypted;
import hello.db.DBHashMapPlain;
import hello.db.SQLUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/home")
                    .permitAll()

                //.and().authorizeRequests().antMatchers("/admin").hasRole("ADMIN")

                .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()

                .and()
                .logout()
                    .permitAll();
    }

    @Override
    public UserDetailsService userDetailsService () {
        return new InMemoryUserDetailsManager(
                //new DBHashMapPlain().usersDetails() // plain storage
                new DBHashMapEncrypted().usersDetails() // secure storage
        );
        // return new SQLUserDetailsService(); // own implementation UserDetailsService based on SQL
    }

/*
    this is also working configuration #2

    @Autowired
    private SQLUserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
*/

}