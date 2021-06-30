package TAASS.ServiceDBUtenti.response;

public class LoginResponse {
    private long id;
    private String userName;
    private String email;
    private String accessToken;

    public LoginResponse() {
    }

    public LoginResponse(long id, String userName, String email, String accessToken) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.accessToken = accessToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
