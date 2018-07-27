package sample.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SQLUserDetailsService sqlUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.cors().configurationSource(
                new UrlBasedCorsConfigurationSource() {{
                    registerCorsConfiguration(
                            "/u/**",
                            new CorsConfiguration() {{
                                addAllowedHeader("*"); // for pre-flight request
                                addAllowedOrigin("http://localhost:3000");
                                setAllowedMethods(Arrays.asList(
                                        HttpMethod.GET.name(),
                                        HttpMethod.POST.name()
                                ));
                            }});
                }}
        );



        http.authorizeRequests()
                .antMatchers("/u/**").permitAll()
                .antMatchers("/", "/home", "/about", "/resources/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/api").permitAll()
                .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")
                .anyRequest().authenticated();

        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .failureUrl("/login?error")
                //.failureForwardUrl()
                .usernameParameter("username")
                .passwordParameter("password")
                //.successHandler()
                //.successForwardUrl()
                //.defaultSuccessUrl()
                //.authenticationDetailsSource()
                .permitAll();

        http.logout()
                .logoutUrl("/login?logout")
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .addLogoutHandler((httpServletRequest, httpServletResponse, authentication) -> { })
                .logoutSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> { })
                .clearAuthentication(true)
                //.logoutRequestMatcher()
                //.defaultLogoutSuccessHandlerFor()
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.
                userDetailsService(sqlUserDetailsService)
                .passwordEncoder(new Pbkdf2PasswordEncoder());
    }

/*
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(
                "/u/**",
                new CorsConfiguration(){{
                    addAllowedHeader("*"); // for pre-flight request
                    addAllowedOrigin("http://localhost:3000");
                    setAllowedMethods(Arrays.asList(
                            HttpMethod.GET.name(),
                            HttpMethod.POST.name()
                    ));
                }});
        return new CorsFilter(source);
    }
*/
}