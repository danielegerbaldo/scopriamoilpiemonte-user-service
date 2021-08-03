import TAASS.ServiceDBUtenti.models.Utente;
import TAASS.ServiceDBUtenti.requests.LoginRequest;
import TAASS.ServiceDBUtenti.response.LoginResponse;
import com.github.tomakehurst.wiremock.common.Json;
import com.google.gson.JsonObject;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerExtension;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;


public class ContractRestClientApplicationTest {
    @RegisterExtension
    public StubRunnerExtension stubRunner = new StubRunnerExtension()
            .downloadStub("com.example", "contract-rest-service", "0.0.1-SNAPSHOT", "stubs")
            .stubsMode(StubRunnerProperties.StubsMode.LOCAL);

    private String accessToken = "";

    @Test
    public void login(){
        Object n = "flavio@gmail.com";
        Object p = "flavio";
        // create request body
        JSONObject request = new JSONObject();
        try{
            request.put("email", n);
            request.put("password", p);
        }catch (JSONException e){
            System.out.println("Error JSON");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);
        String urlString = "http://localhost/api/v1/login";
        // Ottengo LOGIN
        ResponseEntity<LoginResponse> loginResponse = new RestTemplate()
                .exchange(urlString, HttpMethod.POST, entity, LoginResponse.class);
        //Controllo se è andata bene
        if (loginResponse.getStatusCode() == HttpStatus.OK) {
            System.out.println("token: "+loginResponse.getBody().getAccessToken());
        } else if (loginResponse.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            System.out.println("NON AUTORIZZATO");
        }
        this.accessToken = loginResponse.getBody().getAccessToken();
    }

    @Test
    public void getAllUsers(){
        this.login();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers = new HttpHeaders();
        String url="http://localhost/api/v1/utente/getAllUser";
        System.out.println("MyAccessToken: " + accessToken);
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        ResponseEntity<String> utenteResponseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        System.out.println(utenteResponseEntity.getBody());
    }
    @Test
    public void signUp(){
        String Json ="*Add Here*";
        /*
        String Json="{\n" +
                "    \"email\":\"flavio1@gmail.com\",\n" +
                "    \"password\":\"flavio\",\n" +
                "    \"nome\":\"Flavio\",\n" +
                "    \"cognome\":\"Roman\",\n" +
                "    \"cf\":\"cf\",\n" +
                "    \"telefono\":\"3333456333\",\n" +
                "    \"comuneResidenza\":1265,\n" +
                "    \"dipendenteDiComune\": 1265,\n" +
                "    \"roles\":[\"ROLE_ADMIN\", \"ROLE_MAYOR\"]\n" +
                "}";
        */

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(Json);
        }catch (JSONException err){
            System.out.println(err.toString());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
        String urlString = "http://localhost/api/v1/signUp";

        ResponseEntity<LoginResponse> loginResponse = new RestTemplate()
                .exchange(urlString, HttpMethod.POST, entity, LoginResponse.class);
        //Controllo se è andata bene
        if (loginResponse.getStatusCode() == HttpStatus.OK) {
            System.out.println("OK");
        } else if (loginResponse.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            System.out.println("NON AUTORIZZATO");
        }
        System.out.println("result: " +loginResponse.getBody().getUserName());
    }







}
