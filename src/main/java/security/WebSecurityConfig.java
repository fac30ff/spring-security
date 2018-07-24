package security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import security.beans.DBHashMapEncrypted;
import security.beans.DBHashMapPlain;
import security.db.DBRoles;
import security.db.SQLUserDetailsService;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);

    // All pages secured
    //@Override
    protected void configure0(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .and()
                .httpBasic();
    }

    // "/login" added to unsecured
    //@Override
    protected void configure1(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll();
    }

    // All pages secured, but "/" and "/home" unsecured
    //@Override
    protected void configure2(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/", "/home").permitAll()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .and()
                .logout()
                    .permitAll();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/", "/home", "/about", "/resources/**").permitAll()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/user/**").hasRole("USER")
                    .antMatchers(HttpMethod.GET, "/api").permitAll()
                    .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")
                .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .and()
                .logout()
                    .permitAll();
    }

    // logout clarification
    //@Override
    protected void configure4(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/", "/home", "/about", "/resources/**").permitAll()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/user/**").hasRole("USER")
                    .antMatchers(HttpMethod.GET, "/api").permitAll()
                    //.antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .and()
                .logout()
                    .logoutUrl("/my/logout")
                    .logoutSuccessUrl("/my/index")
                    .logoutSuccessHandler(new LogoutSuccessHandler() {
                        @Override
                        public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

                        }
                    })
                    .invalidateHttpSession(true)
                    .addLogoutHandler(new LogoutHandler() {
                        @Override
                        public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {

                        }
                    })
                    .deleteCookies("JSESSIONID")
                    .permitAll();
    }

    //@Bean
    //@Override
    protected UserDetailsService userDetailsService_Plain () {
    //public UserDetailsService userDetailsService () {
        return new InMemoryUserDetailsManager(
                new DBHashMapPlain().usersDetails() // plain storage
        );
    }

    //@Override
    protected UserDetailsService userDetailsService_Secured () {
        return new InMemoryUserDetailsManager(
                new DBHashMapEncrypted().usersDetails() // secure storage
        );
    }

    // own implementation UserDetailsService based on SQL
    //@Bean // or @Bean here or @Service at SQLUserDetailsService class
    //@Override
    protected UserDetailsService userDetailsService_SQL () {
        return new SQLUserDetailsService();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
    //protected UserDetailsService userDetailsService_inline() {
        return new InMemoryUserDetailsManager() {{
            createUser(User.withDefaultPasswordEncoder().username("u0").password("p0").roles(DBRoles.getU()).build());
            createUser(User.withDefaultPasswordEncoder().username("a0").password("p0").roles(DBRoles.getU()).build());
        }};
    }

    /*
    Second way to set configurtion

    @Autowired
    private SQLUserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
*/

}