package TAASS.ServiceDBUtenti.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Column(name = "iscrizioni")
    @ElementCollection(fetch = FetchType.EAGER)
    Set<Long> iscrizioni;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Column(name = "comune")
    //@DefaultValue(value = null)
    private long dipendenteDiComune;    //comune del quale sono sindaco o pubblicatore

    @Column(name = "picture_url")
    private String pictureUrl;

    @Column(name = "email_verified")
    private boolean emailVerified;

    public Utente(Long id, String nome, String cognome, String cf, String telefono, long comuneResidenza, String email, String password, List<Role> ruolo, long dipendenteDiComune) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.cf = cf;
        this.telefono = telefono;
        this.comuneResidenza = comuneResidenza;
        this.email = email;
        this.password = password;
        this.ruoli = ruolo;
        this.dipendenteDiComune = dipendenteDiComune;
        this.iscrizioni = new HashSet<Long>();
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

    public long getDipendenteDiComune() {
        return dipendenteDiComune;
    }

    public void setDipendenteDiComune(long comune) {
        this.dipendenteDiComune = comune;
    }

    public Set<Long> getIscrizioni() {
        return iscrizioni;
    }

    public void setIscrizioni(Set<Long> iscrizioni) {
        this.iscrizioni = iscrizioni;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }
}
