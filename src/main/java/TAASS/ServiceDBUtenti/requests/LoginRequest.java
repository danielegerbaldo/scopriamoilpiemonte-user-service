package TAASS.ServiceDBUtenti.requests;

public class LoginRequest {
    //rappresenta la richiesta per il login; mi serve praticemente per ottenere il json
    private String email;
    private String password;

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
}
