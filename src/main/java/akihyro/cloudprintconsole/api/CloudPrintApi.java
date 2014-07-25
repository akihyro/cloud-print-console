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
import com.google.api.client.util.store.MemoryDataStoreFactory;

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
     * @throws IOException IOエラー。
     */
    private AuthorizationCodeFlow newAuthorizationCodeFlow() throws IOException {
        val builder = new AuthorizationCodeFlow.Builder(
                BearerToken.authorizationHeaderAccessMethod(),
                new NetHttpTransport(),
                new JacksonFactory(),
                new GenericUrl(apiInfo.getTokenUri()),
                new ClientParametersAuthentication(apiInfo.getClientId(), apiInfo.getClientSecret()),
                apiInfo.getClientId(),
                apiInfo.getAuthUri().toString());
        builder.setScopes(apiInfo.getScopes());
        builder.setDataStoreFactory(MemoryDataStoreFactory.getDefaultInstance());
        return builder.build();
    }

    /**
     * 認証情報があるか判定する。
     *
     * @param userId ユーザID。
     * @return 認証情報があるかどうか。
     * @throws IOException IOエラー。
     */
    public boolean hasCredential(String userId) throws IOException {
        return codeFlow.loadCredential(userId) != null;
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
     * 認可コードから認証情報を取得/保存する。
     *
     * @param userId ユーザID。
     * @param authCode 認可コード。
     * @throws IOException IOエラー。
     */
    public void storeCredential(String userId, String authCode) throws IOException {
        val req = codeFlow.newTokenRequest(authCode);
        req.setRedirectUri(apiInfo.getRedirectUri().toString());
        val res = req.execute();
        codeFlow.createAndStoreCredential(res, userId);
    }

    /**
     * プリンタリストを取得する。
     *
     * @param userId ユーザID。
     * @return プリンタリスト。
     * @throws IOException IOエラー。
     */
    public Printers takePrinters(String userId) throws IOException {
        val credential = codeFlow.loadCredential(userId);
        val req = Request.Get(apiInfo.getApiUri() + "/search");
        req.addHeader("Authorization", "OAuth " + credential.getAccessToken());
        @Cleanup("discardContent") val res = req.execute();
        return new ObjectMapper().readValue(res.returnContent().asStream(), Printers.class);
    }

    /**
     * ジョブを投げる。
     *
     * @param userId ユーザID。
     * @param printerID プリンタID。
     * @return ジョブ結果。
     * @throws IOException IOエラー。
     */
    public String submitJob(String userId, String printerID) throws IOException {
        val credential = codeFlow.loadCredential(userId);
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
