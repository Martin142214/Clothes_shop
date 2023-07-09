package com.example.demo.test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import com.example.demo.test.Services.UserDetailsServiceImplementation;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfiguration {
 
    @Autowired
    private UserDetailsServiceImplementation myUserDetailsService;
     

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_ADMIN > ROLE_USER";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;

    }

    @Bean
    public DefaultWebSecurityExpressionHandler webSecurityExprHandler() {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }

    /*@Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImplementation();
    }*/
     
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JavaMailSender javaMailSender() {
        return new JavaMailSenderImpl();
    }
 
    
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService);
    }

    @Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.authorizeHttpRequests()
                //Only ADMIN role rights
                .antMatchers(HttpMethod.GET, "/shoes")
                .hasRole("ADMIN")
                //Only USER role rights
                .antMatchers(HttpMethod.GET, "/shoes/checkout")
                .hasAnyRole("USER")
                .antMatchers(HttpMethod.GET, "/shoes/get")
                .hasAnyRole("USER")
                //Both USER and ADMIN role rights
                .antMatchers(HttpMethod.GET, "/cars")
                .hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/sneakers")
                .hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/sneakers/**")
                .hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/auction")
                .hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/auction/**")
                .hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/shoes/favorites")
                .hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/shoes/**")
                .hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/shoes/**")
                .hasAnyRole("USER", "ADMIN")
                .and()
			.formLogin()
				.loginPage("/user/login")
                .defaultSuccessUrl("/", true)
				.permitAll()
                .and()
			.logout((logout) -> logout.permitAll()
                                      .logoutSuccessUrl("/"));

		return http.build();
    }

}

