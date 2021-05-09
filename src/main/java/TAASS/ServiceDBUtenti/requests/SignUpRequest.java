package TAASS.ServiceDBUtenti.requests;

import TAASS.ServiceDBUtenti.models.Role;

import java.util.List;

public class SignUpRequest {
    //rappresenta la richiesta di registrazione; serve per avere il json
    private String email;   //noi useremo l'email per il login quindi come username
    private String password;
    private String nome;
    private String cognome;
    private List<Role> roles;

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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
