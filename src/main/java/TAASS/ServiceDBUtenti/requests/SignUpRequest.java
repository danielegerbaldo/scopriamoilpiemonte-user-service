package TAASS.ServiceDBUtenti.requests;

import TAASS.ServiceDBUtenti.models.Role;

import java.util.List;

public class SignUpRequest {
    //rappresenta la richiesta di registrazione; serve per avere il json
    private String email;   //noi useremo l'email per il login quindi come username
    private String password;
    private String nome;
    private String cognome;
    private String cf;
    private String telefono;
    private Long comuneResidenza;
    private Long dipendenteDiComune;
    private List<Role> roles;

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

    public Long getComuneResidenza() {
        return comuneResidenza;
    }

    public void setComuneResidenza(Long comuneResidenza) {
        this.comuneResidenza = comuneResidenza;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Long getDipendenteDiComune() {
        return dipendenteDiComune;
    }

    public void setDipendenteDiComune(Long dipendenteDiComune) {
        this.dipendenteDiComune = dipendenteDiComune;
    }
}
