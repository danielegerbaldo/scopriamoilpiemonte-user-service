package TAASS.ServiceDBUtenti.repositories;

import TAASS.ServiceDBUtenti.models.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UtenteRepository extends JpaRepository<Utente, Long> {
    List<Utente> findByNome(String nome);

    Utente findByEmail(String email);

    //Utente findById(long id);

    boolean existsByEmail(String email);

    List<Utente> findAllByDipendenteDiComune(long idComune);
}
