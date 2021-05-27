package TAASS.ServiceDBUtenti.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public class UserDto {

    private long id;
    private String email;
    private String role;

    public UserDto(long id, String email, String role) {
        this.id = id;
        this.email = email;
        this.role = role;
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
}