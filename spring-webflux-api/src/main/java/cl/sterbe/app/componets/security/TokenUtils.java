package cl.sterbe.app.componets.security;

import cl.sterbe.app.exceptions.TokenError;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;
import java.util.Date;

public class TokenUtils {

    private final static String ACCESO_TOKEN_SECRETO = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVjOWYzYWI2NzY2Mjg2NDYyNDY0YTczNCIsIm5hbWUiOiJSYW5keSIsImF2YXRhciI6Ii8vd3d3LmdyYXZhdGFyLmNvbS9hdmF0YXIvMTNhN2MyYzdkOGVkNTNkMDc2MzRkOGNlZWVkZjM0NTE_cz0yMDAmcj1wZyZkPW1tIiwiaWF0IjoxNTU0NTIxNjk1LCJleHAiOjE1NTQ1MjUyOTV9._SxRurShXS-SI3SE11z6nme9EoaD29T_DBFr8Qwngkg";
    private final static Long ACCESO_TOKEN_VALIDACION_MINUTO = 60_000L;

    public static Mono<String> generateToken(UserDetails user){
        return Mono.just(Jwts
                .builder()
                .setSubject(user.getUsername())
                .claim("authorities", user.getAuthorities())
                .signWith(Keys.hmacShaKeyFor(ACCESO_TOKEN_SECRETO.getBytes()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (ACCESO_TOKEN_VALIDACION_MINUTO * 60)))
                .compact());
    }

    public static Mono<Claims> authenticationToken(String token) {
        return Mono.fromCallable(() -> {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(ACCESO_TOKEN_SECRETO.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims;
        }).onErrorResume(error -> Mono.error(new TokenError("token invalid")));
    }
}
