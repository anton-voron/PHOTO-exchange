package com.voron.petproject.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.voron.petproject.auth.CustomUserDetails;
import com.voron.petproject.auth.CustomUserDetailsService;

import static org.springframework.util.StringUtils.hasText;

@Component
public class JwtFilter extends GenericFilterBean {

	private final String AUTHORIZATION = "Authorization";

	private JwtProvider jwtProvider;
	private CustomUserDetailsService customUserDetailsService;
	

	public JwtFilter(JwtProvider jwtProvider, CustomUserDetailsService customUserDetailsService) {
		this.jwtProvider = jwtProvider;
		this.customUserDetailsService = customUserDetailsService;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String token = getToken((HttpServletRequest) request);

		if (token != null && jwtProvider.validateToken(token)) {

			String username = jwtProvider.getUsernameFormToken(token);

			CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(username);

			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(customUserDetails, "",
					customUserDetails.getAuthorities());

			SecurityContextHolder.getContext().setAuthentication(auth);
		}
		chain.doFilter(request, response);

	}

	private String getToken(HttpServletRequest http) {
		String bearer = "Bearer ";
		String authHeader = http.getHeader(AUTHORIZATION);
		if (hasText(authHeader) && authHeader.startsWith(bearer)) {
			return authHeader.substring(bearer.length());
		}
		return null;
	}

	
}
