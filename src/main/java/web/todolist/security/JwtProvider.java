package web.todolist.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import web.todolist.domain.User;
import web.todolist.dto.response.UserResponse;
import web.todolist.exception.CustomException;
import web.todolist.exception.Error;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Component
public class JwtProvider {

    private Key key;
    private final String AUTHORITY_KEY = "auth";
    private static final long ACCESS_TOKEN_EXPIRE_TIME_MILLIS = 12L * 60L * 60L * 1000L; // 12시간
    private static final long REFRESH_TOKEN_EXPIRE_TIME_MILLIS = 30L * 24L * 60L * 60L * 1000L; // 30일

    @Value("${jwt.key}")
    private String secretKey;

    @Value("${plain.key}")
    private String plainKey;

    @PostConstruct
    public void init() {
//        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.key = getEncodedKey();
    }

    // plain -> encode 시크릿 키 변환
    private Key getEncodedKey() {
        String keyBase64Encoded = Base64.getEncoder().encodeToString(plainKey.getBytes());
        return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }

    // 시크릿 키 반환
    public Key getSecretKey() {
//        return key == null ? Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)) : key;
        return key == null ? getEncodedKey() : key;
    }

    // 토큰 Response 생성
    public UserResponse.Token generateTokenResponse(User user) {
        long now = new Date().getTime();
        return UserResponse.Token.builder()
                .tokenType("Bearer")
                .accessToken(generateToken(user))
                .expiresIn((now + ACCESS_TOKEN_EXPIRE_TIME_MILLIS) / (1000L * 60L * 60L)) // todo: 시간으로 안 나옴 이유를 모르겠음
                .refreshToken(generateRefreshToken(user, new Date(now + REFRESH_TOKEN_EXPIRE_TIME_MILLIS)))
                .refreshTokenExpiresIn((now + REFRESH_TOKEN_EXPIRE_TIME_MILLIS) / (1000L * 60L * 60L))
                .build();
    }

    // 토큰 발급
    public String generateToken(User user) {
        long now = new Date().getTime();
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim(AUTHORITY_KEY, "USER")
                .setExpiration(new Date(now + ACCESS_TOKEN_EXPIRE_TIME_MILLIS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // refresh token 발급
    private String generateRefreshToken(User user, Date expireDate) {
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 유효성 검사
    public boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // 권한 정보 얻기
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        if (ObjectUtils.isEmpty(claims.get(AUTHORITY_KEY))) {
            throw new CustomException(Error.INVALID_TOKEN);
        }

        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(claims.get(AUTHORITY_KEY, String.class)));
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);
    }

    // 토큰의 client 정보 디코딩
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Authorization Header를 통해 인증
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }
}
