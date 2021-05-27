package TAASS.ServiceDBUtenti.security.token;


import TAASS.ServiceDBUtenti.exception.MyCustomException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {

    private IJwtTokenProviderService jwtTokenProviderService;

    //con questo costruttore otteniamo il jwt provider che mi permette di ottenere i token
    public JwtTokenFilter(IJwtTokenProviderService jwtTokenProviderService) {
        this.jwtTokenProviderService = jwtTokenProviderService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProviderService.parseToken(httpServletRequest);  //ottengo il token presente nella richiesta
        try {
            if (token != null && jwtTokenProviderService.validateToken(token)) {    //se esiste il token ed è valito
                //se è autenticato allora lo imposto nell context
                Authentication auth = jwtTokenProviderService.validateUserAndGetAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (MyCustomException ex) {
            //se non riesco a validarlo lancio una eccezione
            SecurityContextHolder.clearContext();
            httpServletResponse.sendError(ex.getHttpStatus().value(), ex.getMessage());
            return;
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}