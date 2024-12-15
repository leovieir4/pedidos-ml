package ml.pedidos.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ml.pedidos.configs.data.ChaveAssinaturaToken;
import ml.pedidos.util.constants.SecurityConstants;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {


    private SecurityConstants securityConstants;
    private ChaveAssinaturaToken chaveAssinaturaToken;


    public JWTAuthorizationFilter(AuthenticationManager authenticationManager,
                                  SecurityConstants securityConstants,
                                  ChaveAssinaturaToken chaveAssinaturaToken) {
        super(authenticationManager);
        this.securityConstants = securityConstants;
        this.chaveAssinaturaToken = chaveAssinaturaToken;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader(securityConstants.getHeader());

        var url = request.getRequestURI();

        if (header == null || !header.startsWith(securityConstants.getTokenPrefix())) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token != null) {
            try {
                Key key = Keys.hmacShaKeyFor(chaveAssinaturaToken.getSecret().getBytes(StandardCharsets.UTF_8));

                // Valida o token e extrai as claims
                Jws<Claims> jwsClaims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token.replace(securityConstants.getTokenPrefix(), ""));

                // Extrai o username do token
                String username = jwsClaims.getBody().getSubject();

                if (username != null) {
                    // Passa o username como principal
                    return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                }

            } catch (JwtException e) {
                // Token inválido - logue o erro ou retorne uma resposta de erro
                return null;
            }
        }
        return null;
    }
}