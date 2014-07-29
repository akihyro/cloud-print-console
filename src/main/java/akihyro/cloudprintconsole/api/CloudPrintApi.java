package akihyro.cloudprintconsole.api;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

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
import lombok.SneakyThrows;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * API。
 */
@ApplicationScoped
@Slf4j
public class CloudPrintApi {

    /** API情報 */
    @Getter
    private CloudPrintApiInfo apiInfo;

    /** 認証コードフロー */
    @Getter
    private AuthorizationCodeFlow codeFlow;

    /**
     * 初期化する。
     */
    @PostConstruct
    @SneakyThrows
    public void init() {
        apiInfo = CloudPrintApiInfo.load();
        codeFlow = newAuthorizationCodeFlow();
        log.debug("APIを初期化しました。 => {}", this);
    }

    /**
     * 解放する。
     */
    @PreDestroy
    public void release() {
        log.debug("APIを解放しました。 => {}", this);
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
                newNetHttpTransportForAuth(),
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
     * 認証用HTTPトランスポートを生成する。
     *
     * @return 認証用HTTPトランスポート。
     */
    private NetHttpTransport newNetHttpTransportForAuth() {
        val builder = new NetHttpTransport.Builder();
        builder.setProxy(apiInfo.takeProxy());
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
        log.debug("認可コードリクエストURIを生成します。");

        val url = codeFlow.newAuthorizationUrl();
        url.setRedirectUri(apiInfo.getRedirectUri().toString());
        val uri = url.toURI();

        log.debug("認可コードリクエストURIを生成しました。 => {}", uri);
        return uri;
    }

    /**
     * 認可コードから認証情報を取得/保存する。
     *
     * @param userId ユーザID。
     * @param authCode 認可コード。
     * @throws IOException IOエラー。
     */
    public void storeCredential(String userId, String authCode) throws IOException {
        log.debug("認証情報を取得/保存します。 => userId={}", userId);

        val req = codeFlow.newTokenRequest(authCode);
        req.setRedirectUri(apiInfo.getRedirectUri().toString());
        val res = req.execute();
        codeFlow.createAndStoreCredential(res, userId);

        log.debug("認証情報を取得/保存しました。 => userId={}", userId);
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
        log.debug("APIをコールします。 => userId={}, req={}", userId, req);

        // 認証情報をロードする
        val credential = codeFlow.loadCredential(userId);

        // HTTPクライアントを生成する
        val builder = HttpClientBuilder.create();
        builder.setProxy(apiInfo.takeProxyHttpHost());
        builder.setDefaultHeaders(Arrays.asList(
                new BasicHeader("Authorization", "OAuth " + credential.getAccessToken())));
        @Cleanup val httpClient = builder.build();

        // HTTPリクエストを発行し、HTTPレスポンスを得る
        val httpReq = req.toHttpReq(apiInfo);
        @Cleanup val httpRes = httpClient.execute(httpReq);
        val httpResEntity = httpRes.getEntity();

        // HTTPレスポンスをオブジェクトへ変換する
        @Cleanup val httpResContent = httpResEntity.getContent();
        val res = new ObjectMapper().readValue(httpResContent, req.getResType());

        log.debug("APIをコールしました。 => userId={}, res={}", userId, res);
        return res;
    }

}
