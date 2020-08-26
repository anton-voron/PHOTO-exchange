package com.voron.petproject.jwt;


import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtProvider {

	@Value("${jwt.token.secret}")
	private String SECRET_KEY;

	@Value("${jwt.token.expiration}")
	private long EXPIRATION_TIME;

	public JwtProvider() {
	}

	public String generateToken(String username, int userId) {
		// The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		Date now = new Date();
		System.out.println("\n===========>>>>>>>> IN JwtProvider.generateToken(), username: " + username);
		Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);

		Claims claims = Jwts.claims()
				.setId(String.valueOf(userId))
				.setSubject(username);
		
		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(expirationDate)
				.signWith(signatureAlgorithm, secretKeyEncoder()).compact();
	}

	public Key secretKeyEncoder() {
		// We will sign our JWT with our ApiKey secret
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
		Key signKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
		return signKey;
	}

	public boolean validateToken(String token) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(secretKeyEncoder()).parseClaimsJws(token);

			if (claims.getBody().getExpiration().before(new Date())) {
				return false;
			}

			return true;
		} catch (JwtException | IllegalArgumentException e) {
			throw new JwtAuthenticationException("JWT token is expired or invalid");
		}
	}

	public String getUsernameFormToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(secretKeyEncoder()).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

	public int getUserIdFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(secretKeyEncoder()).parseClaimsJws(token).getBody();
		return Integer.valueOf(claims.getId());
	}

	public Date extractIssueDateFromToken(String token) {
//		Jws<Claims> claims = Jwts.parser().setSigningKey(secretKeyEncoder()).parseClaimsJws(token);
//		String date =  String.valueOf(claims.getBody().getIssuedAt());
//		System.out.println(date);
		
		return new Date();
	}
	
	public Date extractExpirationDateFromToken(String token) {
//		try {
//			Claims claims = Jwts.parser().setSigningKey(secretKeyEncoder()).parseClaimsJws(token).getBody();
//			Key  key = secretKeyEncoder();
//			System.out.println(key);
//		} catch (Exception e) {
//			System.out.println("FUUUCK!");
//			System.out.println(e);
//			e.printStackTrace();
//		}

		
//		String date =  String.valueOf(claims.getBody().getExpiration());
//		System.out.println(date);
		return new Date();
	}

}
