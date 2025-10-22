package com.pragma.powerup.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String SECRET = "zW4nF8kP2rT7vX1yL9hG5bC3jM6qS0dE8oV1wA4iU7zW4nF8kP2rT7vX1yL9hG5b";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        try {
            String token = extractTokenFromRequest(request);
            
            if (token != null && validateToken(token)) {
                String correo = getCorreoFromToken(token);
                String rol = getRolFromToken(token);
                
                if (correo != null && rol != null) {
                    List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(rol));
                    UsernamePasswordAuthenticationToken auth = 
                        new UsernamePasswordAuthenticationToken(correo, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    log.info("Usuario autenticado: {} con rol: {}", correo, rol);
                }
            }
        } catch (Exception e) {
            log.error("Error en autenticación JWT: {}", e.getMessage());
        }
        
        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("Token JWT inválido: {}", e.getMessage());
            return false;
        }
    }

    private String getCorreoFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            log.error("Error extrayendo correo del token: {}", e.getMessage());
            return null;
        }
    }

    private String getRolFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
            return claims.get("rol", String.class);
        } catch (Exception e) {
            log.error("Error extrayendo rol del token: {}", e.getMessage());
            return null;
        }
    }
}
