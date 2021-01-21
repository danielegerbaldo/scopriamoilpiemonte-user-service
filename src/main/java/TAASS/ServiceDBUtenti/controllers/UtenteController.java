package TAASS.ServiceDBUtenti.controllers;

import TAASS.ServiceDBUtenti.models.Utente;
import TAASS.ServiceDBUtenti.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
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
        return utenti;
    }

    @PostMapping
    public Utente postUtente(@RequestBody Utente utente){
        Utente nuovoUtente = utenteRepository.save(new Utente(utente.getNome(), utente.getCognome(), utente.getCf(), utente.getTelefono(), utente.getComuneResidenza()));
        return nuovoUtente;
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
