package akihyro.cloudprintconsole.api;

import java.net.URI;
import java.util.Arrays;
import java.util.ResourceBundle;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import lombok.Getter;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;

/**
 * ファサード。
 */
@Named
@ApplicationScoped
public class CloudPrintFacade {

    /**
     * API情報。
     */
    private static final ResourceBundle apiInfo = ResourceBundle.getBundle("api");

    /** 認可コードフロー */
    @Getter
    private final AuthorizationCodeFlow codeFlow;

    /**
     * コンストラクタ。
     */
    public CloudPrintFacade() {
        codeFlow = newAuthorizationCodeFlow();
    }

    /**
     * 認可コードフローを生成する。
     *
     * @return 認可コードフロー。
     */
    private AuthorizationCodeFlow newAuthorizationCodeFlow() {
        AuthorizationCodeFlow.Builder builder = new AuthorizationCodeFlow.Builder(
                BearerToken.authorizationHeaderAccessMethod(),
                new NetHttpTransport(),
                new JacksonFactory(),
                new GenericUrl(apiInfo.getString("token-url")),
                new ClientParametersAuthentication(
                        apiInfo.getString("client-id"), apiInfo.getString("client-secret")),
                apiInfo.getString("client-id"),
                apiInfo.getString("auth-url"));
        builder.setScopes(Arrays.asList(apiInfo.getString("scopes").split(" ")));
        return builder.build();
    }

    /**
     * 認可コードリクエストURIを生成する。
     *
     * @return 認可コードリクエストURI。
     */
    public URI newAuthCodeReqURI() {
        AuthorizationCodeRequestUrl url = codeFlow.newAuthorizationUrl();
        url.setRedirectUri(apiInfo.getString("redirect-url"));
        return url.toURI();
    }

}
