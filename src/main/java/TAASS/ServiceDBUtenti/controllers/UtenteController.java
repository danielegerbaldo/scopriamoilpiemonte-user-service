package TAASS.ServiceDBUtenti.controllers;

import TAASS.ServiceDBUtenti.exception.ForbiddenException;
import TAASS.ServiceDBUtenti.models.Utente;
import TAASS.ServiceDBUtenti.repositories.UtenteRepository;
import TAASS.ServiceDBUtenti.requests.ChangeRoleRequest;
import TAASS.ServiceDBUtenti.requests.LoginRequest;
import TAASS.ServiceDBUtenti.requests.SignUpRequest;
import TAASS.ServiceDBUtenti.response.LoginResponse;
import TAASS.ServiceDBUtenti.response.UserDto;
import TAASS.ServiceDBUtenti.services.SecureUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/v1")
public class UtenteController {

    private SecureUserService userService;
    private final UtenteRepository  utenteRepository;

    public UtenteController(SecureUserService userService, UtenteRepository utenteRepository) {
        this.userService = userService;
        this.utenteRepository = utenteRepository;
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
    public ResponseEntity<List<Utente>> getAllUser(HttpServletRequest requestHeader) throws RuntimeException {

        //X-auth-user-role: header da controllare
        //AUTH: prendo l'autorizzazione dall'header e verifico che sia admin; questo va a sostituire il "@PreAuthorize("hasRole('ROLE_ADMIN')")"
        String auth = requestHeader.getHeader("X-auth-user-role");
        if(!auth.equals("ROLE_ADMIN")){
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        //System.out.println("Authorization: " + requestHeader);
        try {
            return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }

    @PostMapping(value = "/utente/getUsersByIdList")
    public List<Utente> getUsersByIdList(HttpServletRequest requestHeader, @RequestBody List<Long> ids) {

        /*TODO
            per ora questo metodo è permesso a qualsiasi sindaco o pubblicatore (oltre che agli amministratori)
            bisogna verificare che il sindaco sia sindaco del comune dell'evento o che il pubblicatore abbia
            pubblicato davvero quell'evento
         */
        //AUTH: admin, sindaco, pubblicatore
        String auth = requestHeader.getHeader("X-auth-user-role");
        if(!(auth.equals("ROLE_ADMIN") || auth.equals("ROLE_MAYOR") || auth.equals("ROLE_PUBLISHER") )){
            throw new ForbiddenException();
            //return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        List<Utente> utenti = new ArrayList<Utente>();
        for (long id : ids) {
            if (utenteRepository.findById(id).isPresent()) {
                utenti.add(utenteRepository.findById(id).get());
            }
        }

        return utenti;
    }

    @GetMapping(value = "/utente/getDipendentiDiComune/{idComune}")
    public ResponseEntity<List<Utente>> getDipendentiDiComune(HttpServletRequest requestHeader, @PathVariable long idComune) throws RuntimeException {

        Utente utenteCorrente=null;

        String auth = requestHeader.getHeader("X-auth-user-role");
        Enumeration<String> headers = requestHeader.getHeaderNames();

        long idToken = Long.parseLong(requestHeader.getHeader("X-auth-user-id"));

        //Cerco se chi fa la richiesta e' dipendente del comune
        if(utenteRepository.findById(idToken).isPresent())
            utenteCorrente = utenteRepository.findById(idToken).get();

        if(utenteCorrente==null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //Aturorizzo l' admin o il dindaco del comune richiesto
        if(!(auth.equals("ROLE_ADMIN") || (utenteCorrente.getDipendenteDiComune()==idComune && auth.equals("ROLE_MAYOR")))){
            throw new ForbiddenException();
            //return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }


        System.out.println("Authorization: " + requestHeader);
        try {
                return new ResponseEntity<List<Utente>>(utenteRepository.findAllByDipendenteDiComune(idComune), HttpStatus.OK);

        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping(value = "/utente/getNonDipendenti")
    public ResponseEntity<List<Utente>> getNonDipendenti(HttpServletRequest requestHeader) throws RuntimeException {

        System.out.println(">>>>>/utente/getNonDipendenti");

        Utente utenteCorrente=null;

        String auth = requestHeader.getHeader("X-auth-user-role");
        Enumeration<String> headers = requestHeader.getHeaderNames();

        long idToken = Long.parseLong(requestHeader.getHeader("X-auth-user-id"));

        //Cerco se chi fa la richiesta e' dipendente del comune
        if(utenteRepository.findById(idToken).isPresent())
            utenteCorrente = utenteRepository.findById(idToken).get();

        if(utenteCorrente==null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //Aturorizzo l' admin o il dindaco del comune richiesto
        if(!(auth.equals("ROLE_ADMIN") || (auth.equals("ROLE_MAYOR")))){
            throw new ForbiddenException();
            //return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }


        System.out.println("Authorization: " + requestHeader);
        try {
            List<Utente> utenti = utenteRepository.findNonDipendenti();
            System.out.println(">>> utenti non dipendenti = " + utenti.size());

            return new ResponseEntity<List<Utente>>(utenteRepository.findNonDipendenti(), HttpStatus.OK);

        } catch (Exception e) {
            throw e;
        }
    }

    @PostMapping(value = "/utente/changeRole")
    public ResponseEntity<Utente> changeRole(HttpServletRequest requestHeader, @RequestBody ChangeRoleRequest request) throws RuntimeException {

        String auth = requestHeader.getHeader("X-auth-user-role");
        Enumeration<String> headers = requestHeader.getHeaderNames();

        long idToken = Long.parseLong(requestHeader.getHeader("X-auth-user-id"));

        //AUTH: può accedere solo un admin o sindaco
        if(!(auth.equals("ROLE_ADMIN") || auth.equals("ROLE_MAYOR"))){
            throw new ForbiddenException();
            //return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        try {
            if(utenteRepository.findById(request.getId()).isPresent()) {
                Utente utente = utenteRepository.findById(request.getId()).get();
                utente.setRuoli(request.getRuoli());
                utenteRepository.save(utente);
                utente.setPassword(null);
                return new ResponseEntity<Utente>(utente, HttpStatus.OK);
            }
            else
                return new ResponseEntity(HttpStatus.NOT_FOUND);

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

    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> rimuoviTuttiUtenti(HttpServletRequest requestHeader){
        //AUTH: admin
        String auth = requestHeader.getHeader("X-auth-user-role");
        if(!auth.equals("ROLE_ADMIN")){
            return new ResponseEntity<>("Non sei autorizzato", HttpStatus.FORBIDDEN);
        }
        utenteRepository.deleteAll();
        return new ResponseEntity<>("Tutti gli utenti sono stati cancellati con successo", HttpStatus.OK);
    }

    @GetMapping(value = "/validateToken")
    public ResponseEntity<UserDto> validateToken(@RequestParam String token) throws RuntimeException {

        System.out.println("Authorization: " + token);

        return new ResponseEntity<UserDto>(userService.getAuth(token),HttpStatus.OK);

    }

    @GetMapping(value = "/utente/getUser/{id}")
    public ResponseEntity<Utente> getUser(HttpServletRequest requestHeader, @PathVariable long id) throws RuntimeException {
        // Questo metodo non richiede autorizzazione, TODO: filtrarlo dal gateway
        String auth = requestHeader.getHeader("X-auth-user-role");
        Enumeration<String> headers = requestHeader.getHeaderNames();
        /*String head = headers.nextElement() ;
        while(head != null){
            System.out.println("/utente/getUser/{id}: headers = " + head);
            head = headers.nextElement() ;
        }*/
        System.out.println("/utente/getUser/{id}: X-auth-user-id pt2 = " + requestHeader.getHeader("X-auth-user-id"));
        long idToken = Long.parseLong(requestHeader.getHeader("X-auth-user-id"));
        System.out.println("/utente/getUser/{id}: X-auth-user-id pt3 = " + idToken);
        //AUTH: può accedere solo un admin o se l'id calcolato dal token coincide con l'id ricevuto nella richiesta
        if(!(auth.equals("ROLE_ADMIN") || id == idToken)){
            throw new ForbiddenException();
            //return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        System.out.println("/utente/getUser/{id}: superato l'if");

        System.out.println("Authorization: " + requestHeader);
        try {
            if(utenteRepository.findById(id).isPresent())
                return new ResponseEntity<Utente>(utenteRepository.findById(id).get(), HttpStatus.OK);
            else
                return new ResponseEntity(HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            throw e;
        }
    }

    @PutMapping("/utente/updateUser/{id}")
    public ResponseEntity<Utente> updateUser(HttpServletRequest requestHeader, @PathVariable(value = "id") Long id,
                                             @RequestBody Utente utente) throws RuntimeException {

        String auth = requestHeader.getHeader("X-auth-user-role");
        long idToken = Long.parseLong(requestHeader.getHeader("X-auth-user-id"));

        if(!utenteRepository.findById(id).isPresent()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Utente userToBeUpdated = utenteRepository.findById(id).get();

        //AUTH: Devo essere ADMIN o l'utente stesso
        if(!(auth.equals("ROLE_ADMIN") || id == idToken)) {
            throw new ForbiddenException();
        }


        //UPDATING user

        userToBeUpdated.setDipendenteDiComune(utente.getDipendenteDiComune());
        userToBeUpdated.setCognome(utente.getCognome());
        userToBeUpdated.setNome(utente.getNome());
        userToBeUpdated.setEmail(utente.getEmail());
        userToBeUpdated.setCf(utente.getCf());
        userToBeUpdated.setComuneResidenza(utente.getComuneResidenza());
        userToBeUpdated.setTelefono(utente.getTelefono());

        utenteRepository.save(userToBeUpdated);

        return new ResponseEntity<Utente>(userToBeUpdated, HttpStatus.OK);
    }

    @PutMapping("/utente/setDipendenteDiComune")
    public ResponseEntity<Utente> setDipendenteDiComune(HttpServletRequest requestHeader, @RequestParam Long idUtente,
                                             @RequestParam Long idComune) throws RuntimeException {

        String auth = requestHeader.getHeader("X-auth-user-role");
        long idToken = Long.parseLong(requestHeader.getHeader("X-auth-user-id"));
        long comuneDipendente = requestHeader.getHeader("X-auth-user-comune-dipendente-id")!=null?Long.parseLong(requestHeader.getHeader("X-auth-user-comune-dipendente-id")):-1;

        if(!utenteRepository.findById(idUtente).isPresent()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Utente userToBeUpdated = utenteRepository.findById(idUtente).get();


        //AUTH: Devo essere ADMIN o il sindaco del comune da impostare
        if(!(auth.equals("ROLE_ADMIN") || (auth.equals("ROLE_MAYOR") && comuneDipendente==idComune))) {
            throw new ForbiddenException();
        }


        //UPDATING user

        userToBeUpdated.setDipendenteDiComune(idComune);
        utenteRepository.save(userToBeUpdated);

        return new ResponseEntity<Utente>(userToBeUpdated, HttpStatus.OK);
    }

}
