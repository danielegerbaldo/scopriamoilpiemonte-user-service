package TAASS.ServiceDBUtenti.services;

import TAASS.ServiceDBUtenti.models.CustomOAuth2User;
import TAASS.ServiceDBUtenti.models.Provider;
import TAASS.ServiceDBUtenti.models.Role;
import TAASS.ServiceDBUtenti.models.Utente;
import TAASS.ServiceDBUtenti.repositories.UtenteRepository;
import TAASS.ServiceDBUtenti.response.LoginResponse;
import TAASS.ServiceDBUtenti.security.token.IJwtTokenProviderService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class GoogleUserService {
    final
    UtenteRepository userRepo;
    private final IJwtTokenProviderService jwtTokenProviderService;

    public GoogleUserService(UtenteRepository userRepo, IJwtTokenProviderService jwtTokenProviderService) {
        this.userRepo = userRepo;
        this.jwtTokenProviderService = jwtTokenProviderService;
    }

    public LoginResponse processOAuthPostLogin(CustomOAuth2User user) {

        Utente existUser = userRepo.findByEmail(user.getEmail());
        LoginResponse response = new LoginResponse();

        if (existUser == null) {
            Utente newUser = new Utente();
            newUser.setEmail(user.getEmail());
            newUser.setProvider(Provider.GOOGLE);
            newUser.setEmailVerified(true);
            newUser.setPictureUrl(user.getPictureUrl());
            newUser.setNome(user.getGivenName());
            newUser.setCognome(user.getFamilyName());
            newUser.setPassword(alphaNumericString(12));
            List<Role> ruolo = new ArrayList<Role>();
            ruolo.add(Role.ROLE_CLIENT);
            newUser.setRuoli(ruolo);
            userRepo.save(newUser);
        }

        Utente dbUser = userRepo.findByEmail(user.getEmail());
        response.setId(dbUser.getId());
        response.setEmail(dbUser.getEmail());
        response.setUserName(dbUser.getNome());
        response.setAccessToken(jwtTokenProviderService.createToken(dbUser.getEmail(), dbUser.getRuoli()));

        return response;
    }

    public static String alphaNumericString(int len) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghilmnopqrstuvwxyz";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }
}
