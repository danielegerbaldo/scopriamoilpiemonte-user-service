package TAASS.ServiceDBUtenti.controllers;

import TAASS.ServiceDBUtenti.models.Utente;
import TAASS.ServiceDBUtenti.repositories.UtenteRepository;
import TAASS.ServiceDBUtenti.requests.LoginRequest;
import TAASS.ServiceDBUtenti.requests.SignUpRequest;
import TAASS.ServiceDBUtenti.response.LoginResponse;
import TAASS.ServiceDBUtenti.response.UserDto;
import TAASS.ServiceDBUtenti.services.SecureUserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/v1")
public class UtenteController {

    private SecureUserService userService;

    public UtenteController(SecureUserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(HttpServletRequest requestHeader, @RequestBody LoginRequest request) throws RuntimeException {
        //login dell'utente in maniera sicura
        System.out.println("controller.login: email: " + request.getEmail());
        LoginResponse loginResponse = userService.login(request.getEmail(), request.getPassword());
        if(loginResponse == null){
            throw new RuntimeException("Login failed. Possible cause : incorrect username/password");
        }else{
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        }
    }

    @PostMapping(value = "/signUp")
    public ResponseEntity<LoginResponse> signUp(HttpServletRequest requestHeader, @RequestBody SignUpRequest request) throws RuntimeException {
        //registrazione dell'utente
        System.out.println("Registro l'utente: " + request.getEmail());
        LoginResponse response;
        try {
            response = userService.signUp(request);
            System.out.println("Registrazione utente: " + request.getEmail() + " avvenuta con successo");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping(value = "/utente/getAllUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Utente>> getAllUser(HttpServletRequest requestHeader) throws RuntimeException {

       /* System.out.println("**************************************************************************************************************");

        Enumeration headerNames = requestHeader.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = requestHeader.getHeader(key);
            System.out.println("HEADER: " + key +" " + value);
        }*/

        System.out.println("Authorization: " + requestHeader);
        try {
            return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }

/*  @PostMapping(value = "/getAuth/{token}")
    public ResponseEntity<String> getAuth(@PathVariable(value="token") String token) throws RuntimeException {

        System.out.println("Authorization: " + token);
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);    //restituisco la stringa senza "Baerer "
            //System.out.println("bearerToken: " + bearerToken);
            return new ResponseEntity<String>("autorizzazione: " + userService.getAuth(token) + ".", HttpStatus.OK);
        }
        return new ResponseEntity<String>("non autorizzato ", HttpStatus.FORBIDDEN);

    }*/

    @GetMapping(value = "/validateToken")
    public ResponseEntity<UserDto> validateToken(@RequestParam String token) throws RuntimeException {

        System.out.println("Authorization: " + token);

        return new ResponseEntity<UserDto>(userService.getAuth(token),HttpStatus.OK);

    }


    /*@Autowired
    private UtenteRepository utenteRepository;

    @GetMapping
    public List<Utente> getAllUtenti(){
        //System.out.println("restituisco tutti gli utenti");
        List<Utente> utenti = new ArrayList<>();
        utenteRepository.findAll().forEach(utenti::add);
        System.out.println(">richiesta lista utenti, quantita' trovata: " + utenti.size());
        return utenti;
    }*/
    /*@PostMapping
    public Utente postUtente(@RequestBody Utente utente){
        Utente nuovoUtente = utenteRepository.save(new Utente(utente.getNome(), utente.getCognome(), utente.getCf(),
                utente.getTelefono(), utente.getComuneResidenza(), utente.getEmail(), utente.getPassword(),
                utente.getRuolo()));
        return nuovoUtente;
    }*/
    /*
    @PostMapping
    public ResponseEntity<Map<String, String>> postUtente(@RequestBody Map<String, String> datiUtente){
        Utente nuovoUtente = new Utente(datiUtente.get("nome"), datiUtente.get("cognome"), datiUtente.getOrDefault("cf", ""),
                datiUtente.getOrDefault("tel", ""), 1, datiUtente.get("email"), datiUtente.get("password"),
                datiUtente.getOrDefault("ruolo", "normale"), Long.parseLong(datiUtente.getOrDefault("comune", "-1").toString()));
        System.out.println(">registrazione nuovo utente: ");
        System.out.println("\t>e: " + datiUtente.get("email"));
        System.out.println("\t>n: " + datiUtente.get("nome"));
        System.out.println("\t>r: " + datiUtente.getOrDefault("ruolo", "normale"));
        nuovoUtente = utenteRepository.save(nuovoUtente);
        Map<String, String> risposta = new HashMap<>();
        risposta.put("risposta", "registrazione avvenuta con successo");
        return  new ResponseEntity<Map<String, String>>(risposta, HttpStatus.OK);
    }

    //questa richiesta dovrà poi essere eliminata quando si sarà implementato spring secure
    @PostMapping("/login")
    public ResponseEntity<String> fakeLogin(@RequestBody Map<String, String> richiestaLogin){
        String email = richiestaLogin.get("email").toString().trim();
        String password = richiestaLogin.get("password").toString().trim();
        System.out.println(">richiesta login: p: " + password);
        System.out.println(">richiesta login: e: " + email);
        List<Utente> utenti = utenteRepository.findByEmail(email);
        System.out.println(">utenti trovati: " + utenti.size());
        Gson gson = new Gson();
        ResponseEntity<String> responseEntity;
        if(utenti.size() == 1){
            responseEntity = new ResponseEntity<>(gson.toJson(utenti.get(0)), HttpStatus.OK);
        }else{
            responseEntity = new ResponseEntity<>(gson.toJson(null), HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> rimuoviTuttiUtenti(){
        utenteRepository.deleteAll();
        return new ResponseEntity<>("Tutti gli utenti sono stati cancellati con successo", HttpStatus.OK);
    }

    @DeleteMapping("/deleteByID")
    public ResponseEntity<String> rimuoviUtentePerID(long id){
        utenteRepository.deleteById(id);
        return new ResponseEntity<>("Tutti gli utenti sono stati cancellati con successo", HttpStatus.OK);
    }

    @GetMapping("/nome/{nome}")     //da aggiungere anche cognome
    public List<Utente> trovaPerNome(@PathVariable String nome){
        List<Utente> utenti = utenteRepository.findByNome(nome);
        return utenti;
    }

    @PutMapping("/aggiorna/{id}")
    public ResponseEntity<Utente> aggiornaUtente(@PathVariable("id") long id, @RequestBody Utente utente){
        Optional<Utente> datiUtente = utenteRepository.findById(id);
        if(datiUtente.isPresent()){
            Utente _utente = datiUtente.get();

            _utente.setNome(utente.getNome());
            _utente.setCognome(utente.getCognome());
            _utente.setCf(utente.getCf());
            _utente.setTelefono(utente.getTelefono());
            _utente.setComuneResidenza(utente.getComuneResidenza());
            return new ResponseEntity<>(utenteRepository.save(_utente), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }*/
}
