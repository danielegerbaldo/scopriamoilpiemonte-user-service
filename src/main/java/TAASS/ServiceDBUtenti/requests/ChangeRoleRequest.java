package TAASS.ServiceDBUtenti.requests;

import TAASS.ServiceDBUtenti.models.Role;

import java.util.List;

public class ChangeRoleRequest {
    private long id;
    private List<Role> ruoli;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Role> getRuoli() {
        return ruoli;
    }

    public void setRuoli(List<Role> ruoli) {
        this.ruoli = ruoli;
    }
}
