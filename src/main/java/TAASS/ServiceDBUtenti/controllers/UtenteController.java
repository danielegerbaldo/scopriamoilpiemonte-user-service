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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @PostMapping
    public Utente postUtente(@RequestBody Utente utente){
        Utente nuovoUtente = utenteRepository.save(new Utente(utente.getNome(), utente.getCognome(), utente.getCf(),
                utente.getTelefono(), utente.getComuneResidenza(), utente.getEmail(), utente.getPassword(),
                utente.getRuolo()));
        return nuovoUtente;
    }

    //questa richiesta dovrà poi essere eliminata quando si sarà implementato spring secure
    @PostMapping("/login")
    //@RequestMapping(value="/login",method = RequestMethod.GET)
    public ResponseEntity<String> fakeLogin(/*@RequestParam RichiestaLogin richiestaLogin*/ /*@RequestParam Map<String, String> richiesta*/
        @RequestBody RichiestaLogin richiestaLogin){
        /*System.out.println(">richiesta login: p:" + richiestaLogin.getPassword() + "; e: " + richiestaLogin.getEmail());
        List<Utente> utenti = utenteRepository.findByEmail(richiestaLogin.getEmail());
        if(utenti.size() > 0 && utenti.get(0).getPassword().equals(richiestaLogin.getPassword())){
            //non esiste un utente con quella mail
            return new ResponseEntity<>(utenti.get(0).getRuolo(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Errore Login", HttpStatus.FORBIDDEN);
        }*/
        System.out.println(">richiesta login: p: " + richiestaLogin.getPassword());
        System.out.println(">richiesta login: e: " + richiestaLogin.getEmail());
        String risposta = "li mortacci tua";
        Gson gson = new Gson();
        risposta = gson.toJson(risposta);
        //GsonJsonParser a = new GsonJsonParser();
        ResponseEntity<String> responseEntity = new ResponseEntity<>(risposta, HttpStatus.OK);
        System.out.println(">richiesta login: r: " + responseEntity.toString());
        System.out.println(">richiesta login: r: b: " + responseEntity.getBody());
        System.out.println(">richiesta login: r: h: " + responseEntity.getHeaders());

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
    }
}
