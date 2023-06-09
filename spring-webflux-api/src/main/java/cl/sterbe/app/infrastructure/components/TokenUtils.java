package cl.sterbe.app.infrastructure.components;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

import java.util.Date;

public class TokenUtils {

    private final static Long ACCESO_TOKEN_VALIDACION_MINUTO = 60_000L;

    private final static String ACCESS_TOKEN_SECRET = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVjOWYzYWI2NzY2Mjg2NDYyNDY0YTczNCIsIm5hbWUiOiJSYW5keSIsImF2YXRhciI6Ii8vd3d3LmdyYXZhdGFyLmNvbS9hdmF0YXIvMTNhN2MyYzdkOGVkNTNkMDc2MzRkOGNlZWVkZjM0NTE_cz0yMDAmcj1wZyZkPW1tIiwiaWF0IjoxNTU0NTIxNjk1LCJleHAiOjE1NTQ1MjUyOTV9._SxRurShXS-SI3SE11z6nme9EoaD29T_DBFr8Qwngkg";

    public static Mono<String> generateToken(UserDetails user, Integer time){
        return Mono.just(Jwts
                .builder()
                .setSubject(user.getUsername())
                .claim("authorities", user.getAuthorities())
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (ACCESO_TOKEN_VALIDACION_MINUTO * time)))
                .compact());
    }

    public static Mono<Claims> authenticationToken(String token) {
        return Mono.just(token)
                .flatMap(t -> {
                    try {
                        Claims claims = Jwts.parserBuilder()
                                .setSigningKey(ACCESS_TOKEN_SECRET.getBytes())
                                .build()
                                .parseClaimsJws(token)
                                .getBody();
                        return Mono.just(claims);
                    }catch (JwtException e){
                     //   return Mono.error(new TokenErrorException("token invalid"));
                        return Mono.empty();
                    }
                });
    }
}
