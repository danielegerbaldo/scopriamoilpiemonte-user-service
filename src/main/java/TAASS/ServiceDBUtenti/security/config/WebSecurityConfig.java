package TAASS.ServiceDBUtenti.security.config;


import TAASS.ServiceDBUtenti.security.token.IJwtTokenProviderService;
import TAASS.ServiceDBUtenti.security.token.JwtTokenFilterConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private IJwtTokenProviderService jwtTokenProviderService;

    public WebSecurityConfig(IJwtTokenProviderService jwtTokenProviderService) {
        this.jwtTokenProviderService = jwtTokenProviderService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Entry points
        http.authorizeRequests()
                .antMatchers("/api/v1/login").permitAll()
                .antMatchers("/api/v1/signUp").permitAll()
                .anyRequest().authenticated();

        // If a user try to access a resource without having enough permissions
        http.exceptionHandling().accessDeniedPage("/accessDeniedPage");

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