package kg.PerfectJob.BookStore.config;

import kg.PerfectJob.BookStore.filter.JwtFilter;
import kg.PerfectJob.BookStore.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private JwtFilter jwtFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(encoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
	http.cors().and().csrf().disable().authorizeRequests()
//                .antMatchers("/register/**").permitAll()
//                .antMatchers("/register/saveAdmin").hasAnyRole("ADMIN", "SUPER_ADMIN")
//                .antMatchers("/register/admin").hasAnyRole("SUPER_ADMIN")
//
//                .antMatchers(HttpMethod.GET, "/user/profile").authenticated()
//                .antMatchers("/user/**").authenticated()
//
//                .antMatchers(HttpMethod.GET, "/book/**").permitAll()
//                .antMatchers("/book/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
//
//                .antMatchers(HttpMethod.GET, "/category/**").permitAll()
//                .antMatchers("/category/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
//
//                .antMatchers(HttpMethod.GET, "/author/**").permitAll()
//                .antMatchers("/author/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
//
//                .antMatchers("/cart/**").authenticated()
//                .antMatchers(HttpMethod.GET, "/order/getAll").hasAnyRole("ADMIN", "SUPER_ADMIN")
//                .antMatchers(HttpMethod.DELETE, "/order/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
//                .antMatchers("/order/**").authenticated()
//                .antMatchers("/history/**").hasRole("SUPER_ADMIN")
                .anyRequest().permitAll()
                .and().exceptionHandling()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}