package com.voron.petproject.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.voron.petproject.jwt.JwtFilter;

@Configurable
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static final String POSTS_ENDPOINT = "/api/v1/profile/**";
	private static final String REGISTER_ENDPOINT = "/api/v1/registration";
	private static final String LOGIN_ENDPOINT = "/api/v1/login";
	
	@Autowired
	private JwtFilter jwtFilter;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.httpBasic().disable()
		.csrf().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.authorizeRequests()
			.antMatchers(REGISTER_ENDPOINT, LOGIN_ENDPOINT, POSTS_ENDPOINT).permitAll()
			.anyRequest().authenticated()
		.and()
			.logout()
			.logoutUrl("/api/logout")
			.logoutRequestMatcher(new AntPathRequestMatcher("/api/logout", "GET"))
			.clearAuthentication(true)
			.deleteCookies("JSESSIONID")
		.and()
		.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	}

}
