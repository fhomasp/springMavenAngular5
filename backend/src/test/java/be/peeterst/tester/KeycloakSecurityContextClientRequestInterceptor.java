package be.peeterst.tester;

import java.io.IOException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: Thomas
 * Date: 10/09/2018
 * Time: 20:39
 */


@Component
public class KeycloakSecurityContextClientRequestInterceptor implements ClientHttpRequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";


    @Value("${keycloak.auth-server-url}")
    protected String authServer;

    @Value("${keycloak.realm}")
    protected String realm;

    @Value("${keycloak.resource}")
    protected String resource;

    @Value("${keycloak.test-user}")
    protected String keycloakTestUser;

    @Value("${keycloak.test-password}")
    protected String keycloakTestPassword;

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        AccessTokenResponse token = getAccessTokenResponse();
        httpRequest.getHeaders().set(AUTHORIZATION_HEADER, "Bearer " + token.getToken());
        return clientHttpRequestExecution.execute(httpRequest, bytes);
    }

    private AccessTokenResponse getAccessTokenResponse() {
        return Keycloak.getInstance(authServer, realm, keycloakTestUser, keycloakTestPassword, resource).tokenManager().getAccessToken();
    }


    public void setKeycloakTestUser(String keycloakTestUser) {
        this.keycloakTestUser = keycloakTestUser;
    }

    public String getKeycloakTestUser(){
        return keycloakTestUser;
    }

    public String getKeycloakTestPassword() {
        return keycloakTestPassword;
    }

    public void setKeycloakTestPassword(String keycloakTestPassword) {
        this.keycloakTestPassword = keycloakTestPassword;
    }
}