package TAASS.ServiceDBUtenti.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public class UserDto {

    private long id;
    private String email;
    private String role;
    private long dipendenteDiComune;

    public UserDto(long id, String email, String role, long dipendenteDiComune) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.dipendenteDiComune = dipendenteDiComune;
    }

    public long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public long getDipendenteDiComune() {
        return dipendenteDiComune;
    }
}