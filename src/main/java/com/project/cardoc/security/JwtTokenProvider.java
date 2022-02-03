package com.project.cardoc.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

  @Value("${jwt.secret-key")
  private String secretKey;

  private final UserDetailsService userDetailsService;

  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  public String createToken(final String userPk, final List<String> roles) {
    Claims claims = Jwts.claims().setSubject(userPk);   // JWT payload에 저장되는 정보 단위
    claims.put("roles", roles);
    Date now = new Date();
    long tokenValidTime = 30 * 60 * 1000L; // 토큰 유효시간 30분
    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + tokenValidTime))
        .signWith(SignatureAlgorithm.HS512, secretKey)
        .compact();
  }

  /* JWT 토큰에서 인증 정보 조회 */
  public Authentication getAuthentication(final String token) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  /* 토큰에서 회원 정보 추출 */
  public String getUserPk(final String token) {
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
  }


  /* Request의 Header에서 token 값 가져오기 ("X-AUTH-TOKEN" : "{value}") */
  public String resolveToken(final HttpServletRequest request) {
    return request.getHeader("X-AUTH-TOKEN");
  }

  /* 토큰의 유효성 & 만료일자 확인 */
  public boolean validateToken(final String jwtToken) {
    try {
      Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
      return !claims.getBody().getExpiration().before(new Date());
    } catch (Exception e) {
      return false;
    }
  }
}
