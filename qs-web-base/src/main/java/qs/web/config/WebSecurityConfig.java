//package qs.web.config;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.SecurityBuilder;
//import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import qs.web.jwt.JwtAuthenticationTokenFilter;
//
//import javax.servlet.FilterRegistration;
//import java.util.concurrent.Executors;
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//    @Autowired
//    UserDetailsService userDetailsService;
//    @Autowired
//    AuthenticationManager authenticationManager;
//
//    @Override
//    public void configure(WebSecurity web) {
//        //忽略资源文件的安全检查
//        web.ignoring().antMatchers("/static/**");
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                //.antMatchers("/", "/home" ).permitAll() //允许匿名用户访问
//                //.antMatchers("/admin/**").hasRole("ADMIN")//允许admin用户访问
//                .anyRequest().permitAll() //允许认证的用户访问
//                //form登录设置
//                .and().formLogin().loginPage("/login").defaultSuccessUrl("/helloadmin")
//                .and().logout().permitAll();
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                // 设置UserDetailsService，验证登录用户的身份
//                .userDetailsService(this.userDetailsService)
//                // 使用BCrypt进行密码的hash
//                //.passwordEncoder(passwordEncoder())
//        ;
//    }
//        // enable in memory based authentication with a user named "user" and "admin"
//        //.inMemoryAuthentication().withUser("user").password("password").roles("USER")
//        //.and().withUser("admin").password("password").roles("USER", "ADMIN");
//
//
//    //@Autowired
//    //JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
//    //@Bean
//    //public PasswordEncoder passwordEncoder() {
//    //    return new BCryptPasswordEncoder();
//    //}
//
//    //@Bean
//    //public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter()   {
//    //    return new JwtAuthenticationTokenFilter();
//    //}
//    //
//    //@Bean
//    //public FilterRegistrationBean jwtFilterRegistration(){
//    //    FilterRegistrationBean filterRegistration=new FilterRegistrationBean();
//    //    filterRegistration.setFilter(jwtAuthenticationTokenFilter());
//    //    filterRegistration.setOrder(Ordered.HIGHEST_PRECEDENCE);
//    //    filterRegistration.setName("jwtFilter");
//    //    return filterRegistration;
//    //}
//}
