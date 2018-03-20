package qs.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import qs.web.jwt.JwtAuthenticationTokenFilter;

import javax.servlet.FilterRegistration;
import java.util.concurrent.Executors;

/**
 * Created by yinqingzhun on 2017/09/15.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    //@Bean
    //public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter()   {
    //    return new JwtAuthenticationTokenFilter();
    //}
    //
    //@Bean
    //public FilterRegistrationBean jwtFilterRegistration(){
    //    FilterRegistrationBean filterRegistration=new FilterRegistrationBean();
    //    filterRegistration.setFilter(jwtAuthenticationTokenFilter());
    //    filterRegistration.setOrder(Ordered.HIGHEST_PRECEDENCE);
    //    filterRegistration.setName("jwtFilter");
    //    return filterRegistration;
    //}

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                // Spring Security should completely ignore URLs starting with /resources/
                .antMatchers("/static/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                //.antMatchers("/public/**").permitAll()
                .antMatchers("/index").hasRole("USER")
                .antMatchers("/auth/**").permitAll()
                .and()
                // Possibly more configuration ...
                .formLogin() // enable form based log in
                .loginPage("/logon")
                // set permitAll for all URLs associated with Form Login
                .permitAll();
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                // 设置UserDetailsService
                .userDetailsService(this.userDetailsService)
                // 使用BCrypt进行密码的hash
                //.passwordEncoder(passwordEncoder())
        ;

        // enable in memory based authentication with a user named "user" and "admin"
        //.inMemoryAuthentication().withUser("user").password("password").roles("USER")
        //.and().withUser("admin").password("password").roles("USER", "ADMIN");
    }
}
