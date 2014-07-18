package akihyro.cloudprintconsole.api;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.ResourceBundle;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

import lombok.Cleanup;
import lombok.Getter;
import akihyro.cloudprintconsole.model.Printers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
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

    /**
     * 認可コードから認証情報を得る。
     *
     * @param authCode 認可コード。
     * @return 認証情報。
     * @throws IOException IOエラー。
     */
    public CloudPrintCredential takeCredential(String authCode) throws IOException {

        // アクセストークンを得る
        AuthorizationCodeTokenRequest req = codeFlow.newTokenRequest(authCode);
        req.setRedirectUri(apiInfo.getString("redirect-url"));
        TokenResponse res = req.execute();

        // 認証情報を得る
        Credential credential = codeFlow.createAndStoreCredential(res, null);
        return new CloudPrintCredential(credential);
    }

    /**
     * プリンタリストを取得する。
     *
     * @param credential 認証情報。
     * @return プリンタリスト。
     * @throws IOException IOエラー。
     */
    public Printers takePrinters(CloudPrintCredential credential) throws IOException {
        Request req = Request.Get(apiInfo.getString("api-url") + "/search");
        req.addHeader("Authorization", "OAuth " + credential.getAccessToken());
        @Cleanup("discardContent") Response res = req.execute();
        return new ObjectMapper().readValue(res.returnContent().asStream(), Printers.class);
    }

}
