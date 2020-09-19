package org.kiworkshop.blind.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private RestLoginSuccessHandler restLoginSuccessHandler;

    @Autowired
    private RestLoginFailureHandler restLoginFailureHandler;

    @Autowired
    private RestLogoutSuccessHandler logoutSuccessHandler;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().maximumSessions(-1).sessionRegistry(sessionRegistry());
        http.sessionManagement().sessionFixation().migrateSession()
            .sessionAuthenticationStrategy(registerSessionAuthStr());

        http
            .csrf().disable()
            .exceptionHandling()
            .authenticationEntryPoint(restAuthenticationEntryPoint)
            .and()
            .formLogin()
            .disable()
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessHandler(logoutSuccessHandler)
            .invalidateHttpSession(true)
            .and()
            .authorizeRequests()
            .antMatchers("/admin**").hasRole("ADMIN")
            .antMatchers("/posts/**").authenticated()
            .antMatchers("/home").authenticated()
            .and()
            .addFilterAt(getAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    protected JsonUsernamePasswordAuthenticationFilter getAuthenticationFilter() {
        JsonUsernamePasswordAuthenticationFilter authFilter = new JsonUsernamePasswordAuthenticationFilter();
        try {
            authFilter.setFilterProcessesUrl("/login");
            authFilter.setUsernameParameter("email");
            authFilter.setPasswordParameter("password");
            authFilter.setAuthenticationManager(this.authenticationManagerBean());
            authFilter.setAuthenticationSuccessHandler(restLoginSuccessHandler);
            authFilter.setAuthenticationFailureHandler(restLoginFailureHandler);
            authFilter.setSessionAuthenticationStrategy(registerSessionAuthStr());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authFilter;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        SessionRegistry sessionRegistry = new SessionRegistryImpl();
        return sessionRegistry;
    }

    @Bean
    public RegisterSessionAuthenticationStrategy registerSessionAuthStr() {
        return new RegisterSessionAuthenticationStrategy(sessionRegistry());
    }
}
