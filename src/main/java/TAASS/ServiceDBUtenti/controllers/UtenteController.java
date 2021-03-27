package TAASS.ServiceDBUtenti.controllers;

import TAASS.ServiceDBUtenti.classiComode.RichiestaLogin;
import TAASS.ServiceDBUtenti.models.Utente;
import TAASS.ServiceDBUtenti.repositories.UtenteRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.*;

@RestController
@RequestMapping("/api/v1/utente")
public class UtenteController {
    @Autowired
    private UtenteRepository utenteRepository;

    @GetMapping
    public List<Utente> getAllUtenti(){
        //System.out.println("restituisco tutti gli utenti");
        List<Utente> utenti = new ArrayList<>();
        utenteRepository.findAll().forEach(utenti::add);
        System.out.println(">richiesta lista utenti, quantita' trovata: " + utenti.size());
        return utenti;
    }

    /*@PostMapping
    public Utente postUtente(@RequestBody Utente utente){
        Utente nuovoUtente = utenteRepository.save(new Utente(utente.getNome(), utente.getCognome(), utente.getCf(),
                utente.getTelefono(), utente.getComuneResidenza(), utente.getEmail(), utente.getPassword(),
                utente.getRuolo()));
        return nuovoUtente;
    }*/

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

    /*@PostMapping("/login")
    public ResponseEntity<String> fakeLogin(@RequestBody Map richiestaLogin){
        String email = richiestaLogin.get("email").toString().trim();
        String password = richiestaLogin.get("password").toString().trim();
        System.out.println(">richiesta login: p: " + password);
        System.out.println(">richiesta login: e: " + email);
        List<Utente> utenti = utenteRepository.findByEmail(email);
        System.out.println(">utenti trovati: " + utenti.size());
        Map<String, String> risposta = new HashMap<>();
        Gson gson = new Gson();
        ResponseEntity<String> responseEntity;
        if(utenti.size() == 1){
            if(utenti.get(0).getPassword().equals(password)){
                System.out.println(">Ho trovato una corrispondenza");
                Utente utente = utenti.get(0);
                risposta.put("nome", utente.getNome());
                risposta.put("cognome", utente.getCognome());
                risposta.put("ruolo", utente.getRuolo());
                risposta.put("utente_id", String.valueOf(utente.getId()));
                if(utente.getRuolo().equals("sindaco")){
                    risposta.put("comune_id", String.valueOf(utente.getComune()));
                }
                responseEntity = new ResponseEntity<>(gson.toJson(risposta), HttpStatus.OK);
            }else{
                System.out.println(">NON ho trovato una corrispondenza");
                System.out.println(">utenti trovati: ");
                risposta.put("errore", "utente o password sconosciuta");
                responseEntity =  new ResponseEntity<>(gson.toJson(risposta), HttpStatus.BAD_REQUEST);
            }
        }else{
            System.out.println(">NON ho trovato una corrispondenza");
            System.out.println(">utenti trovati: ");
            risposta.put("errore", "utente o password sconosciuta");
            responseEntity =  new ResponseEntity<>(gson.toJson(risposta), HttpStatus.BAD_REQUEST);
        }

        System.out.println(">Response:");
        System.out.println("\t$ c: " + responseEntity.getStatusCode() + " = " + responseEntity.getStatusCodeValue());
        System.out.println("\t$ h: " + responseEntity.getHeaders());
        System.out.println("\t$ b: " + responseEntity.getBody());
        System.out.println();
        return responseEntity;
    }*/

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
    }
}
