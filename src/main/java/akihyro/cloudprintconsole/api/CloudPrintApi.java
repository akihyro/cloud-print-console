package akihyro.cloudprintconsole.api;

import java.io.IOException;
import java.net.URI;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

import akihyro.cloudprintconsole.model.Printers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;

import lombok.Cleanup;
import lombok.Getter;
import lombok.val;

/**
 * API。
 */
@Named
@ApplicationScoped
public class CloudPrintApi {

    /** API情報 */
    private final CloudPrintApiInfo apiInfo;

    /** 認証コードフロー */
    @Getter
    private final AuthorizationCodeFlow codeFlow;

    /**
     * コンストラクタ。
     *
     * @throws IOException IOエラー。
     */
    public CloudPrintApi() throws IOException {
        apiInfo = CloudPrintApiInfo.load();
        codeFlow = newAuthorizationCodeFlow();
    }

    /**
     * 認証コードフローを生成する。
     *
     * @return 認証コードフロー。
     */
    private AuthorizationCodeFlow newAuthorizationCodeFlow() {
        val builder = new AuthorizationCodeFlow.Builder(
                BearerToken.authorizationHeaderAccessMethod(),
                new NetHttpTransport(),
                new JacksonFactory(),
                new GenericUrl(apiInfo.getTokenUri()),
                new ClientParametersAuthentication(apiInfo.getClientId(), apiInfo.getClientSecret()),
                apiInfo.getClientId(),
                apiInfo.getAuthUri().toString());
        builder.setScopes(apiInfo.getScopes());
        return builder.build();
    }

    /**
     * 認可コードリクエストURIを生成する。
     *
     * @return 認可コードリクエストURI。
     */
    public URI newAuthCodeReqURI() {
        val url = codeFlow.newAuthorizationUrl();
        url.setRedirectUri(apiInfo.getRedirectUri().toString());
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
        val req = codeFlow.newTokenRequest(authCode);
        req.setRedirectUri(apiInfo.getRedirectUri().toString());
        val res = req.execute();

        // 認証情報を得る
        val credential = codeFlow.createAndStoreCredential(res, null);
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
        val req = Request.Get(apiInfo.getApiUri() + "/search");
        req.addHeader("Authorization", "OAuth " + credential.getAccessToken());
        @Cleanup("discardContent") val res = req.execute();
        return new ObjectMapper().readValue(res.returnContent().asStream(), Printers.class);
    }

    /**
     * ジョブを投げる。
     *
     * @param credential 認証情報。
     * @param printerID プリンタID。
     * @return ジョブ結果。
     * @throws IOException IOエラー。
     */
    public String submitJob(CloudPrintCredential credential, String printerID) throws IOException {
        val req = Request.Post(apiInfo.getApiUri("submit"));
        req.addHeader("Authorization", "OAuth " + credential.getAccessToken());
        req.bodyForm(
                Form.form()
                    .add("printerid", printerID)
                    .add("title", "Cloud Print Console: Test Print.")
                    .add("content", "<h1>Cloud Print Console: Test Print.</h1><p>Printer ID: " + printerID + "</p>")
                    .add("contentType", "text/html")
                    .build()
            );
        @Cleanup("discardContent") val res = req.execute();
        return res.returnContent().asString();
    }

}
