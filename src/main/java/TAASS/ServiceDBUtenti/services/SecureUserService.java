package TAASS.ServiceDBUtenti.services;

import TAASS.ServiceDBUtenti.exception.MyCustomException;
import TAASS.ServiceDBUtenti.models.Role;
import TAASS.ServiceDBUtenti.models.Utente;
import TAASS.ServiceDBUtenti.repositories.UtenteRepository;
import TAASS.ServiceDBUtenti.requests.SignUpRequest;
import TAASS.ServiceDBUtenti.response.LoginResponse;
import TAASS.ServiceDBUtenti.response.UserDto;
import TAASS.ServiceDBUtenti.security.token.IJwtTokenProviderService;
import TAASS.ServiceDBUtenti.security.token.JwtTokenProviderService;
import io.jsonwebtoken.Jwts;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;


@Service
public class SecureUserService /*implements ISecureUserService*/ {
    private static Log log = LogFactory.getLog(SecureUserService.class);

    private UtenteRepository secureUserRepository;
    private PasswordEncoder passwordEncoder;
    private IJwtTokenProviderService jwtTokenProviderService;
    private AuthenticationManager authenticationManager;


    public SecureUserService(UtenteRepository secureUserRepository, PasswordEncoder passwordEncoder, IJwtTokenProviderService jwtTokenProviderService, AuthenticationManager authenticationManager) {
        this.secureUserRepository = secureUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProviderService = jwtTokenProviderService;
        this.authenticationManager = authenticationManager;
    }

    //@Override
    public LoginResponse login(String userName, String password) {
        log.info("username: " + userName);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));

            Utente user = secureUserRepository.findByEmail(userName);
            
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setEmail(user.getEmail());
            loginResponse.setUserName(user.getNome());
            loginResponse.setAccessToken(jwtTokenProviderService.createToken(userName, user.getRuoli()));

            log.info("Login successfully");

            return loginResponse;
        } catch (AuthenticationException e) {
            throw new MyCustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    //@Override
    public LoginResponse signUp(SignUpRequest request) {
        if(secureUserRepository.existsByEmail(request.getEmail())){
            throw new MyCustomException("User already exists in system", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Utente user = new Utente();
        user.setNome(request.getNome());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRuoli(request.getRoles());
        user.setCognome(request.getCognome());
        user.setComuneResidenza(request.getComuneResidenza());
        user.setDipendenteDiComune(request.getDipendenteDiComune());
        user.setCf(request.getCf());
        user.setTelefono(request.getTelefono());
        request.setPassword(user.getPassword());

        secureUserRepository.save(user);
        log.info("Register successfully");

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setEmail(user.getEmail());
        loginResponse.setUserName(user.getNome());
        loginResponse.setAccessToken(jwtTokenProviderService.createToken(request.getEmail(), user.getRuoli()));

        return loginResponse;
    }

    /*@Override
    public void removeUser(String userName) {
        if(!secureUserRepository.existsByUsername(userName)){
            throw new RuntimeException("User doesn't exists");
        }
        secureUserRepository.deleteByUsername(userName);
        log.info("User remove successfully");

    }

    @Override
    public UserResponse searchUser(String userName) {
        User user = secureUserRepository.findByUsername(userName);
        if (user == null) {
            throw new MyCustomException("Provided user doesn't exist", HttpStatus.NOT_FOUND);
        }

        UserResponse userResponse = new UserResponse();
        userResponse.setEmail(user.getEmail());
        userResponse.setUserName(user.getUsername());

        return userResponse;
    }*/

    //@Override
    public List<Utente> getAllUser() {
        return secureUserRepository.findAll();
    }


    public UserDto getAuth(String token){
        System.out.println("name: " + jwtTokenProviderService.validateUserAndGetAuthentication(token).getName());

        if(jwtTokenProviderService.validateUserAndGetAuthentication(token).getAuthorities().isEmpty()){
            throw new MyCustomException("Invalid username/password supplied", HttpStatus.NOT_FOUND);
        }else{
            Role role = (Role)jwtTokenProviderService.validateUserAndGetAuthentication(token).getAuthorities().toArray()[0];
            String email = jwtTokenProviderService.getUsername(token);
            long id = secureUserRepository.findByEmail(email).getId();
            long dipendenteDiComune = secureUserRepository.findById(id).get().getDipendenteDiComune();

            System.out.println("Authorized: " + id + " " + email + " " + role.toString());

            return new UserDto(id,email, role.name(), dipendenteDiComune);
        }





    }

    //@Override
    public String refreshToken(String userName) {
        return jwtTokenProviderService.createToken(userName, secureUserRepository.findByEmail(userName).getRuoli());
    }


}
