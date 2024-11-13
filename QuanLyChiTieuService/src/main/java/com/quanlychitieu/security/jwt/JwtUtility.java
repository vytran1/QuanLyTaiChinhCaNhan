package com.quanlychitieu.security.jwt;

import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.quanlychitieu.common.user.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtility {
	private static final String SECRET_KEY_ALGORITHM = "HmacSHA512";
	@Value("${app.security.jwt.issuer}")
	private String issueName;
	
	@Value("${app.security.jwt.secret}")
	private String secretKey;
	
	@Value("${app.security.jwt.access-token.expiration}")
	private int accessTokenExpiration;
    
	public String generateAccessToken(User user) {
		if(user == null || user.getUserId() == null || user.getFirstName() == null  || user.getLastName() == null) {
			throw new IllegalArgumentException("user object is null or its field have null values");
		}
		long expirationTimeMills = System.currentTimeMillis() + (accessTokenExpiration * 60000);
		String subject =String.format("%s,%s,%s",user.getUserId(),user.getFirstName(),user.getLastName());
		return Jwts.builder().subject(subject)
				.issuer(issueName)
				.issuedAt(new Date())
				.expiration(new Date(expirationTimeMills)).signWith(Keys.hmacShaKeyFor(secretKey.getBytes()),Jwts.SIG.HS512).compact();
		
	} 
	 
	
	public Claims validateAccessToken(String token) throws JwtValidationException {
		try {
			SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(),SECRET_KEY_ALGORITHM); 
			return Jwts.parser().verifyWith(keySpec).build().parseSignedClaims(token).getPayload();
		}catch(ExpiredJwtException ex) {
			throw new JwtValidationException("Access token expired",ex);
		}catch(IllegalArgumentException ex) {
			throw new JwtValidationException("Access token is illegal",ex);
		}catch(MalformedJwtException ex) {
			throw new JwtValidationException("Access token is not well formerd",ex);
		}catch(UnsupportedJwtException ex) {
			throw new JwtValidationException("Access token is not supported",ex);
		}
	}
	
	public String getIssueName() {
		return issueName;
	}

	public void setIssueName(String issueName) {
		this.issueName = issueName;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public int getAccessTokenExpiration() {
		return accessTokenExpiration;
	}

	public void setAccessTokenExpiration(int accessTokenExpiration) {
		this.accessTokenExpiration = accessTokenExpiration;
	}

	public static String getSecretKeyAlgorithm() {
		return SECRET_KEY_ALGORITHM;
	}

}
