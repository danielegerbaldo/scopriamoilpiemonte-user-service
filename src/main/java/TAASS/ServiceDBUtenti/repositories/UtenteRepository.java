package TAASS.ServiceDBUtenti.repositories;

import TAASS.ServiceDBUtenti.models.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UtenteRepository extends JpaRepository<Utente, Long> {
    List<Utente> findByNome(String nome);

    Utente findByEmail(String email);

    //Utente findById(long id);

    boolean existsByEmail(String email);

    List<Utente> findAllByDipendenteDiComune(long idComune);

    @Query(value = "SELECT utente.* FROM utente WHERE utente.comune <= 0", nativeQuery = true)
    List<Utente> findNonDipendenti();
}
