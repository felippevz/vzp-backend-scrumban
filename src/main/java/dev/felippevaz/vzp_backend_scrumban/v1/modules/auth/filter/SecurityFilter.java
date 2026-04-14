package dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.filter;

import dev.felippevaz.vzp_backend_scrumban.v1.commons.domain.ErrorData;
import dev.felippevaz.vzp_backend_scrumban.v1.commons.exceptions.RequestException;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.service.TokenService;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.domain.JWTUserDATA;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public SecurityFilter(TokenService tokenService, HandlerExceptionResolver handlerExceptionResolver) {
        this.tokenService = tokenService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {

            String token = extractToken(request);

            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            Optional<JWTUserDATA> userData = tokenService.validateToken(token);

            if (userData.isPresent()) {

                JWTUserDATA user = userData.get();
                var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.role().getName()));

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        authorities
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

                filterChain.doFilter(request, response);

            } else {
                throw new RequestException(ErrorData.INVALID_TOKEN);
            }

        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }

    private String extractToken(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        String initToken = "Bearer ";

        if (authHeader == null || !authHeader.startsWith(initToken)) {
            return null;
        }

        return authHeader.replace(initToken, Strings.EMPTY);
    }
}