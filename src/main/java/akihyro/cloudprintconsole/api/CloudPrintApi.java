package akihyro.cloudprintconsole.api;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;

import akihyro.cloudprintconsole.api.model.CloudPrintApiReq;
import akihyro.cloudprintconsole.api.model.CloudPrintApiRes;

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
    @Getter
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
     * APIをコールする。
     *
     * @param userId ユーザID。
     * @param req APIリクエスト。
     * @return APIレスポンス。
     * @throws IOException IOエラー。
     */
    public <ResType extends CloudPrintApiRes> ResType
    call(String userId, CloudPrintApiReq<ResType> req) throws IOException {

        // 認証情報をロードする
        val credential = codeFlow.loadCredential(userId);

        // HTTPクライアントを生成する
        val builder = HttpClientBuilder.create();
        builder.setDefaultHeaders(Arrays.asList(
                new BasicHeader("Authorization", "OAuth " + credential.getAccessToken())));
        @Cleanup val httpClient = builder.build();

        // HTTPリクエストを発行し、HTTPレスポンスを得る
        val httpReq = req.toHttpReq(apiInfo);
        @Cleanup val httpRes = httpClient.execute(httpReq);
        val httpResEntity = httpRes.getEntity();

        // HTTPレスポンスをオブジェクトへ変換する
        @Cleanup val httpResContent = httpResEntity.getContent();
        return new ObjectMapper().readValue(httpResContent, req.getResType());
    }

}
