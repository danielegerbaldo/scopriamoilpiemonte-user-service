package TAASS.ServiceDBUtenti.security.config;


import TAASS.ServiceDBUtenti.models.CustomOAuth2User;
import TAASS.ServiceDBUtenti.response.LoginResponse;
import TAASS.ServiceDBUtenti.security.token.IJwtTokenProviderService;
import TAASS.ServiceDBUtenti.security.token.JwtTokenFilterConfigurer;
import TAASS.ServiceDBUtenti.services.CustomOAuth2UserService;
import TAASS.ServiceDBUtenti.services.GoogleUserService;
import TAASS.ServiceDBUtenti.services.SecureUserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private IJwtTokenProviderService jwtTokenProviderService;

    private GoogleUserService googleUserService;


    @Autowired
    private CustomOAuth2UserService oauthUserService;

    public WebSecurityConfig(IJwtTokenProviderService jwtTokenProviderService, CustomOAuth2UserService oauthUserService, GoogleUserService googleUserService) {
        this.jwtTokenProviderService = jwtTokenProviderService;
        this.oauthUserService = oauthUserService;
        this.googleUserService = googleUserService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .antMatchers("/", "/login", "/oauth2/**").permitAll()
                .anyRequest().permitAll()
                //.and()
                //.formLogin().permitAll()
                .and()
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(oauthUserService)
                .and()
                .successHandler(new AuthenticationSuccessHandler() {

                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                        Authentication authentication) throws IOException, ServletException {

                        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();


                        System.out.println("ATTRIBUTES: "+ oauthUser.getAttributes().toString());

                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("application/json");
                        response.setStatus(200);
                        response.getWriter().write(new Gson().toJson(googleUserService.processOAuthPostLogin(oauthUser)));
                    }
                })
                .and()
                .logout().logoutSuccessUrl("/").permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/403");

        // Apply JWT
        http.apply(new JwtTokenFilterConfigurer(jwtTokenProviderService));
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return authenticationManager();
    }

}