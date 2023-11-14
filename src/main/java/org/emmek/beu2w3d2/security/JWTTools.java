package org.emmek.beu2w3d2.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.emmek.beu2w3d2.entities.User;
import org.emmek.beu2w3d2.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTTools {
    @Value("${spring.jwt.secret}")
    private String secret;

    public String createToken(User user) {

        return Jwts.builder().setSubject(String.valueOf(user.getId()))// Subject <-- A chi appartiene il token
                .setIssuedAt(new Date(System.currentTimeMillis())) // Data di emissione (IAT - Issued At)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // Data di scadenza (Expiration Date)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes())).compact();

    } // Si utilizza al login

    public void verifyToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build().parse(token);
        } catch (Exception ex) {
            throw new UnauthorizedException("Token not valid! Please login again");
        }

    } // Si utilizza in tutte le request autenticate

    public String extractIdFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build().parseClaimsJws(token).getBody().getSubject();
        // Nel body (payload) del token ci sono il subject( che è l'ID dell'utente), la data di emissione, e la data di scadenza.
        // A noi interessa l'id dell'utente
    }
}