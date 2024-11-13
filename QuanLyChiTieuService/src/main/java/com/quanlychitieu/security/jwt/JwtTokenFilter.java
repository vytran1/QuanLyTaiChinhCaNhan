package com.quanlychitieu.security.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.quanlychitieu.GlobalHandleException;
import com.quanlychitieu.common.user.User;
import com.quanlychitieu.security.CustomUserDetails;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenFilter.class);

	@Autowired
	private JwtUtility jwtUtility;
	
	@Autowired
	@Qualifier("handlerExceptionResolver")
	private HandlerExceptionResolver handlerExceptionResolver;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
        if(!hasAuthorizationBearerToken(request)) {
        	filterChain.doFilter(request, response);
        	return;
        }
        String token = getBearerToken(request);
        LOGGER.info("Token: " + token);
        try {
			Claims claims = jwtUtility.validateAccessToken(token);
			UserDetails customUserDetails = getUserDetails(claims);
			setAuthenticationContext(customUserDetails,request);
			filterChain.doFilter(request, response);
			clearAuthenticationContext();
		} catch (JwtValidationException e) {
			// TODO Auto-generated catch block
			LOGGER.error(e.getMessage(),e);
//			filterChain.doFilter(request, response);
//			return;
			handlerExceptionResolver.resolveException(request, response,null, e);
		}
	}

	private void clearAuthenticationContext() {
		// TODO Auto-generated method stub
		SecurityContextHolder.clearContext();

	}

	private void setAuthenticationContext(UserDetails customUserDetails, HttpServletRequest request) {
		// TODO Auto-generated method stub
		var authentication = new UsernamePasswordAuthenticationToken(customUserDetails,null,null);
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
	}

	private UserDetails getUserDetails(Claims claims) {
		// TODO Auto-generated method stub
		String subject = (String) claims.get(Claims.SUBJECT);
		String[] array = subject.split(",");
		Integer userId = Integer.valueOf(array[0]);
		String firstName = array[1];
		String lastName = array[2];
		User user = new User();
		user.setUserId(userId);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		LOGGER.info("Info user is parsed from JWT token: " + user.getUserId() + ", " + user.getFullName());
		return new CustomUserDetails(user);
	}

	private boolean hasAuthorizationBearerToken(HttpServletRequest request) {
		// TODO Auto-generated method stub
		String header = request.getHeader("Authorization");
		LOGGER.info("Authorization Header: " + header);
		if(ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) {
			return false;
		}
		return true;
	}
	
	private String getBearerToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		String[] array = header.split(" ");
		if(array.length == 2) {
			return array[1];
		}
		return null;
	}
	
	

}
