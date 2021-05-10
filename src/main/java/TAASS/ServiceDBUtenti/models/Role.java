package TAASS.ServiceDBUtenti.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_ADMIN, ROLE_CLIENT, ROLE_MAYOR, ROLE_PUBLISHER;

    public String getAuthority() {
        return name();
    }

}