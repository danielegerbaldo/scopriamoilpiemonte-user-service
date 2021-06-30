package TAASS.ServiceDBUtenti.services;

import TAASS.ServiceDBUtenti.models.Provider;
import TAASS.ServiceDBUtenti.models.Utente;
import TAASS.ServiceDBUtenti.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoogleUserService {
    @Autowired
    UtenteRepository userRepo;
    public void processOAuthPostLogin(String email) {
        Utente existUser = userRepo.findByEmail(email);

        if (existUser == null) {
            Utente newUser = new Utente();
            newUser.setEmail(email);
            newUser.setProvider(Provider.GOOGLE);
            //newUser.setEnabled(true);
            userRepo.save(newUser);
        }

    }
}
