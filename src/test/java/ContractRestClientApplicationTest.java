import TAASS.ServiceDBUtenti.models.Utente;
import TAASS.ServiceDBUtenti.requests.LoginRequest;
import TAASS.ServiceDBUtenti.response.LoginResponse;
import com.github.tomakehurst.wiremock.common.Json;
import com.google.gson.JsonObject;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
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
        String accessToken = loginResponse.getBody().getAccessToken();

        //IMPOSTO TOKEN
        //loginResponse.getHeaders().add("Authorization", "Bearer" + accessToken);

        /*
        ResponseEntity<Utente> personResponseEntity = restTemplate.getForEntity("http://localhost/api/v1/utente/getUser/1",Utente.class);
        // then:
        BDDAssertions.then(personResponseEntity.getStatusCodeValue()).isEqualTo(200);
        BDDAssertions.then(personResponseEntity.getBody().getId()).isEqualTo(1);
        BDDAssertions.then(personResponseEntity.getBody().getNome()).isEqualTo("flavio");
        */
    }
    /*
    @Test
    public void getAllUsers(){
        RestTemplate restTemplate = new RestTemplate();
        //PROVO RICHIESTA CON AUTORIZZAZIONE
        ResponseEntity<Utente> utenteResponseEntity = restTemplate.getForEntity("http://localhost/api/v1/utente/getAllUser",Utente.class);
        if (utenteResponseEntity.getStatusCode() == HttpStatus.OK) {
            System.out.println(utenteResponseEntity.getBody().getCognome());
        } else if (utenteResponseEntity.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            System.out.println("NON AUTORIZZATO");
        }
    }
    */





}
