package TAASS.ServiceDBUtenti.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema = "public")
public class Utente {
    //dichiarazione elementi tabella
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="nome")
    private String nome;

    @Column(name="cognome")
    private String cognome;

    @Column(name="cf")
    private String cf;

    @Column(name="telefono")
    private String telefono;

    @Column(name="comune_residenza")
    private long comuneResidenza;   //comune di residenza

    @Column(name = "email", unique=true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "ruoli")
    @ElementCollection(fetch = FetchType.EAGER)
    List<Role> ruoli;

    @Column(name = "comune")
    //@DefaultValue(value = null)
    private long comune;    //comune del quale sono sindaco o pubblicatore

    public Utente(Long id, String nome, String cognome, String cf, String telefono, long comuneResidenza, String email, String password, List<Role> ruolo, long comune) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.cf = cf;
        this.telefono = telefono;
        this.comuneResidenza = comuneResidenza;
        this.email = email;
        this.password = password;
        this.ruoli = ruolo;
        this.comune = comune;
    }

    public Utente() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public long getComuneResidenza() {
        return comuneResidenza;
    }

    public void setComuneResidenza(long comuneResidenza) {
        this.comuneResidenza = comuneResidenza;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRuoli() {
        return ruoli;
    }

    public void setRuoli(List<Role> ruolo) {
        this.ruoli = ruolo;
    }

    public long getComune() {
        return comune;
    }

    public void setComune(long comune) {
        this.comune = comune;
    }
}
